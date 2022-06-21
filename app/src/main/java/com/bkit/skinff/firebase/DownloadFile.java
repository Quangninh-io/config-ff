package com.bkit.skinff.firebase;

import static com.bkit.skinff.utilities.Constants.ACTIVE_SUCCESS;
import static com.bkit.skinff.utilities.Constants.DELETE_SUCCESS;
import static com.bkit.skinff.utilities.Constants.NAME_IMAGE;
import static com.bkit.skinff.utilities.Constants.RESCONF;
import static com.bkit.skinff.utilities.Constants.TIME_DELETE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bkit.skinff.R;
import com.bkit.skinff.activity.UserDetailActivity;
import com.bkit.skinff.freefire.WriteFileToFreeFire;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.concurrent.atomic.DoubleAccumulator;

public class DownloadFile {

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();

    private static DownloadFile instance;

    public static DownloadFile getInstance() {
        if (instance == null) {
            return new DownloadFile();
        }
        return instance;
    }

    public void downLoadFile(ProgressBar pb, String time, Uri uri, Context context, String model, String type, String nameFile) {
        String uriLocal = model + "/" + type + "/" + time;
        StorageReference imgRef = storageReference.child(uriLocal + "/" + nameFile);
        final long ONE_MEGABYTE = 3000000;

        pb.setVisibility(View.VISIBLE);
        imgRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            pb.setVisibility(View.INVISIBLE);
            WriteFileToFreeFire.getInstance().writeFile(uri, context, bytes);
            Log.d("byte", String.valueOf(bytes));
            if (!time.equals(TIME_DELETE)) {
                showDialog(context,ACTIVE_SUCCESS,model);
            }else{
                showDialog(context,DELETE_SUCCESS,model);
            }
        }).addOnFailureListener(e -> {
            pb.setVisibility(View.INVISIBLE);
            Log.d("Fail something", "fail");
        });
    }

    private void showDialog(Context context, String content , String choseModel) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.alert_move_free_fire);
        Button btnMoveFf = dialog.findViewById(R.id.bt_move_free_fire);
        Button btnCancel = dialog.findViewById(R.id.bt_cancel);
        TextView tvContent = dialog.findViewById(R.id.tv_content);
        tvContent.setText(content);
        btnMoveFf.setOnClickListener(v -> {
            if (choseModel.equals("ff")){
                context.startActivity(context.getPackageManager().getLaunchIntentForPackage("com.dts.freefireth"));
            }
            if(choseModel.equals("ffmax")){
                context.startActivity(context.getPackageManager().getLaunchIntentForPackage("com.dts.freefiremax"));
            }
        });
        btnCancel.setOnClickListener(v -> {
            dialog.cancel();
        });
        dialog.show();
    }
}
