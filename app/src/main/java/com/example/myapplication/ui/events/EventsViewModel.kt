package com.example.myapplication.ui.events

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.api.RetrofitInstance
import com.example.myapplication.data.events.EventItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventsViewModel : ViewModel() {
    val TAG = "EventsViewModel"
    private val loading = MutableLiveData<Boolean>()
    private var eventsLiveData = MutableLiveData<List<EventItem>>()
    fun getEvents() {
        loading.postValue(true)
        RetrofitInstance.eventsApi.getEvents().enqueue(object  : Callback<List<EventItem>>{
            override fun onResponse(call: Call<List<EventItem>>, response: Response<List<EventItem>>) {
                if (response.body()!=null) {
                    eventsLiveData.value = response.body()!!.sortedByDescending { it.date }
                } else {
                    return
                }
                loading.postValue(false)
            }
            override fun onFailure(call: Call<List<EventItem>>, t: Throwable) {
                Log.d(TAG, t.message.toString())
                loading.postValue(false)
            }
        })
    }

    fun observeEventsLiveData() : LiveData<List<EventItem>> {
        return eventsLiveData
    }

    fun observeLoadingLiveData() : LiveData<Boolean> {
        return loading
    }
}