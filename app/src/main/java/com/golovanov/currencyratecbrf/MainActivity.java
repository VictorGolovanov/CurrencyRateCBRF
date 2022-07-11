package com.golovanov.currencyratecbrf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.golovanov.currencyratecbrf.entity.Currency;
import com.golovanov.currencyratecbrf.service.CurrencyRateService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private final static CurrencyRateService service = CurrencyRateService.getInstance();
    private static final SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        service.sendRequest(currentDate.format(new Date()));
    }

    public void getRate(View view) {
        EditText editText = findViewById(R.id.editText);
        String value = editText.getText().toString();
        if (isNumber(value)) {
            Currency originalCurrency = Currency.USD;
            Currency targetCurrency = Currency.RUB;

            double ratio = service.getCurrencyRatio(originalCurrency, targetCurrency);

            double doubleValue = Double.parseDouble(value);
            double result = ratio * doubleValue;
            System.out.println(result);
        }
    }

    private static boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}