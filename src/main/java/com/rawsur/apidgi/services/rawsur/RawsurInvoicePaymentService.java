package com.rawsur.apidgi.services.rawsur;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rawsur.apidgi.converters.SfecInvoiceConverter;
import com.rawsur.apidgi.dto.PeriodeDTO;
import com.rawsur.apidgi.models.dgi.SfecInvoice;
import com.rawsur.apidgi.models.rawsur.RawsurInvoicePayment;
import com.rawsur.apidgi.repositories.dgi.SfecInvoiceRepo;
import com.rawsur.apidgi.repositories.rawsur.RawsurInvoicePaymentRepo;
import com.rawsur.apidgi.services.dgi.IntermediaryService;

@Service
public class RawsurInvoicePaymentService {

    @Autowired
    private RawsurInvoicePaymentRepo rawsurInvoicePaymentRepo;

    @Autowired
    private SfecInvoiceRepo sfecInvoiceRepo;

    @Autowired
    private IntermediaryService intermediaryService;

    public List<RawsurInvoicePayment> getInvoices(PeriodeDTO periodeDTO) {
        List<Integer> allowedCodes = this.intermediaryService.getAllowedIntermediaryCodes();
        if (allowedCodes != null && allowedCodes.isEmpty()) {
            return Collections.emptyList();
        }

        List<RawsurInvoicePayment> invoicePayments;
        if (allowedCodes == null) {
            invoicePayments = this.rawsurInvoicePaymentRepo
                    .findByDateEncaBetween(periodeDTO.getDateDebut(), periodeDTO.getDateFin());
        } else {
            invoicePayments = this.rawsurInvoicePaymentRepo.findByDateEncaBetweenAndCodeInteIn(
                    periodeDTO.getDateDebut(), periodeDTO.getDateFin(), allowedCodes);
        }
        return enrichWithSfecStatus(invoicePayments);
    }

    public List<RawsurInvoicePayment> getInvoicesByReference(String reference) {
        InvoiceReferenceCriteria criteria = this.parseReference(reference);
        List<Integer> allowedCodes = this.intermediaryService.getAllowedIntermediaryCodes();
        if (allowedCodes != null && allowedCodes.isEmpty()) {
            return Collections.emptyList();
        }

        if (allowedCodes != null && criteria.codeIntermediaire != null
                && !allowedCodes.contains(criteria.codeIntermediaire)) {
            return Collections.emptyList();
        }

        List<RawsurInvoicePayment> invoicePayments;
        if (allowedCodes == null) {
            invoicePayments = this.rawsurInvoicePaymentRepo
                    .findByCriteria(criteria.codeIntermediaire, criteria.numeroPolice, criteria.numeroAvenant);
        } else {
            invoicePayments = this.rawsurInvoicePaymentRepo.findByCriteriaRestricted(
                    allowedCodes, criteria.codeIntermediaire, criteria.numeroPolice, criteria.numeroAvenant);
        }
        return enrichWithSfecStatus(invoicePayments);
    }

    private List<RawsurInvoicePayment> enrichWithSfecStatus(List<RawsurInvoicePayment> invoicePayments) {
        if (invoicePayments == null || invoicePayments.isEmpty()) {
            return invoicePayments;
        }

        List<String> invoiceIds = invoicePayments.stream()
                .map(RawsurInvoicePayment::getCle)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        Map<String, SfecInvoice> sfecByExternalId = invoiceIds.isEmpty()
                ? Collections.emptyMap()
                : sfecInvoiceRepo.findByExternalInvoiceIdInWithCertification(invoiceIds).stream()
                        .collect(Collectors.toMap(SfecInvoice::getExternalInvoiceId, Function.identity(), (a, b) -> a));

        for (RawsurInvoicePayment invoicePayment : invoicePayments) {
            SfecInvoice sfecInvoice = sfecByExternalId.get(invoicePayment.getCle());
            if (sfecInvoice == null) {
                invoicePayment.setStatus("0");
                invoicePayment.setSfecResponse(null);
                continue;
            }

            invoicePayment.setStatus(sfecInvoice.getStatus() != null ? sfecInvoice.getStatus().name() : "0");
            invoicePayment.setSfecResponse(
                    SfecInvoiceConverter.toCertificationResponseDto(sfecInvoice.getCertification()));
        }

        return invoicePayments;
    }

    private InvoiceReferenceCriteria parseReference(String reference) {
        if (reference == null || reference.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Le critere de recherche est obligatoire au format code_intermediaire-numeropolice-numeroavenant");
        }

        String[] parts = reference.trim().split("-", -1);
        if (parts.length > 3) {
            throw new IllegalArgumentException(
                    "Le critere de recherche ne doit contenir que 3 elements au maximum");
        }

        Integer codeIntermediaire = this.parseInteger(parts, 0, "code_intermediaire");
        Long numeroPolice = this.parseLong(parts, 1, "numeropolice");
        Long numeroAvenant = this.parseLong(parts, 2, "numeroavenant");

        if (codeIntermediaire == null && numeroPolice == null && numeroAvenant == null) {
            throw new IllegalArgumentException(
                    "Au moins un element de recherche est requis dans le critere");
        }

        return new InvoiceReferenceCriteria(codeIntermediaire, numeroPolice, numeroAvenant);
    }

    private Integer parseInteger(String[] parts, int index, String label) {
        if (index >= parts.length || parts[index].trim().isEmpty()) {
            return null;
        }

        try {
            return Integer.valueOf(parts[index].trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(label + " doit etre numerique");
        }
    }

    private Long parseLong(String[] parts, int index, String label) {
        if (index >= parts.length || parts[index].trim().isEmpty()) {
            return null;
        }

        try {
            return Long.valueOf(parts[index].trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(label + " doit etre numerique");
        }
    }

    private static class InvoiceReferenceCriteria {
        private final Integer codeIntermediaire;
        private final Long numeroPolice;
        private final Long numeroAvenant;

        private InvoiceReferenceCriteria(Integer codeIntermediaire, Long numeroPolice, Long numeroAvenant) {
            this.codeIntermediaire = codeIntermediaire;
            this.numeroPolice = numeroPolice;
            this.numeroAvenant = numeroAvenant;
        }
    }
}
