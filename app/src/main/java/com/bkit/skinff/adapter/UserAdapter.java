package com.bkit.skinff.adapter;


import static com.bkit.skinff.utilities.Constants.LIMITED_DATE_SET_NEW;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bkit.skinff.databinding.ItemUserMainAdpaterBinding;
import com.bkit.skinff.listener.ClickSpecificItem;
import com.bkit.skinff.model.FileData;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

// user for recycle view in main, outfit, weapon
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserMainViewHolder>{
    List<FileData> list;
    Date currentTime = Calendar.getInstance().getTime();
    Context context;
    private final ClickSpecificItem specificItem;

    public UserAdapter(List<FileData> list, Context context,ClickSpecificItem specificItem) {
        this.list = list;
        this.context = context;
        this.specificItem = specificItem;
    }

    @NonNull
    @Override
    public UserMainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserMainViewHolder(
                ItemUserMainAdpaterBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserMainViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        if(list!=null){
            return list.size();
        }
        return 0;
    }

    public class UserMainViewHolder extends RecyclerView.ViewHolder{
        private final ItemUserMainAdpaterBinding binding;


        public UserMainViewHolder(@NonNull ItemUserMainAdpaterBinding itemUserMainAdpaterBinding) {
            super(itemUserMainAdpaterBinding.getRoot());
            binding = itemUserMainAdpaterBinding;
        }
        // calculated time now with time of packages, if package time less current time -30, set new and vice versa
        @SuppressLint("SetTextI18n")
        void setData(FileData fileData)  {
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

            binding.tvName.setText(fileData.getName());
            binding.tvTime.setText(fileData.getTime());
            binding.getRoot().setOnClickListener(v-> {
                specificItem.click(fileData);
            });
        }
    }
}
