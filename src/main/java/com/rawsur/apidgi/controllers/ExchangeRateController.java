package com.rawsur.apidgi.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rawsur.apidgi.dto.dgi.exchangeRate.CreateExchangeRateDto;
import com.rawsur.apidgi.dto.dgi.exchangeRate.EditExchangeRateDto;
import com.rawsur.apidgi.models.dgi.ExchangeRate;
import com.rawsur.apidgi.routes.Routes;
import com.rawsur.apidgi.services.dgi.ExchangeRateService;

@RestController
@RequestMapping(Routes.EXCHANGE_RATE_BASE_URI)
public class ExchangeRateController {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @PostMapping
    public ResponseEntity<ExchangeRate> create(@RequestBody @Valid CreateExchangeRateDto createExchangeRateDto) {
        ExchangeRate created = exchangeRateService.create(createExchangeRateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<ExchangeRate>> getAll() {
        return ResponseEntity.ok(exchangeRateService.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<ExchangeRate>> getActive() {
        return ResponseEntity.ok(exchangeRateService.findActive());
    }

    @GetMapping("/active/by-date")
    public ResponseEntity<ExchangeRate> getActiveByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        return ResponseEntity.ok(exchangeRateService.findCurrentByDate(date));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExchangeRate> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(exchangeRateService.findById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ExchangeRate> update(@PathVariable UUID id,
            @RequestBody @Valid EditExchangeRateDto editExchangeRateDto) {
        if (!id.equals(editExchangeRateDto.getId())) {
            throw new RuntimeException("L'ID dans le path ne correspond pas a l'ID dans le body");
        }
        return ResponseEntity.ok(exchangeRateService.update(editExchangeRateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        exchangeRateService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
