package com.nodag.app.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nodag.app.R
import com.nodag.app.nodes.PortDirection

/**
 * Адаптер для списка портов (входов/выходов)
 */
class PortAdapter(
    private val direction: PortDirection,
    private val onDeleteClick: (String) -> Unit
) : RecyclerView.Adapter<PortAdapter.ViewHolder>() {

    private val ports = mutableListOf<PortItem>()

    fun addPort(port: PortItem) {
        ports.add(port)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val portName: TextView = itemView.findViewById(R.id.portName)
        private val portType: TextView = itemView.findViewById(R.id.portType)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

        fun bind(port: PortItem) {
            portName.text = port.name
            portType.text = port.type.name

            btnDelete.setOnClickListener {
                onDeleteClick(port.name)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_port, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ports[position])
    }

    override fun getItemCount(): Int = ports.size
}
