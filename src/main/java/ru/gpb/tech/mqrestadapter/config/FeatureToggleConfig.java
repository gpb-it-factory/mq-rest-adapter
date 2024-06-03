package ru.gpb.tech.mqrestadapter.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Конфиг для доступа к фиче-тоглам из application-toggle.yml
 * */
@Getter
@Configuration
@ConfigurationProperties(prefix = "features")
public class FeatureToggleConfig {
    
    /**
     * Флаг для регулирования работы новой интеграции с сервисом wallet
     */
    boolean isWalletIntegrationEnabled;
}
