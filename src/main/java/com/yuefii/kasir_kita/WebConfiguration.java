package com.yuefii.kasir_kita;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.yuefii.kasir_kita.resolver.UserArgsResolver;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

  @Autowired
  private UserArgsResolver userArgsResolver;

  @Override
  @SuppressWarnings("null")
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    resolvers.add(userArgsResolver);
  }
}
