package com.yuefii.kasir_kita.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import com.yuefii.kasir_kita.models.User;
import com.yuefii.kasir_kita.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class UserArgsResolver implements HandlerMethodArgumentResolver {

  @Autowired
  private UserRepository userRepository;

  @Override
  @SuppressWarnings("null")
  public boolean supportsParameter(MethodParameter parameter) {
    return User.class.equals(parameter.getParameterType());
  }

  @Override
  @SuppressWarnings("null")
  public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {

    HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest();
    String token = servletRequest.getHeader("Authorization");

    if (token == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized");
    }

    User user = userRepository.findFirstByToken(token)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized"));

    if (user.getTokenExpiredAt() < System.currentTimeMillis()) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }

    return user;
  }

}
