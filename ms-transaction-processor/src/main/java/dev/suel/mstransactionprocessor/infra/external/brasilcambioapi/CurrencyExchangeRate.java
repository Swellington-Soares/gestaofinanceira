package dev.suel.mstransactionprocessor.infra.external.brasilcambioapi;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrencyExchangeRate(
        @JsonProperty("paridade_compra")
        int purchaseParity,
        @JsonProperty("paridade_venda")
        int sellParity,
        @JsonProperty("cotacao_compra")
        float purchaseQuote,
        @JsonProperty("cotacao_venda")
        float sellQuote,
        @JsonProperty("data_hora_cotacao")
        String updateDate,
        @JsonProperty("tipo_boletim")
        String type
) {
}
