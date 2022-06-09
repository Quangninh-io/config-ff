package com.bkit.skinff.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bkit.skinff.databinding.ItemAdminBinding;
import com.bkit.skinff.listener.ClickToDownload;
import com.bkit.skinff.model.FileData;

import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.AdminViewHolder> {

    List<FileData> list;
    ClickToDownload clickToDownload;

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdminViewHolder(
                ItemAdminBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent,
                        false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        if(list!=null){
            return list.size();
        }
        return 0;
    }

    public class AdminViewHolder extends RecyclerView.ViewHolder{
        private final ItemAdminBinding binding;
        public AdminViewHolder(ItemAdminBinding itemAdminBinding) {
            super(itemAdminBinding.getRoot());
            binding = itemAdminBinding;
        }

        void setData(FileData fileData){
            //binding.ivAdmin.setImageBitmap(fileData.getImage());
            binding.tvName.setText("free fire");
            binding.tvTime.setText(fileData.getTime());
            binding.getRoot().setOnClickListener(v -> {
                clickToDownload.downloadFile(fileData);
            });
        }
    }
}
