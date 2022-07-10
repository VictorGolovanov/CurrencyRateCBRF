package com.golovanov.currencyratecbrf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.golovanov.currencyratecbrf.service.CurrencyRateService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}