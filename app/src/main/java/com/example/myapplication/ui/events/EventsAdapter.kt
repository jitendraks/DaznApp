package com.example.myapplication.ui.events

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.data.events.EventItem
import com.example.myapplication.databinding.EventLayoutBinding
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class EventsAdapter @Inject constructor(): RecyclerView.Adapter<EventsAdapter.ViewHolder>() {
    private val webDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    private var eventsList = ArrayList<EventItem>()
    var onItemClick: ((Any) -> Unit)? = null

    fun setEvents(eventList: List<EventItem>) {
        this.eventsList = ArrayList<EventItem>(eventList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: EventLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            EventLayoutBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(eventsList[position].imageUrl)
            .into(holder.binding.eventImage)
        holder.binding.eventTitle.text = eventsList[position].title
        holder.binding.eventSubtitle.text = eventsList[position].subtitle
        holder.binding.eventDate.text = getFormattedDate(webDateFormat.parse(eventsList[position].date))
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(eventsList[position])
        }
    }

    override fun getItemCount(): Int {
        return eventsList.size
    }

    private fun getFormattedDate(targetTime: Date?): String {
        if (targetTime == null) {
            return ""
        }
        val targetDate = Calendar.getInstance()
        targetDate.time= targetTime
        val now = Calendar.getInstance()
        val timeFormatString = SimpleDateFormat("H:mm", Locale.getDefault())
        val dateTimeFormatString = SimpleDateFormat("EEEE, MMMM d, H:mm", Locale.getDefault())
        return if (now[Calendar.DATE] == targetDate[Calendar.DATE]) {
            "Today, " + timeFormatString.format(targetTime)
        } else if (now[Calendar.DATE] - targetDate[Calendar.DATE] == 1) {
            "Yesterday, " + timeFormatString.format(targetDate)
        } else if (now[Calendar.YEAR] == targetDate[Calendar.YEAR]) {
            dateTimeFormatString.format(targetDate).toString()
        } else {
            SimpleDateFormat("MMMM dd yyyy, H:mm", Locale.getDefault()).format(targetDate).toString()
        }
    }
}