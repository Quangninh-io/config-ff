package com.bkit.skinff.fragment.user;

import static com.bkit.skinff.utilities.Constants.CHECK_FF_EXIST;
import static com.bkit.skinff.utilities.Constants.CHECK_FF_MAX_EXIST;
import static com.bkit.skinff.utilities.Constants.CHOSE_FILE;
import static com.bkit.skinff.utilities.Constants.COLLECTION;
import static com.bkit.skinff.utilities.Constants.FILE_FREE_FIRE;
import static com.bkit.skinff.utilities.Constants.FILE_FREE_FIRE_MAX;
import static com.bkit.skinff.utilities.Constants.FREE_FIRE_MAX_29;
import static com.bkit.skinff.utilities.Constants.FREE_FIRE_TH_29;
import static com.bkit.skinff.utilities.Constants.INITIAL_URI;
import static com.bkit.skinff.utilities.Constants.INTENT_CHOSE_MODEL;
import static com.bkit.skinff.utilities.Constants.INTENT_DETAIL;
import static com.bkit.skinff.utilities.Constants.INTENT_MODEL;
import static com.bkit.skinff.utilities.Constants.INTENT_NAME;
import static com.bkit.skinff.utilities.Constants.INTENT_OUTFIT;
import static com.bkit.skinff.utilities.Constants.INTENT_WEAPON;
import static com.bkit.skinff.utilities.Constants.KEY_CHOSE_MODEL;
import static com.bkit.skinff.utilities.Constants.KEY_IMAGE;
import static com.bkit.skinff.utilities.Constants.KEY_MODEL;
import static com.bkit.skinff.utilities.Constants.KEY_NAME;
import static com.bkit.skinff.utilities.Constants.KEY_NAME_FILE;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT;
import static com.bkit.skinff.utilities.Constants.KEY_TIME;
import static com.bkit.skinff.utilities.Constants.KEY_TYPE;
import static com.bkit.skinff.utilities.Constants.KEY_WEAPON;
import static com.bkit.skinff.utilities.Constants.LIMITED_DATE_SET_NEW;
import static com.bkit.skinff.utilities.Constants.NAME_RESCONF;
import static com.bkit.skinff.utilities.Constants.OPEN_DOCUMENT_TREE;
import static com.bkit.skinff.utilities.Constants.STORAGE_WEAPON;
import static com.bkit.skinff.utilities.Constants.TIME_DELETE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;

