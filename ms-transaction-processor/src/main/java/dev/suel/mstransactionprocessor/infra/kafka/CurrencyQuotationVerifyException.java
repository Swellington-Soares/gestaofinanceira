package dev.suel.mstransactionprocessor.infra.kafka;

public class CurrencyQuotationVerifyException extends RuntimeException {
    public CurrencyQuotationVerifyException(String message) {
        super(message);
    }
}
