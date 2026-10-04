package com.example.apihttpmethods

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

// 1. Retrofit API Interface
interface ApiService {

    // GET: Fetch list of users
    @GET("users")
    suspend fun getUsers(@Query("page") page: Int = 1): Response<UserListResponse>

    // POST: Create a new user
    @POST("users")
    suspend fun createUser(@Body request: UserRequest): Response<UserMutationResponse>

    // PUT: Complete update on a user
    @PUT("users/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Body request: UserRequest
    ): Response<UserMutationResponse>

    // PATCH: Partial update on a user
    @PATCH("users/{id}")
    suspend fun patchUser(
        @Path("id") id: Int,
        @Body updates: Map<String, String>
    ): Response<UserMutationResponse>

    // DELETE: Remove a user (ReqRes returns HTTP 204 No Content)
    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<Unit>
}

// 2. Retrofit Client Instance Provider
object NetworkClient {

    private const val BASE_URL = "https://reqres.in/api/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}