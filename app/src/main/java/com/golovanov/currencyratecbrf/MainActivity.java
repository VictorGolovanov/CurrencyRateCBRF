package com.golovanov.currencyratecbrf;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.golovanov.currencyratecbrf.entity.Currency;
import com.golovanov.currencyratecbrf.service.CurrencyRateService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static CurrencyRateService service = CurrencyRateService.getInstance();
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        service.sendRequest(currentDate.format(new Date()));
        List<String> names = new ArrayList<>();
        for (Currency value : Currency.values()) {
            names.add(value.getName());
        }

        ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, names/*Currency.values()*/) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return setCentered(super.getView(position, convertView, parent));
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                return setCentered(super.getDropDownView(position, convertView, parent));
            }

            private View setCentered(View view) {
                TextView textView = (TextView)view.findViewById(android.R.id.text1);
                textView.setGravity(Gravity.CENTER);
                return view;
            }
        };
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner originalCurrency = findViewById(R.id.originalCurrency);
        originalCurrency.setAdapter(currencyAdapter);

        Spinner targetCurrency = findViewById(R.id.targetCurrency);
        targetCurrency.setAdapter(currencyAdapter);
    }

    public void getRate(View view) {
        EditText editText = findViewById(R.id.editText);
        String value = editText.getText().toString();
        if (isNumber(value)) {
            Spinner original = findViewById(R.id.originalCurrency);
            Spinner target = findViewById(R.id.targetCurrency);
            Currency originalCurrency = Currency.valueOf(original.getSelectedItem().toString().substring(0, 3));
            Currency targetCurrency = Currency.valueOf(target.getSelectedItem().toString().substring(0, 3));
            TextView textView = findViewById(R.id.result);

            double ratio = service.getCurrencyRatio(originalCurrency, targetCurrency);

            double doubleValue = Double.parseDouble(value);
            double result = ratio * doubleValue;

            @SuppressLint("DefaultLocale")
            String totalResult = String.format("%4.2f %s is %4.2f %s", doubleValue, originalCurrency, result, targetCurrency);
            textView.setText(totalResult);
            System.out.println(totalResult);
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

    private static boolean hasConnection() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }
}