package com.golovanov.currencyratecbrf.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CurrencyValue {
    private String charCode;
    private String value;
}