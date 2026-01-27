package dev.suel.mstransactionapi.domain;

import java.util.Arrays;
import java.util.List;

public enum CurrencyType {
    AUD("Dólar australiano"),
    CAD("Dólar canadense"),
    CHF("Franco suíço"),
    DKK("Coroa dinamarquesa"),
    EUR("Euro"),
    GBP("Libra Esterlina"),
    JPY("Iene"),
    NOK("Coroa norueguesa"),
    SEK("Coroa sueca"),
    USD("Dólar dos Estados Unidos"),
    BRL("Real Brasileiro");

    private final String fullName;

    CurrencyType(String fullName) {
        this.fullName = fullName;
    }

    public static CurrencyType fromCode(String code) {
        return CurrencyType.valueOf(code.toUpperCase());
    }

    public static List<CurrencyType> toList() {
        return Arrays.asList(values());
    }

    public String getFullName() {
        return fullName;
    }

    public String getCode() {
        return this.name();
    }

    public static CurrencyType Default() {
        return BRL;
    }
}
