package com.example.chat.com.example.chat.JSON

import retrofit2.Call
import retrofit2.http.*


interface JSONChatInterface {

    @GET("messages")
    fun getPosts(): Call<List<Post>>

    @FormUrlEncoded
    @POST("message")
    fun postPost(@Field("content")content : String,
                 @Field("login") login : String) : Call<Post>

    @PUT("message/{id}")
    fun putPost(@Path("id") id : Int) : Call<Post>

    @DELETE("message/{id}")
    fun deletePost(@Path("id") id : String) : Call<Post>
}