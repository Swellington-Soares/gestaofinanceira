package dev.suel.mstransactionapi.infra.configuration.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalSerializer extends com.fasterxml.jackson.databind.JsonSerializer<java.math.BigDecimal> {
    @Override
    public void serialize(
            BigDecimal value,
            JsonGenerator gen,
            SerializerProvider serializers
    ) throws IOException {

        if (value == null) {
            gen.writeNull();
            return;
        }

        gen.writeNumber(
                value.setScale(2, RoundingMode.HALF_UP)
        );
    }
}
