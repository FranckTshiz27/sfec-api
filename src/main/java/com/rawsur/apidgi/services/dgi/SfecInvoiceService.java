package com.rawsur.apidgi.services.dgi;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.rawsur.apidgi.converters.EncaissementToSfecInvoiceConverter;
import com.rawsur.apidgi.converters.SfecInvoiceConverter;
import com.rawsur.apidgi.dto.PeriodeDTO;
import com.rawsur.apidgi.dto.dgi.sfec.CreateSfecInvoiceDto;
import com.rawsur.apidgi.dto.dgi.sfec.SfecCertificationResponseDto;
import com.rawsur.apidgi.dto.dgi.sfec.SfecInvoiceResponseDto;
import com.rawsur.apidgi.enums.SfecInvoiceStatus;
import com.rawsur.apidgi.exceptions.ConflitException;
import com.rawsur.apidgi.exceptions.SfecException;
import com.rawsur.apidgi.models.dgi.SfecInvoice;
import com.rawsur.apidgi.models.rawsur.RawsurInvoicePayment;
import com.rawsur.apidgi.repositories.dgi.SfecInvoiceRepo;

@Service
public class SfecInvoiceService {

    private static final Set<String> RECIPIENT_TYPES_REQUIRING_NAME = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList("business", "government", "foreign")));
    private static final Set<String> RECIPIENT_TYPES_REQUIRING_NIU = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList("business", "government")));
    private static final Set<String> RECIPIENT_TYPES_REQUIRING_CONTACT = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList("government", "foreign")));

    @Autowired
    private SfecInvoiceRepo sfecInvoiceRepo;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment env;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    public SfecInvoiceResponseDto submitInvoice(CreateSfecInvoiceDto request) {
        validateBusinessRules(request);

        if (sfecInvoiceRepo.existsByExternalInvoiceId(request.getInvoiceId())) {
            throw new ConflitException("Une facture avec l'identifiant '" + request.getInvoiceId() + "' existe deja");
        }

        SfecInvoice invoice = SfecInvoiceConverter.toEntity(request);
        invoice.setStatus(SfecInvoiceStatus.SUBMITTED);
        invoice = sfecInvoiceRepo.save(invoice);

        try {
            SfecCertificationResponseDto apiResponse = callSfecApi(request);
            SfecInvoiceConverter.applyCertificationResponse(invoice, apiResponse);
            invoice = sfecInvoiceRepo.save(invoice);
            SfecInvoiceResponseDto response = SfecInvoiceConverter.toResponseDto(invoice);
            mergeCertificationFields(response, apiResponse);
            return response;
        } catch (RestClientResponseException ex) {
            invoice.setStatus(SfecInvoiceStatus.FAILED);
            invoice.setErrorMessage(ex.getResponseBodyAsString());
            sfecInvoiceRepo.save(invoice);
            throw ex;
        } catch (Exception ex) {
            invoice.setStatus(SfecInvoiceStatus.FAILED);
            invoice.setErrorMessage(ex.getMessage());
            sfecInvoiceRepo.save(invoice);
            throw new SfecException("Erreur lors de la soumission de la facture au SFEC: " + ex.getMessage());
        }
    }

    @Transactional
    public SfecInvoiceResponseDto findById(java.util.UUID id) {
        SfecInvoice invoice = sfecInvoiceRepo.findById(id)
                .orElseThrow(() -> new SfecException("Facture SFEC non trouvee avec l'ID: " + id));
        return SfecInvoiceConverter.toResponseDto(invoice);
    }

    @Transactional
    public SfecInvoiceResponseDto createFromEncaissement(RawsurInvoicePayment encaissement) {
        CreateSfecInvoiceDto request = EncaissementToSfecInvoiceConverter.toCreateSfecInvoiceDto(encaissement);
        return submitInvoice(request);
    }

    @Transactional
    public List<SfecInvoiceResponseDto> findByPeriod(PeriodeDTO periodeDTO) {
        if (periodeDTO.getDateDebut() == null || periodeDTO.getDateFin() == null) {
            throw new SfecException("dateDebut et dateFin sont obligatoires");
        }

        Instant startDate = periodeDTO.getDateDebut().atStartOfDay(java.time.ZoneOffset.UTC).toInstant();
        Instant endDate = periodeDTO.getDateFin().plusDays(1).atStartOfDay(java.time.ZoneOffset.UTC).toInstant();

        return sfecInvoiceRepo.findByCreatedAtPeriod(startDate, endDate).stream()
                .map(SfecInvoiceConverter::toResponseDto)
                .collect(Collectors.toList());
    }

    private SfecCertificationResponseDto callSfecApi(CreateSfecInvoiceDto request) {
        String url = env.getProperty("sfec.api.invoices-endpoint");
        if (url == null || url.isBlank()) {
            throw new SfecException("L'URL SFEC n'est pas configuree (sfec.api.invoices-endpoint)");
        }

        String apiKey = env.getProperty("sfec.api.key");
        if (apiKey == null || apiKey.isBlank()) {
            throw new SfecException("La cle API SFEC n'est pas configuree (sfec.api.key)");
        }

        System.out.println("url: " + url);
        try {
            System.out.println("request json: " + objectMapper.writeValueAsString(request));
        } catch (Exception e) {
            System.out.println("request: " + request);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("X-API-Key", apiKey);

        HttpEntity<CreateSfecInvoiceDto> httpEntity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<SfecCertificationResponseDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    httpEntity,
                    SfecCertificationResponseDto.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                System.out.println("SFEC erreur: statut=" + response.getStatusCode() + ", body=" + response.getBody());
                throw new SfecException("Reponse invalide du service SFEC");
            }

            SfecCertificationResponseDto body = response.getBody();
            // Lien explicite avec la requete SFEC (invoice_id envoye)
            body.setInvoiceId(request.getInvoiceId());
            return body;
        } catch (RestClientResponseException ex) {
            System.out.println("SFEC erreur: statut=" + ex.getRawStatusCode()
                    + ", message=" + ex.getMessage()
                    + ", body=" + ex.getResponseBodyAsString());
            throw ex;
        }
    }

    private void validateBusinessRules(CreateSfecInvoiceDto request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new SfecException("La facture doit contenir au moins un article");
        }

        if ("creditNote".equals(request.getInvoiceType())
                && (request.getReferenceInvoiceId() == null || request.getReferenceInvoiceId().isBlank())) {
            throw new SfecException("reference_invoice_id est obligatoire pour une facture d'avoir (creditNote)");
        }

        String recipientType = request.getRecipientType();
        if (RECIPIENT_TYPES_REQUIRING_NAME.contains(recipientType) && isBlank(request.getRecipientName())) {
            throw new SfecException("recipient_name est obligatoire pour recipient_type=" + recipientType);
        }

        if (RECIPIENT_TYPES_REQUIRING_NIU.contains(recipientType)) {
            if (isBlank(request.getRecipientNiu())) {
                throw new SfecException("recipient_niu est obligatoire pour recipient_type=" + recipientType);
            }
            int niuLength = request.getRecipientNiu().trim().length();
            if (niuLength != 16 && niuLength != 17) {
                throw new SfecException("recipient_niu doit contenir 16 ou 17 caracteres");
            }
        }

        if (RECIPIENT_TYPES_REQUIRING_CONTACT.contains(recipientType)) {
            if (isBlank(request.getRecipientPhone())) {
                throw new SfecException("recipient_phone est obligatoire pour recipient_type=" + recipientType);
            }
            if (isBlank(request.getRecipientEmail())) {
                throw new SfecException("recipient_email est obligatoire pour recipient_type=" + recipientType);
            }
            if (isBlank(request.getRecipientAddress())) {
                throw new SfecException("recipient_address est obligatoire pour recipient_type=" + recipientType);
            }
        }
    }

    private void mergeCertificationFields(SfecInvoiceResponseDto target, SfecCertificationResponseDto apiResponse) {
        if (apiResponse.getCertificationNumber() != null) {
            target.setCertificationNumber(apiResponse.getCertificationNumber());
        }
        if (apiResponse.getSignature() != null) {
            target.setSignature(apiResponse.getSignature());
        }
        if (apiResponse.getShortSignature() != null) {
            target.setShortSignature(apiResponse.getShortSignature());
        }
        if (apiResponse.getQrCode() != null) {
            target.setQrCode(apiResponse.getQrCode());
        }
        if (apiResponse.getCertificationDate() != null) {
            target.setCertificationDate(apiResponse.getCertificationDate());
        }
        if (apiResponse.getInvoiceNumber() != null) {
            target.setInvoiceNumber(apiResponse.getInvoiceNumber());
        }
        if (apiResponse.getInvoiceId() != null) {
            target.setInvoiceId(apiResponse.getInvoiceId());
        }
        if (apiResponse.getIdentifier() != null) {
            target.setIdentifier(apiResponse.getIdentifier());
        }
        target.setStatus(SfecInvoiceStatus.CERTIFIED.name());
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
