package com.example.demo.dao

import com.example.demo.entity.CatComment
import org.springframework.data.repository.CrudRepository

interface CatCommentsDao : CrudRepository<CatComment, Long> {
    fun findByCatId(catId: String): List<CatComment>
}