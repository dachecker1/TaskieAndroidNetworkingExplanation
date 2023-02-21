package com.vk.dachecker.taskieandroidnetworkingexplanation.ui.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vk.dachecker.taskieandroidnetworkingexplanation.databinding.ItemTaskBinding
import com.vk.dachecker.taskieandroidnetworkingexplanation.model.Task

class TaskAdapter(
    private val onItemLongClick: (String) -> Unit
) : RecyclerView.Adapter<TaskHolder>() {

    private val data: MutableList<Task> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val binding = ItemTaskBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bindData(data[position], onItemLongClick)
    }

    fun addData(item: Task) {
        data.add(item)
        notifyItemInserted(data.size)
    }

    fun setData(data: List<Task>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun removeTask(taskId: String) {
        val taskIndex = data.indexOfFirst { it.id == taskId }

        if (taskIndex != -1) {
            data.removeAt(taskIndex)
            notifyItemRemoved(taskIndex)
        }
    }
}