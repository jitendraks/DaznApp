package com.example.myapplication.ui.events

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.events.EventItem
import com.example.myapplication.databinding.FragmentEventsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class EventsFragment : Fragment() {

    private var _binding: FragmentEventsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: EventsViewModel
    @Inject lateinit var eventsAdapter: EventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        prepareRecyclerView()
        viewModel = ViewModelProvider(this)[EventsViewModel::class.java]
        viewModel.getEvents()
        viewModel.observeEventsLiveData().observe(viewLifecycleOwner) { eventsList ->
            eventsAdapter.setEvents(eventsList)
        }
        viewModel.observeLoadingLiveData().observe(viewLifecycleOwner) {
            when (it) {
                true -> binding.progressBar.visibility = View.VISIBLE
                false -> binding.progressBar.visibility = View.GONE
            }
        }
        return root
    }

    private fun prepareRecyclerView() {
        eventsAdapter.onItemClick = { item: Any ->
            // Perform action on click
            val uri: Uri = Uri.parse((item as EventItem).videoUrl)
            val intent = Intent(Intent.ACTION_VIEW, uri)

            startActivity(intent)
        }

        binding.rvEvents.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = eventsAdapter

            val dividerItemDecoration = DividerItemDecoration(
                getContext(),
                (layoutManager as LinearLayoutManager).getOrientation()
            )
            addItemDecoration(dividerItemDecoration)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}