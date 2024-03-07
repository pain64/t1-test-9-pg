package com.example.demo

import com.example.demo.util.getForObject
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate


data class CatData(
    @JsonProperty("_id")
    val id: String,
    @JsonProperty("mimetype")
    val mimeType: String,
    val tags: List<String>,
)

interface CatsService {
    fun randomCat(tag: String?): CatData?
}

class CataasService(
    private val restTemplate: RestTemplate
) : CatsService {

    override fun randomCat(tag: String?) =
        try {
            restTemplate.getForObject<CatData>(
                // "/cat${tag?.let { "/$it" } ?: ""}?json=true"
                "/cat${if (tag == null) "" else "/$tag"}?json=true"

            ) ?: throw Exception("unexpected null response from cats service")
        } catch (e: HttpClientErrorException.NotFound) {
            null
        }
}