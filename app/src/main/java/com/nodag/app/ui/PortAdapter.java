package com.nodag.app.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.nodag.app.R;
import com.nodag.app.nodes.PortDirection;
import java.util.ArrayList;
import java.util.List;

/**
 * Адаптер для списка портов (входов/выходов)
 */
public class PortAdapter extends RecyclerView.Adapter<PortAdapter.ViewHolder> {

    private final PortDirection direction;
    private final OnDeleteClickListener onDeleteClick;
    private final List<PortItem> ports;

    public interface OnDeleteClickListener {
        void onDeleteClick(String portName);
    }

    public PortAdapter(PortDirection direction, OnDeleteClickListener onDeleteClick) {
        this.direction = direction;
        this.onDeleteClick = onDeleteClick;
        this.ports = new ArrayList<>();
    }

    public void addPort(PortItem port) {
        ports.add(port);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_port, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(ports.get(position));
    }

    @Override
    public int getItemCount() {
        return ports.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView portName;
        private TextView portType;
        private ImageButton btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            portName = itemView.findViewById(R.id.portName);
            portType = itemView.findViewById(R.id.portType);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        void bind(PortItem port) {
            portName.setText(port.getName());
            portType.setText(port.getType().name());
            btnDelete.setOnClickListener(v -> onDeleteClick.onDeleteClick(port.getName()));
        }
    }
}
