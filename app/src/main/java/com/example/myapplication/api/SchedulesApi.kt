package com.example.myapplication.api

import com.example.myapplication.data.schedules.ScheduleItem
import retrofit2.Call
import retrofit2.http.GET

interface SchedulesApi {
    @GET("getSchedule")
    fun getSchedules() : Call<List<ScheduleItem>>
}