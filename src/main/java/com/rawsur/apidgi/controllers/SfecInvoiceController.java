package com.rawsur.apidgi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rawsur.apidgi.dto.PeriodeDTO;
import com.rawsur.apidgi.dto.dgi.sfec.SfecInvoiceResponseDto;
import com.rawsur.apidgi.models.rawsur.RawsurInvoicePayment;
import com.rawsur.apidgi.routes.Routes;
import com.rawsur.apidgi.services.dgi.SfecInvoiceService;
import com.rawsur.apidgi.services.rawsur.RawsurInvoicePaymentService;

@RestController
@RequestMapping(Routes.RAWSUR_INVOICES_TYPE_BASE_URI)
public class SfecInvoiceController {

    @Autowired
    private RawsurInvoicePaymentService rawsurInvoiceService;

    @Autowired
    private SfecInvoiceService sfecInvoiceService;

    @PostMapping
    public ResponseEntity<List<RawsurInvoicePayment>> getInvoices(@RequestBody PeriodeDTO periodeDTO) {
        List<RawsurInvoicePayment> invoices = this.rawsurInvoiceService.getInvoices(periodeDTO);
        return ResponseEntity.ok().body(invoices);
    }

    @PostMapping("/search")
    public ResponseEntity<List<RawsurInvoicePayment>> getInvoicesByReference(@RequestBody String reference) {
        try {
            List<RawsurInvoicePayment> invoices = this.rawsurInvoiceService.getInvoicesByReference(reference);
            return ResponseEntity.ok().body(invoices);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(Routes.CREATE_DGI_INVOICE)
    public ResponseEntity<SfecInvoiceResponseDto> createInvoiceFromEncaissement(
            @RequestBody RawsurInvoicePayment rawsurInvoicePayment) {
        SfecInvoiceResponseDto dto = this.sfecInvoiceService.createFromEncaissement(rawsurInvoicePayment);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping(Routes.GET_DGI_INVOICE)
    public ResponseEntity<List<SfecInvoiceResponseDto>> getInvoicesByPeriod(@RequestBody PeriodeDTO periodeDTO) {
        List<SfecInvoiceResponseDto> invoices = this.sfecInvoiceService.findByPeriod(periodeDTO);
        return ResponseEntity.ok().body(invoices);
    }
}
