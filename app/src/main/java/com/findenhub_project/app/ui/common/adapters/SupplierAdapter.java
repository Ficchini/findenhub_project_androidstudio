// Adapter para exibir usuários do tipo SUPPLIER (perfil, não serviço).
package com.findenhub_project.app.ui.common.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.findenhub_project.app.R;
import com.findenhub_project.app.data.model.User;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.SupplierViewHolder> {

    public interface OnSupplierClickListener {
        void onClick(User supplier);
    }

    private List<User> suppliers;
    private final OnSupplierClickListener listener;

    public SupplierAdapter(List<User> suppliers, OnSupplierClickListener listener) {
        this.suppliers = suppliers;
        this.listener  = listener;
    }

    public void updateList(List<User> newList) {
        this.suppliers = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SupplierViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_supplier, parent, false);
        return new SupplierViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupplierViewHolder holder, int position) {
        User supplier = suppliers.get(position);
        holder.tvName.setText(supplier.getName());
        holder.tvCategory.setText(supplier.getCategory());
        holder.itemView.setOnClickListener(v -> listener.onClick(supplier));
    }

    @Override
    public int getItemCount() { return suppliers.size(); }

    static class SupplierViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCategory;
        SupplierViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName     = itemView.findViewById(R.id.tv_item_supplier_name);
            tvCategory = itemView.findViewById(R.id.tv_item_supplier_category);
        }
    }
}
