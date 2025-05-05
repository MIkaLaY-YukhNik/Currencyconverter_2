package com.example.currencyconverter.controller;

import com.example.currencyconverter.entity.ConversionHistory;
import com.example.currencyconverter.model.CurrencyResponse;
import com.example.currencyconverter.repository.ConversionHistoryRepository;
import com.example.currencyconverter.service.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CurrencyController {

    private final CurrencyService currencyService;
    private final ConversionHistoryRepository conversionHistoryRepository;

    public CurrencyController(CurrencyService currencyService, ConversionHistoryRepository conversionHistoryRepository) {
        this.currencyService = currencyService;
        this.conversionHistoryRepository = conversionHistoryRepository;
    }

    @GetMapping("/convert")
    public ResponseEntity<Map<String, Object>> convert(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam double amount) {

        try {
            // Получаем курсы валют
            CurrencyResponse rates = currencyService.fetchExchangeRates();

            // Вычисляем конвертированную сумму
            double convertedAmount = currencyService.convertAmount(from, to, amount, rates);

            // Сохранение истории конвертации
            ConversionHistory conversionHistory = new ConversionHistory(
                    from.toUpperCase(),
                    to.toUpperCase(),
                    amount,
                    convertedAmount
            );
            conversionHistoryRepository.save(conversionHistory);

            // Формируем ответ
            Map<String, Object> result = new HashMap<>();
            result.put("fromCurrency", from.toUpperCase());
            result.put("toCurrency", to.toUpperCase());
            result.put("convertedAmount", convertedAmount);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", "Unable to process conversion: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResult);
        }
    }
}