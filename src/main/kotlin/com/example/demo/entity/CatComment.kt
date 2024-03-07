package com.example.demo.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity class CatComment(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long,
    val catId: String,
    val comment: String,
)