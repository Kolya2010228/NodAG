package com.nodag.app.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nodag.app.R
import com.nodag.app.workflow.WorkflowManager

/**
 * Адаптер для списка воркфлоу
 */
class WorkflowAdapter(
    private val workflows: List<WorkflowManager>,
    private val onItemClick: (WorkflowManager) -> Unit
) : RecyclerView.Adapter<WorkflowAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val workflowName: TextView = itemView.findViewById(R.id.workflowName)
        private val workflowDescription: TextView = itemView.findViewById(R.id.workflowDescription)
        private val workflowNodesCount: TextView = itemView.findViewById(R.id.workflowNodesCount)

        fun bind(workflow: WorkflowManager) {
            workflowName.text = workflow.name
            workflowDescription.text = workflow.description.ifEmpty { "Нет описания" }
            workflowNodesCount.text = "Нод: ${workflow.nodes.size}"

            itemView.setOnClickListener {
                onItemClick(workflow)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workflow, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(workflows[position])
    }

    override fun getItemCount(): Int = workflows.size
}
