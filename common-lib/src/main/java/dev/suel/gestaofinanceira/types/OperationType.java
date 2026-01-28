package dev.suel.gestaofinanceira.types;

public enum OperationType {
    WITHDRAW("SAQUE"),
    DEPOSIT("DEPÓSITO"),
    TRANSFER("TRANSFERÊNCIA"),
    PURCHASER("COMPRA");

    private final String title;

    OperationType(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
