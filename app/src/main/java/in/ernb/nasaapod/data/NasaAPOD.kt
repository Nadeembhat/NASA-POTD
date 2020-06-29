package com.mythio.retrofitsample.network

import com.google.gson.annotations.SerializedName
import java.util.*

data class NasaAPOD(
    @SerializedName("copyright")
    var copyright:String?,
    @SerializedName("date")
    var date: Date,
    @SerializedName("explanation")
    var explanation: String,
    @SerializedName("hdurl")
    var hdurl: String?,
    @SerializedName("media_type")
    var mediatype: String,
    @SerializedName("service_version")
    var sversion: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("url")
    var url: String

)