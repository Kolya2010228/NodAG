package com.nodag.app.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nodag.app.R

/**
 * Адаптер для списка шаблонов нод
 */
class NodeTemplateAdapter(
    private val templates: List<NodeTemplate>,
    private val onItemClick: (NodeTemplate) -> Unit
) : RecyclerView.Adapter<NodeTemplateAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nodeIcon: TextView = itemView.findViewById(R.id.nodeIcon)
        private val nodeName: TextView = itemView.findViewById(R.id.nodeName)

        fun bind(template: NodeTemplate) {
            nodeIcon.text = template.icon
            nodeName.text = template.name

            itemView.setOnClickListener {
                onItemClick(template)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_node_template, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(templates[position])
    }

    override fun getItemCount(): Int = templates.size
}
