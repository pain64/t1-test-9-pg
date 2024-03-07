package com.example.demo

import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@Configuration
class Configuration {
    @Bean fun catsApiService() =
        CataasService(
            RestTemplateBuilder()
                .rootUri("https://cataas.com/").build()
        )

    @Bean fun jackson2ObjectMapperBuilder() =
        Jackson2ObjectMapperBuilder().modules(
            KotlinModule.Builder()
                .configure(KotlinFeature.StrictNullChecks, true)
                .build()
        )
}