package com.example.demo.util

import org.springframework.web.client.RestTemplate

inline fun <reified T> RestTemplate.getForObject(url: String): T? =
    this.getForObject(url, T::class.java)
