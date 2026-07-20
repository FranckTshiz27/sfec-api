package com.rawsur.apidgi.converters;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.rawsur.apidgi.dto.dgi.sfec.CreateSfecInvoiceDto;
import com.rawsur.apidgi.dto.dgi.sfec.SfecAdditionalTaxDto;
import com.rawsur.apidgi.dto.dgi.sfec.SfecCertificationResponseDto;
import com.rawsur.apidgi.dto.dgi.sfec.SfecInvoiceItemDto;
import com.rawsur.apidgi.dto.dgi.sfec.SfecInvoiceResponseDto;
import com.rawsur.apidgi.enums.SfecInvoiceStatus;
import com.rawsur.apidgi.models.dgi.SfecAdditionalTax;
import com.rawsur.apidgi.models.dgi.SfecCertification;
import com.rawsur.apidgi.models.dgi.SfecInvoice;
import com.rawsur.apidgi.models.dgi.SfecInvoiceItem;

public final class SfecInvoiceConverter {

    private SfecInvoiceConverter() {
    }

    public static SfecInvoice toEntity(CreateSfecInvoiceDto dto) {
        SfecInvoice invoice = new SfecInvoice();
        invoice.setExternalInvoiceId(dto.getInvoiceId());
        invoice.setTaxpayerNiu(dto.getTaxpayerNiu());
        invoice.setInvoiceType(dto.getInvoiceType());
        invoice.setInvoiceSubject(dto.getInvoiceSubject());
        invoice.setInvoiceDueDate(dto.getInvoiceDueDate());
        invoice.setReferenceInvoiceId(dto.getReferenceInvoiceId());
        invoice.setSciet(dto.getSciet());
        invoice.setTerminalIdentifier(dto.getTerminalIdentifier());
        invoice.setSubtotal(dto.getSubtotal());
        invoice.setTotalTaxTAmount(dto.getTotalTaxTAmount());
        invoice.setTotalTaxRAmount(dto.getTotalTaxRAmount());
        invoice.setTotalExemptAmount(dto.getTotalExemptAmount());
        invoice.setTotalTaxAmount(dto.getTotalTaxAmount());
        invoice.setDiscountAmount(dto.getDiscountAmount());
        invoice.setAmountDue(dto.getAmountDue());
        invoice.setTotalLineDiscountAmount(dto.getTotalLineDiscountAmount());
        invoice.setAdditionalCentTax(dto.getAdditionalCentTax());
        invoice.setElectronicStampDuty(dto.getElectronicStampDuty());
        invoice.setTotalAmount(dto.getTotalAmount());
        invoice.setCurrency(dto.getCurrency());
        invoice.setRecipientType(dto.getRecipientType());
        invoice.setRecipientName(dto.getRecipientName());
        invoice.setRecipientNiu(dto.getRecipientNiu());
        invoice.setRecipientRccm(dto.getRecipientRccm());
        invoice.setRecipientAddress(dto.getRecipientAddress());
        invoice.setRecipientPhone(dto.getRecipientPhone());
        invoice.setRecipientEmail(dto.getRecipientEmail());
        invoice.setIsRecipientTaxable(dto.getIsRecipientTaxable());
        invoice.setPaymentMethod(dto.getPaymentMethod());
        invoice.setPaymentReference(dto.getPaymentReference());
        invoice.setPaymentDate(dto.getPaymentDate());
        invoice.setNotes(dto.getNotes());
        invoice.setStatus(SfecInvoiceStatus.DRAFT);

        List<SfecAdditionalTax> taxes = safeList(dto.getAdditionalTaxes()).stream()
                .map(SfecInvoiceConverter::toAdditionalTaxEntity)
                .collect(Collectors.toList());
        taxes.forEach(tax -> tax.setInvoice(invoice));
        invoice.setAdditionalTaxes(taxes);

        List<SfecInvoiceItem> items = safeList(dto.getItems()).stream()
                .map(SfecInvoiceConverter::toItemEntity)
                .collect(Collectors.toList());
        items.forEach(item -> item.setInvoice(invoice));
        invoice.setItems(items);

        return invoice;
    }

