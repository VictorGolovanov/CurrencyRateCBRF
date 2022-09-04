package com.golovanov.currencyratecbrf.entity;

import com.golovanov.currencyratecbrf.MainActivity;
import com.golovanov.currencyratecbrf.R;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Currency {

    RUB(R.string.ruble_name),
    USD(R.string.usd_name),
    EUR(R.string.eur_name),
    CNY(R.string.cny_name),
    GBP(R.string.gbp_name),
    AUD(R.string.aud_name),
    AZN(R.string.azn_name),
    AMD(R.string.amd_name),
    BYN(R.string.byn_name),
    BGN(R.string.bgn_name),
    BRL(R.string.brl_name),
    HUF(R.string.huf_name),
    HKD(R.string.hkd_name),
    DKK(R.string.dkk_name),
    INR(R.string.inr_name),
    KZT(R.string.kzt_name),
    CAD(R.string.cad_name),
    KGS(R.string.kgs_name),
    MDL(R.string.mdl),
    NOK(R.string.ndk_name),
    PLN(R.string.pln_name),
    RON(R.string.ron_name),
    SGD(R.string.sgd_name),
    TJS(R.string.tjs_name),
    TRY(R.string.try_name),
    TMT(R.string.tmt_name),
    UZS(R.string.uzs_name),
    UAH(R.string.uah_name),
    CZK(R.string.czk_name),
    SEK(R.string.sek_name),
    CHF(R.string.chf_name),
    ZAR(R.string.zar_name),
    KRW(R.string.krw_name),
    JPY(R.string.jpy_name),
    XDR(R.string.xdr_name);

    String name;

    Currency(int resourceId) {
        this.name = Currency.this + " - " + MainActivity.getContext().getString(resourceId);
    }

}