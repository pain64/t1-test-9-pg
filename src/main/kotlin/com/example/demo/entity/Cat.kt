package com.example.demo.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity class Cat(
    @Id
    val id: String,
    val mimeType: String,
    val tags: String,
)