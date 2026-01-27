package dev.suel.mstransactionapi.application.gateway;

import dev.suel.mstransactionapi.domain.DocumentInfo;

import java.util.Map;

public interface DocumentGeneratorPort {
   DocumentInfo generateDocument(Map<String,Object> data);
}
