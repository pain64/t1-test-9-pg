package com.example.demo

import com.example.demo.dao.CatCommentsDao
import com.example.demo.dao.CatsDao
import com.example.demo.entity.Cat
import com.example.demo.entity.CatComment
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.bind.annotation.*

class AddCatCommentRequest(
    val catId: String,
    val comment: String
)

@Schema(
    oneOf = [
        AddCatCommentResponse.Ok::class,
        AddCatCommentResponse.CatNotFound::class
    ],
    discriminatorProperty = "type"
)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
sealed class AddCatCommentResponse {
    data object Ok : AddCatCommentResponse()
    data object CatNotFound : AddCatCommentResponse()
}

data class RandomCatResponse(
    val id: String,
    val mimeType: String,
    val tags: List<String>,
    val comments: List<String>,
)

@RestController
@RequestMapping("/api/v1/")
class CatsController(
    val catsService: CatsService,
    val catsDao: CatsDao,
    val catCommentsDao: CatCommentsDao,
) {

    @GetMapping(path = ["/randomCat", "/randomCat/{tag}"])
    @ResponseBody
    fun getRandomCat(@PathVariable(required = false) tag: String?) =
        catsService.randomCat(tag)?.let { cd ->
            catsDao.save(
                Cat(cd.id, cd.mimeType, cd.tags.joinToString(separator = " "))
            )

            RandomCatResponse(
                cd.id, cd.mimeType, cd.tags,
                catCommentsDao.findByCatId(cd.id)
                    .map { it.comment }
            )
        }

    @PostMapping("/addComment")
    @ResponseBody
    fun addComment(@RequestBody request: AddCatCommentRequest): AddCatCommentResponse =
        if (catsDao.findCatById(request.catId) == null)
            AddCatCommentResponse.CatNotFound
        else {
            catCommentsDao.save(CatComment(0, request.catId, request.comment))
            AddCatCommentResponse.Ok
        }
}