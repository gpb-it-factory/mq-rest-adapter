package ru.gpb.tech.mqrestadapter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Конфиг для доступа к фиче-тоглам из application-toggle.yml
 */
@Data
@Component
@ConfigurationProperties(prefix = "features")
public class FeatureToggleConfig {
    
    /**
     * Флаг для регулирования работы новой интеграции с сервисом wallet
     */
    boolean walletIntegrationEnabled;
}
