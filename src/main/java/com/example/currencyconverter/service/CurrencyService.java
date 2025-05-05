package com.example.currencyconverter.service;

import com.example.currencyconverter.entity.CurrencyRate;
import com.example.currencyconverter.model.CurrencyResponse;
import com.example.currencyconverter.repository.CurrencyRateRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CurrencyService {

    private final String API_KEY = "dd23561225204beca0c5fe1cf9bd0d16";
    private final String API_URL = "https://openexchangerates.org/api/latest.json?app_id=" + API_KEY;
    private final RestTemplate restTemplate;
    private final CurrencyRateRepository currencyRateRepository;

    public CurrencyService(RestTemplate restTemplate, CurrencyRateRepository currencyRateRepository) {
        this.restTemplate = restTemplate;
        this.currencyRateRepository = currencyRateRepository;
    }

    public CurrencyResponse fetchExchangeRates() {
        CurrencyResponse response = restTemplate.getForObject(API_URL, CurrencyResponse.class);
        if (response == null || response.getRates() == null) {
            throw new RuntimeException("Unable to fetch exchange rates");
        }

        Map<String, Double> rates = response.getRates();
        for (Map.Entry<String, Double> entry : rates.entrySet()) {
            String currency = entry.getKey();
            Double rate = entry.getValue();
            CurrencyRate currencyRate = new CurrencyRate(currency, rate);
            currencyRateRepository.save(currencyRate);
        }

        return response;
    }

    public double convertAmount(String from, String to, double amount, CurrencyResponse rates) {
        Map<String, Double> rateMap = rates.getRates();
        double fromRate = rateMap.getOrDefault(from.toUpperCase(), 1.0);
        double toRate = rateMap.getOrDefault(to.toUpperCase(), 1.0);
        return (amount / fromRate) * toRate;
    }
}