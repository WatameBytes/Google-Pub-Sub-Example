package com.app.config;

import com.business.calculator.CalculatorConfig;
import com.business.gcp.GcpConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:app-application.properties")
@ComponentScan("com.app")
@Import({CalculatorConfig.class, GcpConfig.class})
public class AppConfig {
}