    public static CreateSfecInvoiceDto toRequestDto(SfecInvoice invoice) {
        CreateSfecInvoiceDto dto = new CreateSfecInvoiceDto();
        dto.setInvoiceId(invoice.getExternalInvoiceId());
        dto.setTaxpayerNiu(invoice.getTaxpayerNiu());
        dto.setInvoiceType(invoice.getInvoiceType());
        dto.setInvoiceSubject(invoice.getInvoiceSubject());
        dto.setInvoiceDueDate(invoice.getInvoiceDueDate());
        dto.setReferenceInvoiceId(invoice.getReferenceInvoiceId());
        dto.setSciet(invoice.getSciet());
        dto.setTerminalIdentifier(invoice.getTerminalIdentifier());
        dto.setSubtotal(invoice.getSubtotal());
        dto.setTotalTaxTAmount(invoice.getTotalTaxTAmount());
        dto.setTotalTaxRAmount(invoice.getTotalTaxRAmount());
        dto.setTotalExemptAmount(invoice.getTotalExemptAmount());
        dto.setTotalTaxAmount(invoice.getTotalTaxAmount());
        dto.setDiscountAmount(invoice.getDiscountAmount());
        dto.setAmountDue(invoice.getAmountDue());
        dto.setTotalLineDiscountAmount(invoice.getTotalLineDiscountAmount());
        dto.setAdditionalCentTax(invoice.getAdditionalCentTax());
        dto.setElectronicStampDuty(invoice.getElectronicStampDuty());
        dto.setTotalAmount(invoice.getTotalAmount());
        dto.setCurrency(invoice.getCurrency());
        dto.setRecipientType(invoice.getRecipientType());
        dto.setRecipientName(invoice.getRecipientName());
        dto.setRecipientNiu(invoice.getRecipientNiu());
        dto.setRecipientRccm(invoice.getRecipientRccm());
        dto.setRecipientAddress(invoice.getRecipientAddress());
        dto.setRecipientPhone(invoice.getRecipientPhone());
        dto.setRecipientEmail(invoice.getRecipientEmail());
        dto.setIsRecipientTaxable(invoice.getIsRecipientTaxable());
        dto.setPaymentMethod(invoice.getPaymentMethod());
        dto.setPaymentReference(invoice.getPaymentReference());
        dto.setPaymentDate(invoice.getPaymentDate());
        dto.setNotes(invoice.getNotes());
        dto.setAdditionalTaxes(safeList(invoice.getAdditionalTaxes()).stream()
                .map(SfecInvoiceConverter::toAdditionalTaxDto)
                .collect(Collectors.toList()));
        dto.setItems(safeList(invoice.getItems()).stream()
                .map(SfecInvoiceConverter::toItemDto)
                .collect(Collectors.toList()));
        return dto;
    }

    public static SfecInvoiceResponseDto toResponseDto(SfecInvoice invoice) {
        SfecInvoiceResponseDto dto = new SfecInvoiceResponseDto();
        dto.setId(invoice.getId());
        dto.setInvoiceId(invoice.getExternalInvoiceId());
        dto.setTaxpayerNiu(invoice.getTaxpayerNiu());
        dto.setInvoiceType(invoice.getInvoiceType());
        dto.setInvoiceSubject(invoice.getInvoiceSubject());
        dto.setInvoiceDueDate(invoice.getInvoiceDueDate());
        dto.setReferenceInvoiceId(invoice.getReferenceInvoiceId());
        dto.setSciet(invoice.getSciet());
        dto.setTerminalIdentifier(invoice.getTerminalIdentifier());
        dto.setSubtotal(invoice.getSubtotal());
        dto.setTotalTaxTAmount(invoice.getTotalTaxTAmount());
        dto.setTotalTaxRAmount(invoice.getTotalTaxRAmount());
        dto.setTotalExemptAmount(invoice.getTotalExemptAmount());
        dto.setTotalTaxAmount(invoice.getTotalTaxAmount());
        dto.setDiscountAmount(invoice.getDiscountAmount());
        dto.setAmountDue(invoice.getAmountDue());
        dto.setTotalLineDiscountAmount(invoice.getTotalLineDiscountAmount());
        dto.setAdditionalCentTax(invoice.getAdditionalCentTax());
        dto.setElectronicStampDuty(invoice.getElectronicStampDuty());
        dto.setTotalAmount(invoice.getTotalAmount());
        dto.setCurrency(invoice.getCurrency());
        dto.setRecipientType(invoice.getRecipientType());
        dto.setRecipientName(invoice.getRecipientName());
        dto.setRecipientNiu(invoice.getRecipientNiu());
        dto.setRecipientRccm(invoice.getRecipientRccm());
        dto.setRecipientAddress(invoice.getRecipientAddress());
        dto.setRecipientPhone(invoice.getRecipientPhone());
        dto.setRecipientEmail(invoice.getRecipientEmail());
        dto.setIsRecipientTaxable(invoice.getIsRecipientTaxable());
        dto.setPaymentMethod(invoice.getPaymentMethod());
        dto.setPaymentReference(invoice.getPaymentReference());
        dto.setPaymentDate(invoice.getPaymentDate());
        dto.setNotes(invoice.getNotes());
        dto.setAdditionalTaxes(safeList(invoice.getAdditionalTaxes()).stream()
                .map(SfecInvoiceConverter::toAdditionalTaxDto)
                .collect(Collectors.toList()));
        dto.setItems(safeList(invoice.getItems()).stream()
                .map(SfecInvoiceConverter::toItemDto)
                .collect(Collectors.toList()));
        dto.setStatus(invoice.getStatus() != null ? invoice.getStatus().name() : null);
        if (invoice.getCertification() != null) {
            SfecCertification certification = invoice.getCertification();
            dto.setCertificationNumber(certification.getCertificationNumber());
            dto.setSignature(certification.getSignature());
            dto.setShortSignature(certification.getShortSignature());
            dto.setQrCode(certification.getQrCode());
            dto.setCertificationDate(certification.getCertificationDate());
            dto.setInvoiceNumber(certification.getInvoiceNumber());
            dto.setIdentifier(certification.getIdentifier());
        }
        dto.setCreatedAt(invoice.getCreatedAt());
        return dto;
    }

