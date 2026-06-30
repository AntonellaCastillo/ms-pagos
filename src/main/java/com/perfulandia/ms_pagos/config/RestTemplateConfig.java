package com.perfulandia.ms_pagos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// CAPA CONFIG: registra el RestTemplate como un @Bean.
// QUÉ: crea el objeto que usa el MS para llamar a otros microservicios por HTTP.
// PARA QUÉ: Spring lo inyecta donde se necesite (no hago new RestTemplate()).
@Configuration
public class RestTemplateConfig 
{

    @Bean
    public RestTemplate restTemplate() 
    {
        return new RestTemplate();
    }
}