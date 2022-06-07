package com.bkit.skinff;

import static com.bkit.skinff.utilities.AssessStorage.BUTTON_TAG;
import static com.bkit.skinff.utilities.AssessStorage.CHOSE_FILE;
import static com.bkit.skinff.utilities.AssessStorage.CHOSE_IMAGE;
import static com.bkit.skinff.utilities.AssessStorage.FILE_FREE_FIRE;
import static com.bkit.skinff.utilities.AssessStorage.INITIAL_URI;
import static com.bkit.skinff.utilities.AssessStorage.NAME_IMAGE;
import static com.bkit.skinff.utilities.AssessStorage.OPEN_DOCUMENT_TREE;
import static com.bkit.skinff.utilities.AssessStorage.RESCONF;
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
import android.view.View;
import android.widget.DatePicker;
import com.bkit.skinff.databinding.ActivityMainBinding;
import com.bkit.skinff.firebase.DownloadFile;
import com.bkit.skinff.firebase.UploadFile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
    UploadFile uploadFile;
    // location storage
    DownloadFile downloadFile;
    Uri uriFile;
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
        });
        binding.btnDelete.setOnClickListener(v->{

        });
        binding.btnDownload.setOnClickListener(v->{
            Log.d("fuianv", String.valueOf(uriFile));
            downloadFile();
        });

        uploadFile = new UploadFile(binding.txtGetTime, binding.btnUpload, binding.pbUpload);
    }

    private void downloadFile() {
        downloadFile = new DownloadFile(binding.txtGetTime, binding.pbUpload, binding.imgResult);
        downloadFile.downloadImage();
        String path ="content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata%2" +
                "Fcom.dts.freefireth%2Ffiles%2Fcontentcache%2FCompulsory%2Fandroid%2" +
                "Fgameassetbundles%2Fconfig/document/primary%3AAndroid%2Fdata%2F" +
                "com.dts.freefireth%2Ffiles%2Fcontentcache%2FCompulsory%2Fandroid%2F" +
                "gameassetbundles%2Fconfig%2Fresconf.UJxBmYOpolKALQXb7bXRfU1Z3Hs~3D";
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
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dateUpdate = dayOfMonth+"-" + month +"-"+ year;
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
            Log.d("jkfamnv", String.valueOf(uri));
            if (name.contains(RESCONF)) {
                uploadFile.putFileFirebaseStorage(uri);
                uriFile = uri;

                break;
            }
        }
    }


}