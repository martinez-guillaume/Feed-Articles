package com.example.feedarticlesjetpack.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


object ApiArticleRoutes {
    const val BASE_URL = "https://dev.dev-id.fr/formation/api/"

    const val REGISTER_USER = "articles/user/"
    const val LOGIN_USER = "articles/user/"
    const val GET_ARTICLES = "articles/"
    const val GET_ARTICLE = "articles/{id}"
    const val POST_ARTICLE = "articles/{id}"
    const val DELETE_ARTICLE = "articles/{id}"
    const val PUT_ARTICLE = "articles/"
    const val UPDATE_FAVORITE_STATUS = "articles/{id}"

    private fun getClient(): Retrofit {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val moshi = Moshi.Builder().apply {
            add(KotlinJsonAdapterFactory())
        }.build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
    }

    val apiService: ApiArticleInterface = getClient().create(ApiArticleInterface::class.java)
}


interface ApiArticleInterface {
    @PUT(ApiArticleRoutes.REGISTER_USER)
    suspend fun registerUser(@Body registerDto: RegisterDto): Response<ApiResponse>?

    @FormUrlEncoded
    @POST(ApiArticleRoutes.LOGIN_USER)
    suspend fun loginUser(
        @Field("login") login: String,
        @Field("mdp") password: String
    ): Response<ApiResponse>?

    @GET(ApiArticleRoutes.GET_ARTICLES)
    suspend fun getArticles(
        @Query("with_fav") withFav: Int,
        @Header("token") token: String
    ): Response<ArticlesResponse>?

    @GET(ApiArticleRoutes.GET_ARTICLE)
    suspend fun getArticle(
        @Path("id") articleId: Long,
        @Query("with_fav") withFav: Int,
        @Header("token") token: String
    ): Response<ArticleResponse>?

    @POST(ApiArticleRoutes.POST_ARTICLE)
    suspend fun updateArticle(
        @Path("id") articleId: Long,
        @Header("token") token: String,
        @Body updateArticleDto: UpdateArticleDto
    ): Response<ApiResponse>?

    @DELETE(ApiArticleRoutes.DELETE_ARTICLE)
    suspend fun deleteArticle(
        @Path("id") articleId: Long,
        @Header("token") token: String
    ): Response<ApiResponse>?

    @PUT(ApiArticleRoutes.PUT_ARTICLE)
    suspend fun createArticle(
        @Header("token") token: String,
        @Body newArticleDto: NewArticleDto
    ): Response<ApiResponse>?

    @PUT(ApiArticleRoutes.UPDATE_FAVORITE_STATUS)
    suspend fun updateFavoriteStatus(
        @Path("id") articleId: Long,
        @Header("token") token: String
    ): Response<ApiResponse>?

}


