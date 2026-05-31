package com.findenhub_project.app.ui.common.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.findenhub_project.app.R;
import com.findenhub_project.app.data.model.Event;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    public interface OnEventClickListener {
        void onClick(Event event);
    }

    private List<Event> events;
    private final OnEventClickListener listener;

    public EventAdapter(List<Event> events, OnEventClickListener listener) {
        this.events   = events;
        this.listener = listener;
    }

    public void updateList(List<Event> newList) {
        this.events = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);
        holder.tvTitle.setText(event.getTitle());
        holder.tvCategory.setText(event.getCategory());
        holder.tvDate.setText(event.getDate());
        holder.tvStatus.setText(event.getStatus());
        holder.itemView.setOnClickListener(v -> listener.onClick(event));
    }

    @Override
    public int getItemCount() { return events.size(); }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCategory, tvDate, tvStatus;
        EventViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle    = itemView.findViewById(R.id.tv_item_event_title);
            tvCategory = itemView.findViewById(R.id.tv_item_event_category);
            tvDate     = itemView.findViewById(R.id.tv_item_event_date);
            tvStatus   = itemView.findViewById(R.id.tv_item_event_status);
        }
    }
}
