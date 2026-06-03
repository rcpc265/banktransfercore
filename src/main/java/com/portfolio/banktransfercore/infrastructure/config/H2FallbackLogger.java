package com.portfolio.banktransfercore.infrastructure.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class H2FallbackLogger {

  private static final Logger log = LoggerFactory.getLogger(H2FallbackLogger.class);

  @Value("${spring.datasource.url:}")
  private String datasourceUrl;

  @PostConstruct
  void warnIfH2() {
    if (datasourceUrl == null || datasourceUrl.isEmpty() || datasourceUrl.contains("h2")) {
      log.warn("No PostgreSQL configured — using H2 in-memory fallback");
    }
  }
}
