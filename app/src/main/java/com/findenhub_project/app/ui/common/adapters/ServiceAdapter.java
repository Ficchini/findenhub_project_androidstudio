// Usado tanto em SupplierListFragment (cliente) quanto em SupplierServicesFragment.
package com.findenhub_project.app.ui.common.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.findenhub_project.app.R;
import com.findenhub_project.app.data.model.Service;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    public interface OnServiceClickListener {
        void onClick(Service service);
    }

    private List<Service> services;
    private final OnServiceClickListener listener;

    public ServiceAdapter(List<Service> services, OnServiceClickListener listener) {
        this.services = services;
        this.listener = listener;
    }

    public void updateList(List<Service> newList) {
        this.services = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = services.get(position);
        holder.tvTitle.setText(service.getTitle());
        holder.tvCategory.setText(service.getCategory());
        holder.tvCity.setText(service.getCity());
        holder.tvPrice.setText("R$ " + service.getPriceBase());
        holder.itemView.setOnClickListener(v -> listener.onClick(service));
    }

    @Override
    public int getItemCount() { return services.size(); }

    static class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCategory, tvCity, tvPrice;
        ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle    = itemView.findViewById(R.id.tv_item_service_title);
            tvCategory = itemView.findViewById(R.id.tv_item_service_category);
            tvCity     = itemView.findViewById(R.id.tv_item_service_city);
            tvPrice    = itemView.findViewById(R.id.tv_item_service_price);
        }
    }
}
