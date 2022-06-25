package com.bkit.skinff.adapter;

import static com.bkit.skinff.utilities.Constants.LIMITED_DATE_SET_NEW;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bkit.skinff.R;
import com.bkit.skinff.databinding.ItemAdminBinding;
import com.bkit.skinff.listener.ClickToModify;
import com.bkit.skinff.model.FileData;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
// use for recycle in admin preview activity
public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.AdminViewHolder> {

    List<FileData> list;
    ClickToModify clickToDownload;

    public AdminAdapter(List<FileData> list, ClickToModify clickToDownload) {
        this.list = list;
        this.clickToDownload = clickToDownload;
    }

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
    public void onBindViewHolder(@NonNull AdminViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setData(list.get(position));
        holder.binding.getRoot().setOnClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(R.string.admin_request_delete)
                    .setTitle("Xoa");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @SuppressLint("NotifyDataSetChanged")
                public void onClick(DialogInterface dialog, int id) {
                    clickToDownload.modify(list.get(position));
                    list.remove(position);
                    notifyItemRemoved(position);
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    dialog.cancel();
                }
            });
            builder.create().show();
        });

    }

    @Override
    public int getItemCount() {
        if(list!=null){
            return list.size();
        }
        return 0;
    }

    public static class AdminViewHolder extends RecyclerView.ViewHolder{
        private final ItemAdminBinding binding;
        public AdminViewHolder(ItemAdminBinding itemAdminBinding) {
            super(itemAdminBinding.getRoot());
            binding = itemAdminBinding;
        }

        void setData(FileData fileData){
            //binding.ivAdmin.setImageBitmap(fileData.getImage());
            Picasso.get().load(fileData.getImage()).into(binding.ivPackage);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Instant now = Instant.now(); //current date
            Instant before = now.minus(Duration.ofDays(LIMITED_DATE_SET_NEW));
            Date dateBefore = Date.from(before);
            String thirtyDayAgo = simpleDateFormat.format(dateBefore);
            Log.d("time", thirtyDayAgo);
            try {
                Date dateInFirebase = simpleDateFormat.parse(fileData.getTime());
                Date dateInReality = simpleDateFormat.parse(thirtyDayAgo);
                if(dateInFirebase.compareTo(dateInReality)>0){
                    binding.tvNew.setVisibility(View.VISIBLE);
                }else{
                    binding.tvNew.setVisibility(View.INVISIBLE);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(fileData.getCheckName()){
                binding.tvCheckName.setVisibility(View.INVISIBLE);
            }
            else{
                binding.tvCheckName.setVisibility(View.VISIBLE);
            }
            binding.tvFilename.setText(fileData.getNameFile());
            binding.tvName.setText(fileData.getName());
            binding.tvTime.setText(fileData.getTime());
        }
    }

}
