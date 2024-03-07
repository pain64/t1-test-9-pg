package com.example.demo.dao

import com.example.demo.entity.Cat
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.math.BigDecimal

interface CatsDao : CrudRepository<Cat, String> {
    fun findCatById(id: String): Cat?
}