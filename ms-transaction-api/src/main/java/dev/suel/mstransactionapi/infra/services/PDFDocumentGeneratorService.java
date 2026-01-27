package dev.suel.mstransactionapi.infra.services;

import dev.suel.mstransactionapi.application.gateway.DocumentGeneratorPort;
import dev.suel.mstransactionapi.domain.DocumentInfo;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PDFDocumentGeneratorService implements DocumentGeneratorPort {
    @Override
    public DocumentInfo generateDocument(Map<String, Object> data) {
        return null;
    }
}
