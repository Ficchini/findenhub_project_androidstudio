package com.findenhub_project.app.ui.common.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import com.findenhub_project.app.R;
import com.findenhub_project.app.data.model.Request;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    public interface OnRequestActionListener {
        void onAccept(Request request);
        void onReject(Request request);
    }

    private List<Request> requests;
    private final OnRequestActionListener listener;

    public RequestAdapter(List<Request> requests, OnRequestActionListener listener) {
        this.requests = requests;
        this.listener = listener;
    }

    public void updateList(List<Request> newList) {
        this.requests = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Request request = requests.get(position);
        holder.tvClientName.setText(request.getClientName() != null ? request.getClientName() : request.getClientId());
        holder.tvMessage.setText(request.getMessage());
        holder.tvStatus.setText(request.getStatus());

        holder.btnAccept.setOnClickListener(v -> listener.onAccept(request));
        holder.btnReject.setOnClickListener(v -> listener.onReject(request));

        // Desabilita botões se o status já foi definido
        boolean pending = "PENDING".equals(request.getStatus());
        holder.btnAccept.setEnabled(pending);
        holder.btnReject.setEnabled(pending);
    }

    @Override
    public int getItemCount() { return requests.size(); }

    static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView tvClientName, tvMessage, tvStatus;
        MaterialButton btnAccept, btnReject;

        RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClientName = itemView.findViewById(R.id.tv_item_request_client_name);
            tvMessage    = itemView.findViewById(R.id.tv_item_request_message);
            tvStatus     = itemView.findViewById(R.id.tv_item_request_status);
            btnAccept    = itemView.findViewById(R.id.btn_item_request_accept);
            btnReject    = itemView.findViewById(R.id.btn_item_request_reject);
        }
    }
}
