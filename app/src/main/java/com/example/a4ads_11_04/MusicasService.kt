package com.example.a4ads_11_04

import retrofit2.Response
import retrofit2.http.GET

interface MusicasService {

    @GET("/musicas")
    suspend fun getAll(): Response<List<Musica>>
}