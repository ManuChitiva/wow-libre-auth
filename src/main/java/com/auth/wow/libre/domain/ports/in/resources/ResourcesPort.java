package com.auth.wow.libre.domain.ports.in.resources;

import com.auth.wow.libre.domain.model.CountryModel;
import com.auth.wow.libre.domain.model.FaqsModel;

import java.util.List;

public interface ResourcesPort {
  List<CountryModel> getCountry(String transactionId);
  List<FaqsModel> getFaqs(String transactionId);

}
