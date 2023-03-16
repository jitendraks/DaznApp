package com.example.myapplication.ui.schedules

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.data.schedules.ScheduleItem
import com.example.myapplication.databinding.ScheduleLayoutBinding
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class SchedulesAdapter @Inject constructor(): RecyclerView.Adapter<SchedulesAdapter.ViewHolder>() {
    private val webDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    private var schedulesList = ArrayList<ScheduleItem>()

    fun setSchedules(schedulesList: List<ScheduleItem>) {
        this.schedulesList = ArrayList(schedulesList)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ScheduleLayoutBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ScheduleLayoutBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(schedulesList[position].imageUrl)
            .into(holder.binding.scheduleImage)
        holder.binding.scheduleTitle.text = schedulesList[position].title
        holder.binding.scheduleSubtitle.text = schedulesList[position].subtitle
        holder.binding.scheduleDate.text = getFormattedDate(webDateFormat.parse(schedulesList[position].date))
    }

    override fun getItemCount(): Int {
        return schedulesList.size
    }

    private fun getFormattedDate(targetTime: Date?): String {
        if (targetTime == null) {
            return ""
        }
        val targetDate = Calendar.getInstance()
        targetDate.time= targetTime
        val now = Calendar.getInstance()
        val timeFormatString = SimpleDateFormat("H:mm")
        return if (now[Calendar.DATE] === targetDate[Calendar.DATE]) {
            "Today, " + timeFormatString.format(targetTime)
        } else if(targetDate[Calendar.DATE] - now[Calendar.DATE] === 1) {
            "Tomorrow, " + timeFormatString.format(targetTime)
        } else {
            DateUtils.getRelativeTimeSpanString(targetDate.timeInMillis, now.timeInMillis, 0, DateUtils.FORMAT_ABBREV_ALL).toString()
        }
    }
}