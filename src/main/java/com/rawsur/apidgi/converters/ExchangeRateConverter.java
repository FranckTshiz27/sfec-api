package com.rawsur.apidgi.converters;

import com.rawsur.apidgi.dto.dgi.exchangeRate.CreateExchangeRateDto;
import com.rawsur.apidgi.dto.dgi.exchangeRate.EditExchangeRateDto;
import com.rawsur.apidgi.models.dgi.ExchangeRate;

public class ExchangeRateConverter {

    public static ExchangeRate toExchangeRate(CreateExchangeRateDto createExchangeRateDto) {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setRate(createExchangeRateDto.getRate());
        exchangeRate.setEffectiveDate(createExchangeRateDto.getEffectiveDate());
        exchangeRate.setSource(normalizeSource(createExchangeRateDto.getSource()));
        exchangeRate.setActive(createExchangeRateDto.getActive());
        return exchangeRate;
    }

    public static ExchangeRate toExchangeRate(EditExchangeRateDto editExchangeRateDto) {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setId(editExchangeRateDto.getId());
        exchangeRate.setRate(editExchangeRateDto.getRate());
        exchangeRate.setEffectiveDate(editExchangeRateDto.getEffectiveDate());
        exchangeRate.setSource(normalizeSource(editExchangeRateDto.getSource()));
        exchangeRate.setActive(editExchangeRateDto.getActive());
        return exchangeRate;
    }

    private static String normalizeSource(String source) {
        if (source == null || source.trim().isEmpty()) {
            return "DGI";
        }
        return source.trim();
    }
}
