package com.auth.wow.libre.application.services.resources;

import com.auth.wow.libre.domain.model.CountryModel;
import com.auth.wow.libre.domain.model.FaqsModel;
import com.auth.wow.libre.domain.ports.in.resources.ResourcesPort;
import com.auth.wow.libre.domain.ports.out.resources.JsonLoaderPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourcesService implements ResourcesPort {
  private final JsonLoaderPort jsonLoaderPort;

  public ResourcesService(JsonLoaderPort jsonLoaderPort) {
    this.jsonLoaderPort = jsonLoaderPort;
  }

  @Override
  public List<CountryModel> getCountry(String transactionId) {
    return jsonLoaderPort.getJsonCountry(transactionId);
  }

  @Override
  public List<FaqsModel> getFaqs(String transactionId) {
    return jsonLoaderPort.getJsonFaqs(transactionId);
  }

}
