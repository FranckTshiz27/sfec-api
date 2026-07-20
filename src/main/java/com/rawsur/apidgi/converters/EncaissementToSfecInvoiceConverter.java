package com.rawsur.apidgi.converters;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Collections;

import com.rawsur.apidgi.dto.dgi.sfec.CreateSfecInvoiceDto;
import com.rawsur.apidgi.dto.dgi.sfec.SfecInvoiceItemDto;
import com.rawsur.apidgi.models.rawsur.RawsurInvoicePayment;

public final class EncaissementToSfecInvoiceConverter {

    private EncaissementToSfecInvoiceConverter() {
    }

    public static CreateSfecInvoiceDto toCreateSfecInvoiceDto(RawsurInvoicePayment encaissement) {
        CreateSfecInvoiceDto dto = new CreateSfecInvoiceDto();
        dto.setInvoiceId(encaissement.getCle());
        dto.setTaxpayerNiu(encaissement.getNif());
        dto.setInvoiceType("salesInvoice");
        dto.setInvoiceSubject(encaissement.getNameArticle());
        dto.setInvoiceDueDate(Instant.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS));

        BigDecimal subtotal = resolveSubtotal(encaissement);
        BigDecimal taxAmount = resolveTaxAmount(encaissement, subtotal);
        BigDecimal totalAmount = subtotal.add(taxAmount);

        dto.setSubtotal(subtotal);
        dto.setTotalTaxTAmount(taxAmount);
        dto.setTotalTaxRAmount(BigDecimal.ZERO);
        dto.setTotalExemptAmount(BigDecimal.ZERO);
        dto.setTotalTaxAmount(taxAmount);
        dto.setDiscountAmount(BigDecimal.ZERO);
        dto.setAmountDue(totalAmount);
        dto.setTotalLineDiscountAmount(BigDecimal.ZERO);
        dto.setAdditionalCentTax(BigDecimal.ZERO);
        dto.setElectronicStampDuty(BigDecimal.ZERO);
        dto.setTotalAmount(totalAmount);
        dto.setCurrency(defaultIfBlank(encaissement.getCurrencyCode(), "XAF"));

        dto.setRecipientType(resolveRecipientType(encaissement.getGenRassu()));
        dto.setRecipientName(encaissement.getNom());
        dto.setRecipientNiu(encaissement.getNif());
        dto.setRecipientAddress(encaissement.getAdresse());
        dto.setRecipientPhone(encaissement.getContact());
        dto.setIsRecipientTaxable(true);

        dto.setPaymentMethod(resolvePaymentMethod(encaissement.getTypePaiement()));
        dto.setPaymentDate(encaissement.getDateEncaissement() != null
                ? encaissement.getDateEncaissement().toInstant(java.time.ZoneOffset.UTC)
                        .truncatedTo(java.time.temporal.ChronoUnit.SECONDS)
                : Instant.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS));

        dto.setItems(Collections.singletonList(buildItem(encaissement, subtotal, taxAmount, totalAmount)));
        return dto;
    }

    private static SfecInvoiceItemDto buildItem(RawsurInvoicePayment encaissement, BigDecimal subtotal,
            BigDecimal taxAmount, BigDecimal totalAmount) {
        SfecInvoiceItemDto item = new SfecInvoiceItemDto();
        item.setDesignation(defaultIfBlank(encaissement.getNameArticle(), "Prestation assurance"));
        item.setClassificationCode("C01");
        item.setDiscountAmount(BigDecimal.ZERO);
        item.setDiscountType("fixed");
        item.setNetAmount(subtotal);
        item.setQuantity(BigDecimal.valueOf(encaissement.getQuantity() != null ? encaissement.getQuantity() : 1));
        item.setSubtotal(subtotal);
        item.setTaxAmount(taxAmount);
        item.setTaxRate(resolveTaxRate(encaissement, subtotal, taxAmount));
        item.setTotalAmount(totalAmount);
        item.setType("service");
        item.setUnitPrice(subtotal.divide(item.getQuantity(), 2, RoundingMode.HALF_UP));
        return item;
    }

    private static BigDecimal resolveSubtotal(RawsurInvoicePayment encaissement) {
        if (encaissement.getPrice() != null) {
            return encaissement.getPrice().abs().setScale(2, RoundingMode.HALF_UP);
        }
        if (encaissement.getAmount() != null) {
            return encaissement.getAmount().abs().setScale(2, RoundingMode.HALF_UP);
        }
        if (encaissement.getPrime() != null) {
            return BigDecimal.valueOf(encaissement.getPrime()).abs().setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal resolveTaxAmount(RawsurInvoicePayment encaissement, BigDecimal subtotal) {
        if (encaissement.getTva() != null && encaissement.getTva() > 0) {
            return BigDecimal.valueOf(encaissement.getTva()).setScale(2, RoundingMode.HALF_UP);
        }
        if ("B".equalsIgnoreCase(encaissement.getTaxeGroup())) {
            return subtotal.multiply(BigDecimal.valueOf(0.18)).setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    private static String resolveTaxRate(RawsurInvoicePayment encaissement, BigDecimal subtotal, BigDecimal taxAmount) {
        if (taxAmount.compareTo(BigDecimal.ZERO) == 0 || subtotal.compareTo(BigDecimal.ZERO) == 0) {
            return "0";
        }
        return "18";
    }

    private static String resolveRecipientType(String genRassu) {
        if ("PP".equalsIgnoreCase(genRassu)) {
            return "individual";
        }
        return "business";
    }

    private static String resolvePaymentMethod(String typePaiement) {
        if (typePaiement == null) {
            return "cash";
        }
        String normalized = typePaiement.trim().toUpperCase();
        if (normalized.contains("VIR") || normalized.contains("BANK")) {
            return "bank_transfer";
        }
        if (normalized.contains("CB") || normalized.contains("CARD")) {
            return "card";
        }
        if (normalized.contains("MOB")) {
            return "mobile_money";
        }
        if (normalized.contains("CHQ") || normalized.contains("CHEQ")) {
            return "cheque";
        }
        return "cash";
    }

    private static String defaultIfBlank(String value, String defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return value.trim();
    }
}
