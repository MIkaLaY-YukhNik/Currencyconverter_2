package com.example.currencyconverter.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CurrencyRate {

    @Id
    private String currencyCode;

    private double rate;

    // Геттеры, сеттеры и конструкторы
    public CurrencyRate() {}

    public CurrencyRate(String currencyCode, double rate) {
        this.currencyCode = currencyCode;
        this.rate = rate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}