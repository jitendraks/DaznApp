package com.example.myapplication.api

import com.example.myapplication.data.events.EventItem
import retrofit2.Call
import retrofit2.http.GET

interface EventsApi {
    @GET("getEvents")
    fun getEvents() : Call<List<EventItem>>
}