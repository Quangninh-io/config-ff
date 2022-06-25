package com.bkit.skinff.firebase;

import static com.bkit.skinff.utilities.Constants.ACTIVE_SUCCESS;
import static com.bkit.skinff.utilities.Constants.DELETE_SUCCESS;
import static com.bkit.skinff.utilities.Constants.TIME_DELETE;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bkit.skinff.R;
import com.bkit.skinff.freefire.WriteFileToFreeFire;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

// use for user can download the desire file
public class DownloadFile {
    private final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private final StorageReference storageReference = firebaseStorage.getReference();
    private static DownloadFile instance;
    public static DownloadFile getInstance() {
        if (instance == null) {
            return new DownloadFile();
        }
        return instance;
    }
    // handle download file in storage
    // in proceed download, display progress bar, if download success, turn off progress bar
    // and open dialog
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
                //showDialog(context, R.string.active_success,model);
            }else{
                Log.d("delete", "success");
                showDialog(context,DELETE_SUCCESS,model);
                //showDialog(context, String.valueOf(R.string.delete_success),model);
            }
        }).addOnFailureListener(e -> {
            pb.setVisibility(View.INVISIBLE);
            Log.d("Fail something", "fail");
        });
    }
    // give 2 options for user, open game or exit
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
