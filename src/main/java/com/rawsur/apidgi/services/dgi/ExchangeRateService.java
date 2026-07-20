package com.rawsur.apidgi.services.dgi;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rawsur.apidgi.converters.ExchangeRateConverter;
import com.rawsur.apidgi.dto.dgi.exchangeRate.CreateExchangeRateDto;
import com.rawsur.apidgi.dto.dgi.exchangeRate.EditExchangeRateDto;
import com.rawsur.apidgi.models.dgi.ExchangeRate;
import com.rawsur.apidgi.repositories.dgi.ExchangeRateRepo;

@Service
public class ExchangeRateService {
    @Autowired
    private ExchangeRateRepo exchangeRateRepo;

    @Transactional
    public ExchangeRate create(CreateExchangeRateDto createExchangeRateDto) {
        String source = normalizeSource(createExchangeRateDto.getSource());
        if (exchangeRateRepo.existsByEffectiveDateAndSource(createExchangeRateDto.getEffectiveDate(), source)) {
            throw new RuntimeException("Un taux de change existe deja pour cette date et cette source");
        }

        ExchangeRate exchangeRate = ExchangeRateConverter.toExchangeRate(createExchangeRateDto);
        return exchangeRateRepo.save(exchangeRate);
    }

    @Transactional
    public List<ExchangeRate> findAll() {
        return exchangeRateRepo.findAll();
    }

    @Transactional
    public List<ExchangeRate> findActive() {
        return exchangeRateRepo.findByActiveTrueOrderByEffectiveDateDesc();
    }

    @Transactional
    public ExchangeRate findCurrentByDate(LocalDateTime dateTime) {
        return exchangeRateRepo
                .findTopByActiveTrueAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(dateTime)
                .orElseThrow(() -> new RuntimeException("Aucun taux de change actif trouve pour cette date"));
    }

    @Transactional
    public ExchangeRate findById(UUID id) {
        return exchangeRateRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Taux de change non trouve avec l'ID: " + id));
    }

    @Transactional
    public ExchangeRate update(EditExchangeRateDto editExchangeRateDto) {
        ExchangeRate existingExchangeRate = exchangeRateRepo.findById(editExchangeRateDto.getId())
                .orElseThrow(
                        () -> new RuntimeException("Taux de change non trouve avec l'ID: " + editExchangeRateDto.getId()));

        String source = normalizeSource(editExchangeRateDto.getSource());
        if (exchangeRateRepo.existsByEffectiveDateAndSourceAndIdNot(
                editExchangeRateDto.getEffectiveDate(),
                source,
                editExchangeRateDto.getId())) {
            throw new RuntimeException("Un autre taux de change existe deja pour cette date et cette source");
        }

        ExchangeRate exchangeRate = ExchangeRateConverter.toExchangeRate(editExchangeRateDto);
        exchangeRate.setCreatedAt(existingExchangeRate.getCreatedAt());
        return exchangeRateRepo.save(exchangeRate);
    }

    @Transactional
    public void delete(UUID id) {
        if (!exchangeRateRepo.existsById(id)) {
            throw new RuntimeException("Taux de change non trouve avec l'ID: " + id);
        }
        exchangeRateRepo.deleteById(id);
    }

    private String normalizeSource(String source) {
        if (source == null || source.trim().isEmpty()) {
            return "DGI";
        }
        return source.trim();
    }
}
