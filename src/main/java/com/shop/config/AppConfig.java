package com.shop.config;

import com.shop.model.SaleMemento;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Stack;

@Configuration
public class AppConfig {
    @Bean
    public Stack<SaleMemento> salesHistory() {
        return new Stack<>();
    }
}