import com.bkit.skinff.R;
import com.bkit.skinff.activity.LoginActivity;
import com.bkit.skinff.activity.UserDetailActivity;
import com.bkit.skinff.activity.UserOutfitActivity;
import com.bkit.skinff.activity.UserWeaponActivity;
import com.bkit.skinff.adapter.UserAdapter;
import com.bkit.skinff.databinding.FragmentUserMainBinding;
import com.bkit.skinff.firebase.DownloadFile;
import com.bkit.skinff.listener.ClickSpecificItem;
import com.bkit.skinff.model.FileData;
import com.bkit.skinff.model.Name;
import com.bkit.skinff.sharepreference.GetUri;
import com.bkit.skinff.sharepreference.SaveUri;
import com.bkit.skinff.utilities.ArrangeTime;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class UserMainFragment extends Fragment {
    private FragmentUserMainBinding binding;
    private RadioButton rbFFMax;
    private RadioButton rbFF;
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    UserAdapter adapter;
    Uri uriWeapon, uriOutfit;
    Name fileName;
    String check = "", checkGameExistFF = "", checkGameExistFFmax = "", decideChooseModel = "";
    SaveUri saveUri = SaveUri.getInstance();
    DownloadFile downloadFile = DownloadFile.getInstance();
    GetUri getUri = GetUri.getInstance();
    int dem =0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserMainBinding.inflate(inflater,container,false);
        dem++;
        checkPackageExist();
        initMain();
        return binding.getRoot();
    }

    // if file exist,  proceed to assign value for variable to know have model in devide
    private void checkPackageExist() {
        if (isPackageExisted("com.dts.freefireth")) {
            checkGameExistFF = "ff";
            CHECK_FF_EXIST = checkGameExistFF;
        }
        if (isPackageExisted("com.dts.freefiremax")) {
            checkGameExistFFmax = "ffmax";
            CHECK_FF_MAX_EXIST = checkGameExistFFmax;
        }
    }

    // check package exist or not
    // return true or false
    @SuppressLint("QueryPermissionsNeeded")
    public boolean isPackageExisted(String targetPackage) {
        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = getActivity().getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(targetPackage))
                return true;
        }
        return false;
    }

    // start progress to get uri
    private void requestSdcardAccessPermission(String uri) {
        if (Build.VERSION.SDK_INT >= 23 && Build.VERSION.SDK_INT < 30) {
            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        } else {
            // to can access android/data
            Intent intent = new Intent(OPEN_DOCUMENT_TREE);
            // will be access first when opening storage
            Uri parse = Uri.parse(uri);
            //Uri parse = Uri.parse(FILE_FREE_FIRE);
            intent.putExtra(INITIAL_URI,
                    DocumentsContract.buildDocumentUriUsingTree(parse, DocumentsContract.getTreeDocumentId(parse)));
            startActivityForResult(intent, CHOSE_FILE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                handleGetTotalFile();
            } else {
                Toast.makeText(getActivity(), "Permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void handleGetTotalFile() {
        String path = Environment.getExternalStorageDirectory().toString()+FREE_FIRE_TH_29;
        if(decideChooseModel.equals("ff")){
            path = Environment.getExternalStorageDirectory().toString()+FREE_FIRE_TH_29;
        }
        if(decideChooseModel.equals("ffmax")){
            path = Environment.getExternalStorageDirectory().toString()+FREE_FIRE_MAX_29;
        }
        File directory = new File(path);
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++)
        {
            if(files[i].getName().equals(fileName.getWeapon())){
                uriWeapon = Uri.fromFile(files[i]);
            }
            if(files[i].getName().equals(fileName.getOutfit())){
                uriOutfit = Uri.fromFile(files[i]);
            }
            if(files[i].getName().equals(fileName.getWeaponMax())){
                uriWeapon = Uri.fromFile(files[i]);
            }
            if(files[i].getName().equals(fileName.getOutfitMax())){
                uriOutfit = Uri.fromFile(files[i]);
            }
        }
        saveUri.deleteData(getActivity());
        saveUri.saveData(getActivity(), String.valueOf(uriWeapon), String.valueOf(uriOutfit), decideChooseModel);

    }


    // open to correct to file
    @SuppressLint("WrongConstant")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent resultData) {
        if (resultCode == Activity.RESULT_OK && requestCode == CHOSE_FILE) {
            assert resultData != null;
            Uri uri = resultData.getData();
            getActivity().getContentResolver().takePersistableUriPermission(uri, resultData.getFlags() & 3);
            getUriFromMemory(uri);
        }
        super.onActivityResult(requestCode, resultCode, resultData);
    }

    // function then activity for result called
    // handle read file one by one
    // if meet file have name requirement, proceed to assign for variable(uri weapon and uri outfit)
    // delete value in share preference, and set new value
    //content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata%2Fcom.dts.freefireth%2Ffiles%2Fcontentcache%2FCompulsory%2Fandroid%2Fgameassetbundles%2Fconfig
    private void getUriFromMemory(Uri treeUri) {
        getActivity().getContentResolver().takePersistableUriPermission(treeUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        DocumentFile pickedDir = DocumentFile.fromTreeUri(getActivity(), treeUri);
        assert pickedDir != null;
        DocumentFile[] ddff = pickedDir.listFiles();
        for (DocumentFile df : ddff) {
            String name = df.getName();
            Uri uri = df.getUri();
            assert name != null;
            if (name.contains(fileName.getWeapon())) {
                uriWeapon = uri;
            }
            if (name.contains(fileName.getOutfit())) {
                uriOutfit = uri;
            }
            if (name.contains(fileName.getWeaponMax())) {
                uriWeapon = uri;
            }
            if (name.contains(fileName.getOutfitMax())) {
                uriOutfit = uri;
            }
        }
        saveUri.deleteData(getActivity());
        saveUri.saveData(getActivity(), String.valueOf(uriWeapon), String.valueOf(uriOutfit), decideChooseModel);
    }

    // initial run activity
    // get uri in share preference, if data exist, user don't have to choose uri again
    private void initMain() {

        createNavigationDrawer();

        String stringUriWeapon = getUri.getData(getActivity(), KEY_WEAPON);
        String stringUriOutfit = getUri.getData(getActivity(), KEY_OUTFIT);
        String chooseModelS = getUri.getData(getActivity(), KEY_CHOSE_MODEL);
        Log.d("fadfa",stringUriWeapon);

        if (stringUriOutfit.equals("")) {
            decideOpenDialog();
        } else {
            uriWeapon = Uri.parse(stringUriWeapon);
            uriOutfit = Uri.parse(stringUriOutfit);
            getDataFromFireStore(chooseModelS);
            check = chooseModelS;
            decideChooseModel = chooseModelS;
        }
        fileName = (Name) getActivity().getIntent().getSerializableExtra(INTENT_NAME);

        //showData();
        binding.ivWeapon.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UserWeaponActivity.class);
            intent.putExtra(INTENT_MODEL, check);
            putIntent(intent);
            startActivity(intent);
        });
        binding.ivOutfit.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UserOutfitActivity.class);
            intent.putExtra(INTENT_MODEL, check);
            putIntent(intent);
            startActivity(intent);
        });
