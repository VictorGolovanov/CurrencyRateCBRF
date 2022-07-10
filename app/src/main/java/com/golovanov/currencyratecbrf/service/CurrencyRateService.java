package com.golovanov.currencyratecbrf.service;

import com.golovanov.currencyratecbrf.entity.Currency;

public interface CurrencyRateService {

    double getCurrencyRatio(Currency original, Currency target);

    static CurrencyRateService getInstance() {
        return new CbRFCurrencyConversionService();
    }
}
