package com.example.chat.com.example.chat.JSON

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Post() {
    @SerializedName("content")
    @Expose
    lateinit var content: String

    @SerializedName("login")
    @Expose
    lateinit var login: String

    @SerializedName("date")
    @Expose
    lateinit var date: String

    @SerializedName("id")
    @Expose
    lateinit var id : String


}