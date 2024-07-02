package com.auth.wow.libre.infrastructure.repositories.json_loader;

import com.auth.wow.libre.domain.model.CountryModel;
import com.auth.wow.libre.domain.model.FaqsModel;
import com.auth.wow.libre.domain.ports.out.resources.JsonLoaderPort;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class JsonLoader implements JsonLoaderPort {
    private final ObjectMapper objectMapper;

    private final Resource jsonFile;
    private final Resource faqsJsonFile;

    private List<CountryModel> jsonCountryModel;
    private List<FaqsModel> jsonFaqsModel;


    public JsonLoader(ObjectMapper objectMapper,
                      @Value("classpath:/static/countryAvailable.json") Resource jsonFile,
                      @Value("classpath:/static/faqs.json") Resource faqsJsonFile) {
        this.objectMapper = objectMapper;
        this.jsonFile = jsonFile;
        this.faqsJsonFile = faqsJsonFile;
    }

    @PostConstruct
    public void loadJsonFile() {
        try {
            jsonCountryModel = objectMapper.readValue(jsonFile.getInputStream(), new TypeReference<>() {
            });
            jsonFaqsModel = objectMapper.readValue(faqsJsonFile.getInputStream(), new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CountryModel> getJsonCountry(String transactionId) {
        return jsonCountryModel;
    }

    @Override
    public List<FaqsModel> getJsonFaqs(String transactionId) {
        return jsonFaqsModel;
    }


}
