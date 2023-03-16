package com.example.myapplication.ui.schedules

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentSchedulesBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SchedulesFragment : Fragment() {

    private var _binding: FragmentSchedulesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: SchedulesViewModel

    @Inject lateinit var schedulesAdapter: SchedulesAdapter

    lateinit var handler: Handler
    lateinit var runnableCode: Runnable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSchedulesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        prepareRecyclerView()
        viewModel = ViewModelProvider(this)[SchedulesViewModel::class.java]
        // Create the Handler object (on the main thread by default)
        handler = Handler(Looper.getMainLooper())

        // Define the code block to be executed
        runnableCode = object : Runnable {
            override fun run() {
                viewModel.getSchedules()
                handler.postDelayed(this, 30*1000)
            }
        }
        // Start the initial runnable task by posting through the handler
        handler.post(runnableCode)

        viewModel.observeSchedulesLiveData().observe(viewLifecycleOwner, { schedulesList ->
            schedulesAdapter.setSchedules(schedulesList)
        })
        viewModel.observeLoadingLiveData().observe(viewLifecycleOwner, {
            when(it) {
                true -> binding.progressBar.visibility = View.VISIBLE
                false -> binding.progressBar.visibility = View.GONE
            }})
        return root
    }

    private fun prepareRecyclerView() {
        binding.rvSchedules.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = schedulesAdapter
            val dividerItemDecoration = DividerItemDecoration(
                getContext(),
                (layoutManager as LinearLayoutManager).getOrientation()
            )
            addItemDecoration(dividerItemDecoration)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(runnableCode)
        _binding = null
    }
}