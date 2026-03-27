package com.nodag.app.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.nodag.app.R;
import com.nodag.app.workflow.WorkflowManager;
import java.util.List;

/**
 * Адаптер для списка воркфлоу
 */
public class WorkflowAdapter extends RecyclerView.Adapter<WorkflowAdapter.ViewHolder> {

    private final List<WorkflowManager> workflows;
    private final OnWorkflowClickListener listener;

    public interface OnWorkflowClickListener {
        void onWorkflowClick(WorkflowManager workflow);
    }

    public WorkflowAdapter(List<WorkflowManager> workflows, OnWorkflowClickListener listener) {
        this.workflows = workflows;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_workflow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(workflows.get(position));
    }

    @Override
    public int getItemCount() {
        return workflows.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView workflowName;
        private TextView workflowDescription;
        private TextView workflowNodesCount;

        ViewHolder(View itemView) {
            super(itemView);
            workflowName = itemView.findViewById(R.id.workflowName);
            workflowDescription = itemView.findViewById(R.id.workflowDescription);
            workflowNodesCount = itemView.findViewById(R.id.workflowNodesCount);
        }

        void bind(WorkflowManager workflow) {
            workflowName.setText(workflow.getName());
            workflowDescription.setText(workflow.getDescription().isEmpty() 
                ? "Нет описания" 
                : workflow.getDescription());
            workflowNodesCount.setText("Нод: " + workflow.getNodes().size());

            itemView.setOnClickListener(v -> listener.onWorkflowClick(workflow));
        }
    }
}
