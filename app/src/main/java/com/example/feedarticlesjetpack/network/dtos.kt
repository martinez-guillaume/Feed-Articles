package com.example.feedarticlesjetpack.network

import com.squareup.moshi.Json

data class RegisterDto(
    @Json(name = "login") val login: String,
    @Json(name = "mdp") val mdp: String
)
data class LoginDto(
    @Json(name = "login") val login: String,
    @Json(name = "mdp") val mdp: String
)
data class UpdateArticleDto(
    @Json(name = "id") val id: Long,
    @Json(name = "title") val title: String,
    @Json(name = "desc") val desc: String,
    @Json(name = "image") val image: String,
    @Json(name = "cat") val cat: Int
)
data class NewArticleDto(
    @Json(name = "id_u") val id_u: Long,
    @Json(name = "title") val title: String,
    @Json(name = "desc") val desc: String,
    @Json(name = "image") val image: String,
    @Json(name = "cat") val cat: Int
)
data class ApiResponse(
    @Json(name = "status") val status: Int,
    @Json(name = "id") val id: Long? = null,
    @Json(name = "token") val token: String? = null
)
data class ArticlesResponse(
    @Json(name = "status") val status: String,
    @Json(name = "articles") val articles: List<ArticleDto>
)

data class ArticleDto(
    @Json(name = "id") val id: Long,
    @Json(name = "titre") val titre: String,
    @Json(name = "descriptif") val descriptif: String,
    @Json(name = "url_image") val url_image: String,
    @Json(name = "categorie") val categorie: Int,
    @Json(name = "created_at") val created_at: String,
    @Json(name = "id_u") val id_u: Long,
    @Json(name = "is_fav") val is_fav: Int
)
data class ArticleResponse(
    @Json(name = "status") val status: String,
    @Json(name = "article") val article: ArticleDto?
)







