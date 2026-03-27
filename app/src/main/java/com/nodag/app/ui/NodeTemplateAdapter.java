package com.nodag.app.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.nodag.app.R;
import java.util.List;

/**
 * Адаптер для списка шаблонов нод
 */
public class NodeTemplateAdapter extends RecyclerView.Adapter<NodeTemplateAdapter.ViewHolder> {

    private final List<NodeTemplate> templates;
    private final OnNodeTemplateClickListener listener;

    public interface OnNodeTemplateClickListener {
        void onNodeTemplateClick(NodeTemplate template);
    }

    public NodeTemplateAdapter(List<NodeTemplate> templates, OnNodeTemplateClickListener listener) {
        this.templates = templates;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_node_template, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(templates.get(position));
    }

    @Override
    public int getItemCount() {
        return templates.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nodeIcon;
        private TextView nodeName;

        ViewHolder(View itemView) {
            super(itemView);
            nodeIcon = itemView.findViewById(R.id.nodeIcon);
            nodeName = itemView.findViewById(R.id.nodeName);
        }

        void bind(NodeTemplate template) {
            nodeIcon.setText(template.getIcon());
            nodeName.setText(template.getName());
            itemView.setOnClickListener(v -> listener.onNodeTemplateClick(template));
        }
    }
}
