package com.vk.dachecker.taskieandroidnetworkingexplanation.ui.notes

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.vk.dachecker.taskieandroidnetworkingexplanation.App
import com.vk.dachecker.taskieandroidnetworkingexplanation.databinding.FragmentNotesBinding
import com.vk.dachecker.taskieandroidnetworkingexplanation.model.Task
import com.vk.dachecker.taskieandroidnetworkingexplanation.networking.NetworkStatusChecker
import com.vk.dachecker.taskieandroidnetworkingexplanation.ui.notes.dialog.AddTaskDialogFragment
import com.vk.dachecker.taskieandroidnetworkingexplanation.ui.notes.dialog.TaskOptionsDialogFragment
import com.vk.dachecker.taskieandroidnetworkingexplanation.utils.gone
import com.vk.dachecker.taskieandroidnetworkingexplanation.utils.toast
import com.vk.dachecker.taskieandroidnetworkingexplanation.utils.visible

class NotesFragment : Fragment(), AddTaskDialogFragment.TaskAddedListener,
    TaskOptionsDialogFragment.TaskOptionSelectedListener {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { TaskAdapter(::onItemSelected) }
    private val remoteApi = App.remoteApi
    private val networkStatusChecker by lazy {
        NetworkStatusChecker(activity?.getSystemService(ConnectivityManager::class.java))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentNotesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initListeners()
    }

    private fun initUi() {
        binding.progress.visible()
        binding.noData.visible()
        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.tasksRecyclerView.adapter = adapter
        getAllTasks()
    }

    private fun initListeners() {
        binding.addTask.setOnClickListener { addTask() }
    }

    private fun onItemSelected(taskId: String) {
        val dialog = TaskOptionsDialogFragment.newInstance(taskId)
        dialog.setTaskOptionSelectedListener(this)
        dialog.show(childFragmentManager, dialog.tag)
    }

    override fun onTaskAdded(task: Task) {
        adapter.addData(task)
    }

    private fun addTask() {
        val dialog = AddTaskDialogFragment()
        dialog.setTaskAddedListener(this)
        dialog.show(childFragmentManager, dialog.tag)
    }

    private fun getAllTasks() {
        binding.progress.visible()
        networkStatusChecker.performIfConnectedToInternet {
            remoteApi.getTasks { tasks, error ->
                if (tasks.isNotEmpty()) {
                    onTaskListReceived(tasks)
                } else if (error != null) {
                    onGetTasksFailed()
                }
            }
        }
    }

    private fun checkList(notes: List<Task>) {
        if (notes.isEmpty()) binding.noData.visible() else binding.noData.gone()
    }

    private fun onTasksReceived(tasks: List<Task>) = adapter.setData(tasks)

    private fun onTaskListReceived(tasks: List<Task>) {
        binding.progress.gone()
        checkList(tasks)
        onTasksReceived(tasks)
    }

    private fun onGetTasksFailed() {
        binding.progress.gone()
        activity?.toast("Failed to fetch tasks!")
    }

    override fun onTaskDeleted(taskId: String) {
        adapter.removeTask(taskId)
        activity?.toast("Task deleted!")
    }

    override fun onTaskCompleted(taskId: String) {
        adapter.removeTask(taskId)
        activity?.toast("Task completed!")
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}