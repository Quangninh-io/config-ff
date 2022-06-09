package com.bkit.skinff.activity;

import static com.bkit.skinff.utilities.Constants.BUTTON_TAG;
import static com.bkit.skinff.utilities.Constants.CHOSE_FILE;
import static com.bkit.skinff.utilities.Constants.CHOSE_IMAGE;

import static com.bkit.skinff.utilities.Constants.COLLECTION;
import static com.bkit.skinff.utilities.Constants.FILE_FREE_FIRE;
import static com.bkit.skinff.utilities.Constants.FILE_RESCONF_FREE_FIRE;
import static com.bkit.skinff.utilities.Constants.INITIAL_URI;
import static com.bkit.skinff.utilities.Constants.KEY_IMAGE;
import static com.bkit.skinff.utilities.Constants.KEY_TIME;
import static com.bkit.skinff.utilities.Constants.OPEN_DOCUMENT_TREE;
import static com.bkit.skinff.utilities.Constants.PUT_BUNDLE;
import static com.bkit.skinff.utilities.Constants.PUT_INTENT;
import static com.bkit.skinff.utilities.Constants.RESCONF;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.widget.DatePicker;
import com.bkit.skinff.databinding.ActivityMainBinding;
import com.bkit.skinff.encode.Encode;
import com.bkit.skinff.firebase.DownloadFile;
import com.bkit.skinff.firebase.ReadToFireStore;
import com.bkit.skinff.firebase.UploadFile;
import com.bkit.skinff.firebase.WriteToFirestore;
import com.bkit.skinff.listener.ReceiveDataFromFirebase;
import com.bkit.skinff.model.FileData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity  {


    private ActivityMainBinding binding;
    UploadFile uploadFile;

    // location storage
    DownloadFile downloadFile;
    WriteToFirestore writeToFirestore;
    Uri uriFile;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initMain();

    }

    private void initMain() {
        binding.btnTime.setOnClickListener(v -> {
            getTime();
        });
        binding.btnChoseImage.setOnClickListener(v->{
            choseImage();
        });
        binding.btnUpload.setOnClickListener(v -> {
            requestSdcardAccessPermission();
            writeToFirestore.uploadTimeToFirestore();
        });
        binding.btnDelete.setOnClickListener(v->{

        });
        binding.btnDownload.setOnClickListener(v->{
            Log.d("fuianv", String.valueOf(uriFile));
            downloadFile();
        });
        binding.btnShowData.setOnClickListener(v->{
            showData();
        });
        uploadFile = new UploadFile(binding.txtGetTime, binding.btnUpload, binding.pbUpload);
        writeToFirestore = new WriteToFirestore(binding.pbUpload, binding.txtGetTime, db);

    }

    public void showData() {
        ReadToFireStore readToFireStore = new ReadToFireStore(new ReceiveDataFromFirebase() {
            @Override
            public void data(ArrayList<FileData> list) {
                Intent intent = new Intent(getApplicationContext(),DisplayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(PUT_BUNDLE,list);
                intent.putExtra(PUT_INTENT,bundle);
                startActivity(intent);
            }
        });
    }

    private void downloadFile() {
        downloadFile = new DownloadFile(binding.txtGetTime, binding.pbUpload, binding.imgResult);
        downloadFile.downloadImage();
        String path =FILE_RESCONF_FREE_FIRE;
        downloadFile.downLoadFile(Uri.parse(path),getApplication());
    }


    private void choseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,CHOSE_IMAGE);
    }

    private void getTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int m = month+1;
                        String dateUpdate = dayOfMonth+"-" + m +"-"+ year;
                        binding.txtGetTime.setText(dateUpdate);
                    }
                },
                year, month, dayOfMonth);

        datePickerDialog.show();
    }


    public void requestSdcardAccessPermission() {
        // to can access android/data
        Intent intent = new Intent(OPEN_DOCUMENT_TREE);
        // will be access first when opening storage
        Uri parse = Uri.parse(FILE_FREE_FIRE);
        intent.putExtra(INITIAL_URI,
                DocumentsContract.buildDocumentUriUsingTree(parse, DocumentsContract.getTreeDocumentId(parse)));
        startActivityForResult(intent, CHOSE_FILE);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent resultData) {
        if (resultCode == Activity.RESULT_OK && requestCode==CHOSE_FILE) {
            assert resultData != null;
            Uri uri = resultData.getData();
            binding.btnUpload.setTag(BUTTON_TAG);
            getContentResolver().takePersistableUriPermission(uri, resultData.getFlags() & 3);
            handleMain(uri);
        }

        if(requestCode == CHOSE_IMAGE && resultCode == Activity.RESULT_OK){
            assert resultData != null;
            Uri uri = resultData.getData();
            binding.img.setImageURI(uri);
            binding.btnUpload.setTag("");
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(uri);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // upload image to  Firestore
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            String encodeImage =Encode.getInstance().encodeImage(bitmap);
            writeToFirestore.uploadImageToFirestore(encodeImage);
            //upload image to Storage
            uploadFile.putFileFirebaseStorage(uri);
        }
        super.onActivityResult(requestCode, resultCode, resultData);
    }


    private void handleMain(Uri treeUri) {
        getContentResolver().takePersistableUriPermission(treeUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        DocumentFile pickedDir = DocumentFile.fromTreeUri(getApplicationContext(), treeUri);
        assert pickedDir != null;
        DocumentFile[] ddff = pickedDir.listFiles();
        for (DocumentFile df : ddff) {
            String name = df.getName();
            Uri uri = df.getUri();
            assert name != null;
            //Log.d("jkfamnv", String.valueOf(uri));
            if (name.contains(RESCONF)) {
                uploadFile.putFileFirebaseStorage(uri);
                uriFile = uri;
                break;
            }
        }
    }

}