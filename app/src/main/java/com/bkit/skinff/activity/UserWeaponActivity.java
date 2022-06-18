package com.bkit.skinff.activity;

import static com.bkit.skinff.utilities.Constants.CLOTHES;
import static com.bkit.skinff.utilities.Constants.COLLECTION;
import static com.bkit.skinff.utilities.Constants.INTENT_DETAIL;
import static com.bkit.skinff.utilities.Constants.INTENT_MODEL;
import static com.bkit.skinff.utilities.Constants.INTENT_NAME;
import static com.bkit.skinff.utilities.Constants.INTENT_OUTFIT;
import static com.bkit.skinff.utilities.Constants.INTENT_WEAPON;
import static com.bkit.skinff.utilities.Constants.KEY_IMAGE;
import static com.bkit.skinff.utilities.Constants.KEY_MODEL;
import static com.bkit.skinff.utilities.Constants.KEY_NAME;
import static com.bkit.skinff.utilities.Constants.KEY_NAME_FILE;
import static com.bkit.skinff.utilities.Constants.KEY_TIME;
import static com.bkit.skinff.utilities.Constants.KEY_TYPE;
import static com.bkit.skinff.utilities.Constants.LIMITED_DATE_SET_NEW;
import static com.bkit.skinff.utilities.Constants.RESCONF;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.bkit.skinff.adapter.UserAdapter;
import com.bkit.skinff.databinding.ActivityUserWeaponBinding;
import com.bkit.skinff.firebase.DownloadFile;
import com.bkit.skinff.listener.ClickSpecificItem;
import com.bkit.skinff.model.FileData;
import com.bkit.skinff.model.Name;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserWeaponActivity extends AppCompatActivity {

    private ActivityUserWeaponBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    UserAdapter adapter;
    Uri uriWeapon, uriOutfit;
    Name name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserWeaponBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initMain();
    }

    private void initMain() {
        String result = getIntent().getStringExtra(INTENT_MODEL);
        binding.fbDelete.setOnClickListener(v->{
            String time = "3-12-2000";
            DownloadFile.getInstance().downLoadFile(binding.pbDownload,time,uriWeapon,this,result,"gun",name.getWeapon());
            DownloadFile.getInstance().downLoadFile(binding.pbDownload,time,uriOutfit,this,result,"outfit",name.getOutfit());

        });
        uriWeapon = Uri.parse(getIntent().getStringExtra(INTENT_WEAPON));
        uriOutfit = Uri.parse(getIntent().getStringExtra(INTENT_OUTFIT));
        name = (Name) getIntent().getSerializableExtra(INTENT_NAME);
        db.collection(COLLECTION)
                .whereEqualTo("model",result)
                .whereEqualTo("type","gun")
                .get()
                .addOnCompleteListener(task -> {
                    ArrayList<FileData> fileData = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String image = String.valueOf(document.getData().get(KEY_IMAGE));
                            String model = String.valueOf(document.getData().get(KEY_MODEL));
                            String name = String.valueOf(document.getData().get(KEY_NAME));
                            String time = String.valueOf(document.getData().get(KEY_TIME));
                            String type = String.valueOf(document.getData().get(KEY_TYPE));
                            String documentId = document.getId();
                            String nameFile = String.valueOf(document.getData().get(KEY_NAME_FILE));
                            fileData.add(new FileData(image,model,name,time,type,documentId,nameFile));
                        }
                        uploadRecycleView(fileData);
                    }
                    //receiveDataFromFirebase.data(fileData);
                })
                .addOnFailureListener(e -> {
                    Log.d("error read file","error");
                });

    }

    private void uploadRecycleView(ArrayList<FileData> list) {
        List<FileData> display = new ArrayList<>();
        Instant now = Instant.now(); //current date
        Instant before = now.minus(Duration.ofDays(LIMITED_DATE_SET_NEW));
        Date dateBefore = Date.from(before);
        String thirtyDayAgo = simpleDateFormat.format(dateBefore);
        for (FileData fileData:list){
            Log.d("time", thirtyDayAgo);
            try {
                Date dateInFirebase = simpleDateFormat.parse(fileData.getTime());
                Date dateInReality = simpleDateFormat.parse(thirtyDayAgo);
                assert dateInFirebase != null;
                if(dateInFirebase.compareTo(dateInReality)>0){
                    display.add(fileData);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Log.d("falfia", String.valueOf(display.size()));
        binding.tvType.setText("Súng");
        adapter = new UserAdapter(display, getApplicationContext(), new ClickSpecificItem() {
            @Override
            public void click(FileData fileData) {
                Intent intent = new Intent(getApplication(),UserDetailActivity.class);
                intent.putExtra(INTENT_WEAPON,String.valueOf(uriWeapon));
                intent.putExtra(INTENT_OUTFIT,String.valueOf(uriOutfit));
                intent.putExtra(INTENT_DETAIL,fileData);
                intent.putExtra(INTENT_NAME,name);
                startActivity(intent);
            }
        });
        binding.rvWeapon.setAdapter(adapter);
    }
}