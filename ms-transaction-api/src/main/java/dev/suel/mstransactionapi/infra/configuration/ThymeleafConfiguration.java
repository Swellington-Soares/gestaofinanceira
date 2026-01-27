package dev.suel.mstransactionapi.infra.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class ThymeleafConfiguration {

    @Bean
    public ClassLoaderTemplateResolver pdfTemplateResolver() {
        ClassLoaderTemplateResolver pdfTemplateResource = new ClassLoaderTemplateResolver();
        pdfTemplateResource.setPrefix("templates/pdf-templates/");
        pdfTemplateResource.setSuffix(".html");
        pdfTemplateResource.setTemplateMode("HTML5");
        pdfTemplateResource.setCharacterEncoding("UTF-8");
        pdfTemplateResource.setCacheable(false);
        pdfTemplateResource.setOrder(1);
        return pdfTemplateResource;
    }
}
