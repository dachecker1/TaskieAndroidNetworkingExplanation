package com.vk.dachecker.taskieandroidnetworkingexplanation.ui.notes

import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.vk.dachecker.taskieandroidnetworkingexplanation.databinding.ItemTaskBinding
import com.vk.dachecker.taskieandroidnetworkingexplanation.model.PriorityColor
import com.vk.dachecker.taskieandroidnetworkingexplanation.model.Task

class TaskHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(task: Task, onItemLongClick: (String) -> Unit) {
        binding.root.setOnLongClickListener {
            onItemLongClick(task.id)
            true
        }

        binding.taskTitle.text = task.title
        binding.taskContent.text = task.content

        val drawable = binding.taskPriority.drawable
        val wrapDrawable = DrawableCompat.wrap(drawable)

        val priorityColor = when (task.taskPriority) {
            1 -> PriorityColor.LOW
            2 -> PriorityColor.MEDIUM
            else -> PriorityColor.HIGH
        }

        DrawableCompat.setTint(wrapDrawable,
            ContextCompat.getColor(binding.root.context, priorityColor.getColor()))
    }
}