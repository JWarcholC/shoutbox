package com.example.chat.com.example.chat.JSON

import retrofit2.Call
import retrofit2.http.*


interface JSONChatInterface {

    @GET("messages")
    fun getPosts(): Call<List<Post>>

    @POST("message")
    fun postPost() : Boolean

    @PUT("message/{id}")
    fun putPost(@Path("id") id : Int) : Boolean

    @DELETE("message/{id}")
    fun deletePost(@Path("id") id : Int) : Boolean
}