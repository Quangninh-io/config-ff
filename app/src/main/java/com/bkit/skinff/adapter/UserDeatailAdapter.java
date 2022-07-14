package com.bkit.skinff.adapter;


import static com.bkit.skinff.utilities.Constants.KEY_MODEL;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT;
import static com.bkit.skinff.utilities.Constants.KEY_TIME;
import static com.bkit.skinff.utilities.Constants.KEY_TYPE;
import static com.bkit.skinff.utilities.Constants.LIMITED_DATE_SET_NEW;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bkit.skinff.R;
import com.bkit.skinff.databinding.ItemUserDetailAdpaterBinding;
import com.bkit.skinff.databinding.ItemUserMainAdpaterBinding;
import com.bkit.skinff.listener.ClickSpecificItem;
import com.bkit.skinff.model.FileData;
import com.bkit.skinff.sharepreference.GetUri;
import com.bkit.skinff.utilities.SetLanguage;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

// user for recycle view in main, outfit, weapon
public class UserDeatailAdapter extends RecyclerView.Adapter<UserDeatailAdapter.UserMainViewHolder>{
    List<FileData> list;
    Date currentTime = Calendar.getInstance().getTime();
    String time = "", mod ="", type ="";
    Context context;
    GetUri getUri = GetUri.getInstance();
    ClickSpecificItem specificItem;
    SetLanguage setLanguage = SetLanguage.getInstance();

    public UserDeatailAdapter(List<FileData> list, Context context, ClickSpecificItem specificItem) {
        this.list = list;
        this.context = context;
        this.specificItem = specificItem;
    }

    @NonNull
    @Override
    public UserMainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserMainViewHolder(
                ItemUserDetailAdpaterBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserMainViewHolder holder, int position) {
        holder.setData(list.get(position));
        holder.binding.getRoot().setOnClickListener(v-> {
            specificItem.click(list.get(position));
            specificItem.pos(position);

        });
    }

    @Override
    public int getItemCount() {
        if(list!=null){
            return list.size();
        }
        return 0;
    }

    public class UserMainViewHolder extends RecyclerView.ViewHolder{
        private final ItemUserDetailAdpaterBinding binding;


        public UserMainViewHolder(@NonNull ItemUserDetailAdpaterBinding itemUserDetailAdpaterBinding) {
            super(itemUserDetailAdpaterBinding.getRoot());
            binding = itemUserDetailAdpaterBinding;
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

            getShar(fileData);
            if(fileData.getActive()){
                binding.tvActive.setVisibility(View.VISIBLE);
            }else{
                binding.tvActive.setVisibility(View.INVISIBLE);
            }
            binding.tvTime.setText(fileData.getTime());
            setLanguage.configLanguage(context);
            if(fileData.getType().equals(KEY_OUTFIT)){
                binding.tvName.setText(context.getResources().getString(R.string.collection_outfit)+fileData.getName());
            }else{
                binding.tvName.setText(context.getResources().getString(R.string.collection_gun)+fileData.getName());
            }
        }
    }
    private void getShar(FileData fileData){
        if(fileData.getType().equals("gun")){
            time = getUri.getStatusGun(context,KEY_TIME);
            mod = getUri.getStatusGun(context,KEY_MODEL);
            type = getUri.getStatusGun(context,KEY_TYPE);
        }else{
            time = getUri.getStatusOutfit(context,KEY_TIME);
            mod = getUri.getStatusOutfit(context,KEY_MODEL);
            type = getUri.getStatusOutfit(context,KEY_TYPE);
        }
        if(time.equals(fileData.getTime()) && mod.equals(fileData.getModel())  && type.equals(fileData.getType())){
            fileData.setActive(true);
        }else{
            fileData.setActive(false);
        }
    }
}
