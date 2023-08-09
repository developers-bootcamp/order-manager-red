package com.sapred.ordermanagerred.service;

import lombok.Data;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Service
public class CurrencyConverterService {

    private String EXCHANGE_RATE_API = "http://dbd132af-315b-45ea-a839-453d97370cf6.mock.pstmn.io/exchangeRate?companyCurrency=%s&orderCurrency=%s";

    @Autowired
    private RedisService redisService;

    @SneakyThrows
    public double convertCurrency(String fromCurrency, String toCurrency) {
        String rate;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf.format(new Date());
        String key = fromCurrency + toCurrency + date;
        if (fromCurrency.equals(toCurrency))
            return 1;
        if (redisService.isKeyExists(key))
            rate = redisService.getValue(key);
        else {
            String url = String.format(EXCHANGE_RATE_API, fromCurrency, toCurrency);
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            rate = response.body().string();
            redisService.setValue(key, rate);
        }
        return Double.parseDouble(rate);
    }

}
