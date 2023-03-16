package com.example.myapplication.ui.schedules

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.api.RetrofitInstance
import com.example.myapplication.data.schedules.ScheduleItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SchedulesViewModel : ViewModel() {

    private val loading = MutableLiveData<Boolean>()
    private var schedulesLiveData = MutableLiveData<List<ScheduleItem>>()
    fun getSchedules() {
        loading.postValue(true)
        RetrofitInstance.schedulesApi.getSchedules().enqueue(object  : Callback<List<ScheduleItem>> {
            override fun onResponse(call: Call<List<ScheduleItem>>, response: Response<List<ScheduleItem>>) {
                if (response.body()!=null) {
                    schedulesLiveData.value = response.body()!!.sortedBy { it.date }
                } else {
                    return
                }
                loading.postValue(false)
            }
            override fun onFailure(call: Call<List<ScheduleItem>>, t: Throwable) {
                Log.d("TAG",t.message.toString())
                loading.postValue(false)
            }
        })
    }

    fun observeSchedulesLiveData() : LiveData<List<ScheduleItem>> {
        return schedulesLiveData
    }

    fun observeLoadingLiveData() : LiveData<Boolean> {
        return loading
    }
}