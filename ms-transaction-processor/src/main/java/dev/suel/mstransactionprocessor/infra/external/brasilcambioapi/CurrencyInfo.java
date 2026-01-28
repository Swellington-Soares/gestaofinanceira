package dev.suel.mstransactionprocessor.infra.external.brasilcambioapi;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrencyInfo(
        @JsonProperty("simbolo")
        String symbol,

        @JsonProperty("nome")
        String name,

        @JsonProperty("tipo_moeda")
        String type) {
    @Override
    public String toString() {
        return symbol.toUpperCase() + " -> " + name;
    }

}
