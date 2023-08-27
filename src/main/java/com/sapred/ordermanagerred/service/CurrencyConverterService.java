package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.dto.DailyGateKey;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
public class CurrencyConverterService {

    @Value("${exchangeRateApi}")
    private String exchangeRateApi;
    
    @Autowired
    private RedisService redisService;

    @SneakyThrows
    public double convertCurrency(String fromCurrency, String toCurrency) {
        String rate;
        DailyGateKey dailyGateKey = new DailyGateKey(fromCurrency, toCurrency, LocalDate.now());
        String key = dailyGateKey.toString();
        if (fromCurrency.equals(toCurrency))
            return 1;
        if (redisService.isKeyExists(key))
            rate = redisService.getValue(key);
        else {
            String url = String.format(exchangeRateApi, fromCurrency, toCurrency);
            WebClient.Builder builder = WebClient.builder();
            rate = builder.build()
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            redisService.setValue(key, rate);
        }
        return Double.parseDouble(rate);
    }

}
