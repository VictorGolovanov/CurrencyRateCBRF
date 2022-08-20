package com.golovanov.currencyratecbrf.service;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.golovanov.currencyratecbrf.entity.Currency;
import com.golovanov.currencyratecbrf.entity.CurrencyValue;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import lombok.SneakyThrows;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CbRFCurrencyConversionService implements CurrencyRateService {

    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
    private static Map<String, List<CurrencyValue>> currentCurrencyValues;

    private static final String URL = "http://www.cbr.ru/scripts/XML_daily.asp?date_req=";
    private static final String GET = "GET";
    private static final String VALUTE = "Valute";
    private static final String CHAR_CODE = "CharCode";
    private static final String VALUE = "Value";
    private static final String NOMINAL = "Nominal";
    private static final String ONE = "1";

    @Override
    public double getCurrencyRatio(Currency original, Currency target) {
        String date = currentDate.format(new Date());
        if (currentCurrencyValues != null) {
            return getRateViaRu(original, target, date);
        } else {
            return 0;
        }
    }

    private double getRateViaRu(Currency original, Currency target, String date) {
        try {
            if (currentCurrencyValues.containsKey(date)) {
                List<CurrencyValue> values = currentCurrencyValues.get(date);
                if (values != null) {
                    Optional<CurrencyValue> originalValue = values.stream()
                            .filter(v -> v.getCharCode().equals(original.toString())).findFirst();
                    Optional<CurrencyValue> targetValue = values.stream()
                            .filter(v -> v.getCharCode().equals(target.toString())).findFirst();
                    if (originalValue.isPresent() && targetValue.isPresent()) {
                        return Double.parseDouble(originalValue.get().getValue())
                                / Double.parseDouble(targetValue.get().getValue());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @SneakyThrows
    public void sendRequest(String date) {
        URL url = new URL(URL + date);

        Request request = new Request.Builder().url(url).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @SneakyThrows
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                currentCurrencyValues = new HashMap<>();
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(url.openStream());

                List<CurrencyValue> values = new ArrayList<>();

                NodeList nodeList = doc.getElementsByTagName(VALUTE);
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String charCode = element.getElementsByTagName(CHAR_CODE).item(0).getFirstChild().getNodeValue();
                        String nominal = element.getElementsByTagName(NOMINAL).item(0).getFirstChild().getNodeValue();
                        String value = element.getElementsByTagName(VALUE).item(0).getFirstChild().getNodeValue().replace(",", ".");
                        String valueForOne = String.valueOf((Double.parseDouble(value) / Double.parseDouble(nominal)));
                        values.add(new CurrencyValue(charCode, valueForOne));
                    }
                }
                values.add(new CurrencyValue(Currency.RUB.toString(), ONE));
                currentCurrencyValues.put(date, values);
            }
        });
    }
}
