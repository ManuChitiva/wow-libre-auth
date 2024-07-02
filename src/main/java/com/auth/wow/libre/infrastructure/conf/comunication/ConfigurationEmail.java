package com.auth.wow.libre.infrastructure.conf.comunication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@Configuration
public class ConfigurationEmail {

  @Primary
  @Bean
  public FreeMarkerConfigurationFactoryBean factoryBean() {
    FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
    bean.setTemplateLoaderPath("classpath:/static/templates");
    return bean;
  }
}