//        binding.ivChangeModel.setOnClickListener(v -> {
//            openDialog("Chọn phiên bản free fire");
//        });
//
//        binding.ivLogo.setOnLongClickListener(v -> {
//            startActivity(new Intent(getApplication(), LoginActivity.class));
//            return false;
//        });

        if (check.equals("")) {
            binding.tvDefault.setText(R.string.choose_model);
        }
    }

    private void createNavigationDrawer() {

    }

    // when pass step description, do check model in user's device
    // if just have a model, not open dialog and vise versa
    // then choose model to modify, open folder data/android to user able click and get uri
    private void decideOpenDialog() {
        if (!checkGameExistFF.equals("") && checkGameExistFFmax.equals("")) {
            getDataFromFireStore("ff");
            decideChooseModel = "ff";
            check = "ff";
            binding.tvDefault.setText(R.string.chose_model);
            requestSdcardAccessPermission(FILE_FREE_FIRE);
            saveUri.saveUriModelFF(getActivity(), String.valueOf(uriWeapon), String.valueOf(uriOutfit));
        }
        if (!checkGameExistFFmax.equals("") && checkGameExistFF.equals("")) {
            getDataFromFireStore("ffmax");
            decideChooseModel = "ffmax";
            check = "ffmax";
            binding.tvDefault.setText(R.string.chose_model);
            requestSdcardAccessPermission(FILE_FREE_FIRE_MAX);
            saveUri.saveUriModelFFMAX(getActivity(), String.valueOf(uriWeapon), String.valueOf(uriOutfit));
        }
        if (checkGameExistFFmax.equals("") && checkGameExistFF.equals("")) {
            openDialog(getResources().getString(R.string.not_installed));
        }
        if (!checkGameExistFF.equals("") && !checkGameExistFFmax.equals("")) {
            openDialog(getResources().getString(R.string.installed));
        }

    }

    // if have more 1 model in user's device, dialog will open for user permission choose model will be mod
    // if use do change model by the way click a button set in interface
    // check user's device has the model or not, if exist open space for user able click to get uri
    // if not just display packages in firestore
    // check if user chose uri before, not must user choose again
    public void openDialog(String note) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.alert_choose_model);
        Button btn = dialog.findViewById(R.id.bt_choose_model);
        rbFF = dialog.findViewById(R.id.rb_ff);
        rbFFMax = dialog.findViewById(R.id.rb_ff_max);
        String chooseModel = getUri.getData(getActivity(),KEY_CHOSE_MODEL);
        if(chooseModel.equals("ff")){
            rbFF.setChecked(true);
            rbFFMax.setChecked(false);
        }
        if(chooseModel.equals("ffmax")){
            rbFF.setChecked(false);
            rbFFMax.setChecked(true);
        }
        if (chooseModel.equals("")){
            rbFF.setChecked(true);
            rbFFMax.setChecked(false);
        }
        dialog.setCanceledOnTouchOutside(false);
        TextView tv = dialog.findViewById(R.id.tv);
        tv.setText(note);
        btn.setOnClickListener(v -> {
            if (rbFFMax.isChecked()) {
                getDataFromFireStore("ffmax");
                check = "ffmax";
                decideChooseModel = "ffmax";
                binding.tvDefault.setText(R.string.chose_model);
                if (!checkGameExistFFmax.equals("")) {
                    String uriWeaponS = getUri.getDataFFMAX(getActivity(), KEY_WEAPON);
                    String uriOutitS = getUri.getDataFFMAX(getActivity(), KEY_OUTFIT);
                    if (uriWeaponS.contains(NAME_RESCONF)) {
                        uriWeapon = Uri.parse(uriWeaponS);
                        uriOutfit = Uri.parse(uriOutitS);
                    } else {
                        requestSdcardAccessPermission(FILE_FREE_FIRE_MAX);
                        saveUri.saveUriModelFFMAX(getActivity(), String.valueOf(uriWeapon), String.valueOf(uriOutfit));
                    }
                }
            } else if (rbFF.isChecked()) {
                getDataFromFireStore("ff");
                check = "ff";
                decideChooseModel = "ff";
                binding.tvDefault.setText(R.string.chose_model);
                if (!checkGameExistFF.equals("")) {
                    String uriWeaponS = getUri.getDataFF(getActivity(), KEY_WEAPON);
                    String uriOutitS = getUri.getDataFF(getActivity(), KEY_OUTFIT);
                    if (uriWeaponS.contains(NAME_RESCONF)) {
                        uriWeapon = Uri.parse(uriWeaponS);
                        uriOutfit = Uri.parse(uriOutitS);
                    } else {
                        requestSdcardAccessPermission(FILE_FREE_FIRE);
                        saveUri.saveUriModelFF(getActivity(), String.valueOf(uriWeapon), String.valueOf(uriOutfit));
                    }
                }
            }
            dialog.cancel();
        });
        dialog.show();
    }

    // put data, contain content is
    // uri weapon: file name is resconf in com.dts.freefire
    // uri outfit : file name is clothesslotoverlays in com.dts.freefire
    // put value decide chose which model
    // put specific name with name get from firestore
    private void putIntent(Intent intent) {
        intent.putExtra(INTENT_WEAPON, String.valueOf(uriWeapon));
        intent.putExtra(INTENT_OUTFIT, String.valueOf(uriOutfit));
        intent.putExtra(INTENT_CHOSE_MODEL, decideChooseModel);
        intent.putExtra(INTENT_NAME, fileName);
    }

    //handle delete with model (ff, ff max)
    // to delete, by the way download file set default in storage
    // path to folder default is model/type/3-12-200
    public void delete() {
        String time = TIME_DELETE;
        String model = "";
        if (decideChooseModel.equals("ffmax")) {
            model = "ffmax";
            Log.d("kldh", "run over here");
            Log.d("kldh", String.valueOf(uriWeapon));
            downloadFile.downLoadFile(binding.pbDownload, time, uriWeapon, getContext(), model, STORAGE_WEAPON, fileName.getWeaponMax());
            downloadFile.downLoadFile(binding.pbDownload, time, uriOutfit, getContext(), model, KEY_OUTFIT, fileName.getOutfitMax());
        }
        if (decideChooseModel.equals("ff")) {
            model = "ff";
            downloadFile.downLoadFile(binding.pbDownload, time, uriWeapon, getContext(), model, STORAGE_WEAPON, fileName.getWeapon());
            downloadFile.downLoadFile(binding.pbDownload, time, uriOutfit, getContext(), model, KEY_OUTFIT, fileName.getOutfit());
        }
    }

    // get data from collection "file"
    // pass parameter "model" (ff or ff max) corresponding model, received data from firestore
    // add detail data in array list, prepare to update list to adapter
    private void getDataFromFireStore(String m) {
        db.collection(COLLECTION)
                .whereEqualTo("model", m)
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
                            fileData.add(new FileData(image, model, name, time, type, documentId, nameFile));
                        }
                    }
                    //arrange time
                    ArrangeTime arrangeTime = ArrangeTime.getInstance();
                    Collections.sort(fileData, arrangeTime.comparator);
                    uploadRecycleView(fileData);
                })
                .addOnFailureListener(e -> {
                    Log.d("error read file", "error");
                });
    }

    //set adapter for recycle view
    //do calculated time , if <30 day from real time, set new end else set not new
    //just display data have tag new
    // put package contain detail data to user detail activity
    private void uploadRecycleView(ArrayList<FileData> list) {
        List<FileData> display = new ArrayList<>();
        Instant now = Instant.now(); //current date
        Instant before = now.minus(Duration.ofDays(LIMITED_DATE_SET_NEW));
        Date dateBefore = Date.from(before);
        String thirtyDayAgo = simpleDateFormat.format(dateBefore);
        for (FileData fileData : list) {
            Log.d("time", thirtyDayAgo);
            try {
                Date dateInFirebase = simpleDateFormat.parse(fileData.getTime());
                Date dateInReality = simpleDateFormat.parse(thirtyDayAgo);
                assert dateInFirebase != null;
                if (dateInFirebase.compareTo(dateInReality) > 0) {
                    display.add(fileData);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        adapter = new UserAdapter(display, getContext(), new ClickSpecificItem() {
            @Override
            public void click(FileData fileData) {
                Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                intent.putExtra(INTENT_DETAIL, fileData);
                putIntent(intent);
                startActivity(intent);
            }
        });
        binding.rvItemNew.setAdapter(adapter);
    }

}