package com.bkit.skinff.activity;

import static com.bkit.skinff.utilities.Constants.BUTTON_TAG;
import static com.bkit.skinff.utilities.Constants.CHOSE_FILE;
import static com.bkit.skinff.utilities.Constants.CHOSE_IMAGE;

import static com.bkit.skinff.utilities.Constants.CLOTHES;
import static com.bkit.skinff.utilities.Constants.FILE_FREE_FIRE;
import static com.bkit.skinff.utilities.Constants.FILE_FREE_FIRE_MAX;
import static com.bkit.skinff.utilities.Constants.INITIAL_URI;
import static com.bkit.skinff.utilities.Constants.KEY_IMAGE;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT_MAX;
import static com.bkit.skinff.utilities.Constants.KEY_WEAPON;
import static com.bkit.skinff.utilities.Constants.KEY_WEAPON_MAX;
import static com.bkit.skinff.utilities.Constants.NAME_CLOTHES;
import static com.bkit.skinff.utilities.Constants.NAME_RESCONF;
import static com.bkit.skinff.utilities.Constants.OPEN_DOCUMENT_TREE;
import static com.bkit.skinff.utilities.Constants.RESCONF;
import static com.bkit.skinff.utilities.Constants.TIME_DELETE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bkit.skinff.databinding.ActivityAdminBinding;
import com.bkit.skinff.firebase.UploadStorage;
import com.bkit.skinff.firebase.UploadFirestore;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {


    private ActivityAdminBinding binding;
    Uri uriImage;
    String model = "";
    String type = "";
    String time = "";
    String nameFileChoose = "";
    private RadioGroup rgFileName;
    private RadioButton rbFileName;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Uri> fileDataChose = new HashMap<>();
    UploadStorage storage = UploadStorage.getInstance();
    UploadFirestore firestore = UploadFirestore.getInstance();

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initMain();
    }
    // first initial
    private void initMain() {
        binding.btChooseImage.setOnClickListener(v -> {
            choseImage();
        });
        binding.btTime.setOnClickListener(v -> {
            getTime();
        });
        binding.btnUploadFile.setOnClickListener(v -> {
            upload();
        });
        binding.btnChooseFile.setOnClickListener(v -> {
            chooseFile();
        });
        binding.btnUploadFirestore.setOnClickListener(v -> {
            uploadToFirestore();
        });
        binding.btnGetLinkImage.setOnClickListener(v -> {
            getLinkImage();
        });
        binding.btnPreview.setOnClickListener(v -> {
            showData();
        });
        binding.btUpdateFileRoot.setOnClickListener(v -> {
            updateFileRoot();
        });
    }

    // open activity preview
    public void showData() {
        Intent intent = new Intent(this, AdminPreviewActivity.class);
        startActivity(intent);
    }
    // send request to gallery to able access to image, to get path image
    private void choseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOSE_IMAGE);
    }
    // handle time
    // use time is parameter
    private void getTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int m = month + 1;
                        String dateUpdate = dayOfMonth + "-" + m + "-" + year;
                        binding.tvGetTime.setText(dateUpdate);
                    }
                },
                year, month, dayOfMonth);

        datePickerDialog.show();
    }

    // send a request t device to can open archive dialo
    public void requestSdcardAccessPermission() {
        // to can access android/data
        Intent intent = new Intent(OPEN_DOCUMENT_TREE);
        Uri parse = null;
        if (model.equals("ff")) {
            parse = Uri.parse(FILE_FREE_FIRE);
        }
        if (model.equals("ffmax")) {
            parse = Uri.parse(FILE_FREE_FIRE_MAX);
        }
        // will be access first when opening storage
        intent.putExtra(INITIAL_URI,
                DocumentsContract.buildDocumentUriUsingTree(parse, DocumentsContract.getTreeDocumentId(parse)));
        startActivityForResult(intent, CHOSE_FILE);
    }
    // receive result is path to file free fire and uri image
    @SuppressLint("WrongConstant")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent resultData) {
        if (resultCode == Activity.RESULT_OK && requestCode == CHOSE_FILE) {
            assert resultData != null;
            Uri uri = resultData.getData();
            binding.btnUploadFile.setTag(BUTTON_TAG);
            getContentResolver().takePersistableUriPermission(uri, resultData.getFlags() & 3);
            getUriFile(uri);
        }

        if (requestCode == CHOSE_IMAGE && resultCode == Activity.RESULT_OK) {
            assert resultData != null;
            Uri uri = resultData.getData();
            binding.ivPreview.setImageURI(uri);
            binding.btnUploadFile.setTag("");
            uriImage = uri;
        }
        super.onActivityResult(requestCode, resultCode, resultData);
    }


    // get uri from device
    // save result as type Hashmap (String, uri) , with key is name file and value is path
    private void getUriFile(Uri treeUri) {
        getContentResolver().takePersistableUriPermission(treeUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        DocumentFile pickedDir = DocumentFile.fromTreeUri(getApplicationContext(), treeUri);
        assert pickedDir != null;
        DocumentFile[] ddff = pickedDir.listFiles();
        for (DocumentFile df : ddff) {
            String name = df.getName();
            Uri uri = df.getUri();
            assert name != null;
            Log.d("fjalir", type);
            if (type.equals("gun")) {
                if (name.contains(NAME_RESCONF)) {
                    fileDataChose.put(name, uri);
                }
            } else if (type.equals("outfit")) {
                if (name.contains(NAME_CLOTHES)) {
                    fileDataChose.put(name, uri);
                }
            }
        }
        createRadioButtonToChooseFileName();

    }

    // create radio button, numbers button equal corresponding number file
    // at he same time update name of file
    private void createRadioButtonToChooseFileName() {
        rgFileName = new RadioGroup(this);
        rgFileName.setOrientation(LinearLayout.VERTICAL);
        rgFileName.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        RadioGroup.LayoutParams layoutParams;

        for (String namerb : fileDataChose.keySet()) {
            rbFileName = new RadioButton(this);
            rbFileName.setText(namerb);
            layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
            rgFileName.addView(rbFileName, layoutParams);
        }
        binding.llRadioGroup.addView(rgFileName);
        rgFileName.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkButton = rgFileName.findViewById(checkedId);
                String name = checkButton.getText().toString();
                binding.tvNameFileChose.setText(name);
                if (name.contains(NAME_CLOTHES)) {
                    if (model.equals("ff")) {
                        firestore.updateName(KEY_OUTFIT, name, binding.pbUpload);
                    }
                    if (model.equals("ffmax")) {
                        firestore.updateName(KEY_OUTFIT_MAX, name, binding.pbUpload);
                    }
                } else if (name.contains(NAME_RESCONF)) {
                    if (model.equals("ff")) {
                        firestore.updateName(KEY_WEAPON, name, binding.pbUpload);
                    }
                    if (model.equals("ffmax")) {
                        firestore.updateName(KEY_WEAPON_MAX, name, binding.pbUpload);
                    }
                }
            }
        });
    }
    // upload data to storage
    // upload file config game
    // uploaf file image
    private void upload() {
        time = binding.tvGetTime.getText().toString().trim();
        storage.putFileFirebaseStorage(uriImage, model, type, time, binding.pbUpload, KEY_IMAGE);
        String name = binding.tvNameFileChose.getText().toString().trim();
        Uri uri = fileDataChose.get(name);
        storage.putFileFirebaseStorage(uri, model, type, time, binding.pbUpload, name);
    }
    // update file use to "delete skin"
    private void updateFileRoot() {
        time = TIME_DELETE;
        String name = binding.tvNameFileChose.getText().toString().trim();
        Uri uri = fileDataChose.get(name);
        storage.putFileFirebaseStorage(uri, model, type, time, binding.pbUpload, name);
    }
    // get link image, image saved in storage
    private void getLinkImage() {
        storage.getUriImage(model, type, time, binding.tvLinkImage);
    }
    // upload data to firestore
    // upload in collection "file"
    private void uploadToFirestore() {
        String name = binding.etName.getText().toString().trim();
        String linkImage = binding.tvLinkImage.getText().toString().trim();
        firestore.uploadToFirestore(model, name, time, type, binding.pbUpload, linkImage, binding.tvNameFileChose.getText().toString());
    }
    // handle event click radio button
    private void chooseFile() {
        if (binding.rbFfmax.isChecked()) {
            model = "ffmax";
        }
        if (binding.rbFf.isChecked()) {
            model = "ff";
        }
        if (binding.rbGun.isChecked()) {
            type = "gun";
        }
        if (binding.rbOutfit.isChecked()) {
            type = "outfit";
        }
        requestSdcardAccessPermission();
    }
}