    public static void applyCertificationResponse(SfecInvoice invoice, SfecCertificationResponseDto apiResponse) {
        if (apiResponse == null) {
            return;
        }

        SfecCertification certification = invoice.getCertification();
        if (certification == null) {
            certification = new SfecCertification();
            certification.setInvoice(invoice);
            invoice.setCertification(certification);
        }

        certification.setInvoiceId(apiResponse.getInvoiceId() != null
                ? apiResponse.getInvoiceId()
                : invoice.getExternalInvoiceId());
        certification.setCertificationNumber(apiResponse.getCertificationNumber());
        certification.setSignature(apiResponse.getSignature());
        certification.setShortSignature(apiResponse.getShortSignature());
        certification.setQrCode(apiResponse.getQrCode());
        certification.setCertificationDate(apiResponse.getCertificationDate());
        certification.setInvoiceNumber(apiResponse.getInvoiceNumber());
        certification.setIdentifier(apiResponse.getIdentifier());
        invoice.setStatus(SfecInvoiceStatus.CERTIFIED);
    }

    public static SfecCertificationResponseDto toCertificationResponseDto(SfecCertification certification) {
        if (certification == null) {
            return null;
        }
        SfecCertificationResponseDto dto = new SfecCertificationResponseDto();
        dto.setInvoiceId(certification.getInvoiceId());
        dto.setCertificationNumber(certification.getCertificationNumber());
        dto.setSignature(certification.getSignature());
        dto.setShortSignature(certification.getShortSignature());
        dto.setInvoiceNumber(certification.getInvoiceNumber());
        dto.setCertificationDate(certification.getCertificationDate());
        dto.setIdentifier(certification.getIdentifier());
        dto.setQrCode(certification.getQrCode());
        return dto;
    }

    private static SfecAdditionalTax toAdditionalTaxEntity(SfecAdditionalTaxDto dto) {
        SfecAdditionalTax tax = new SfecAdditionalTax();
        tax.setTaxCode(dto.getTaxCode());
        tax.setTaxLabel(dto.getTaxLabel());
        tax.setTaxAmount(dto.getTaxAmount());
        tax.setTaxRate(dto.getTaxRate());
        return tax;
    }

    private static SfecInvoiceItem toItemEntity(SfecInvoiceItemDto dto) {
        SfecInvoiceItem item = new SfecInvoiceItem();
        item.setDesignation(dto.getDesignation());
        item.setClassificationCode(dto.getClassificationCode());
        item.setDiscountAmount(dto.getDiscountAmount());
        item.setDiscountType(dto.getDiscountType());
        item.setNetAmount(dto.getNetAmount());
        item.setQuantity(dto.getQuantity());
        item.setSubtotal(dto.getSubtotal());
        item.setTaxAmount(dto.getTaxAmount());
        item.setTaxRate(dto.getTaxRate());
        item.setTotalAmount(dto.getTotalAmount());
        item.setType(dto.getType());
        item.setUnitPrice(dto.getUnitPrice());
        return item;
    }

    private static SfecAdditionalTaxDto toAdditionalTaxDto(SfecAdditionalTax tax) {
        SfecAdditionalTaxDto dto = new SfecAdditionalTaxDto();
        dto.setTaxCode(tax.getTaxCode());
        dto.setTaxLabel(tax.getTaxLabel());
        dto.setTaxAmount(tax.getTaxAmount());
        dto.setTaxRate(tax.getTaxRate());
        return dto;
    }

    private static SfecInvoiceItemDto toItemDto(SfecInvoiceItem item) {
        SfecInvoiceItemDto dto = new SfecInvoiceItemDto();
        dto.setDesignation(item.getDesignation());
        dto.setClassificationCode(item.getClassificationCode());
        dto.setDiscountAmount(item.getDiscountAmount());
        dto.setDiscountType(item.getDiscountType());
        dto.setNetAmount(item.getNetAmount());
        dto.setQuantity(item.getQuantity());
        dto.setSubtotal(item.getSubtotal());
        dto.setTaxAmount(item.getTaxAmount());
        dto.setTaxRate(item.getTaxRate());
        dto.setTotalAmount(item.getTotalAmount());
        dto.setType(item.getType());
        dto.setUnitPrice(item.getUnitPrice());
        return dto;
    }

    private static <T> List<T> safeList(List<T> list) {
        return list != null ? list : Collections.emptyList();
    }
}
