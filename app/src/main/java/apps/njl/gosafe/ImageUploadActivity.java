package apps.njl.gosafe;

/***Activity for capture Profile picture and License Card image and upload to firebase db**/
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import REST_Controller.RESTClient;
import REST_Controller.RESTInterface;

public class ImageUploadActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 120;
    private static final int REQUEST_IMAGE_CAPTURE = 100;
    private ImageView previewProfile,previewnic;
    int i = 0;
    private Uri uri;
    private LinearLayout layout;
    private boolean isProfileSet = false, isIdSet = false, allDone = false;
    private Button btn_upload;
    private String userCode="";
    private StorageReference mStorageRef;
    private MaterialDialog dialog;
    private String profileURL = "";
    private String nicURL = "";
    private RESTInterface restInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        previewProfile=findViewById(R.id.img_profile_upload);
        previewnic=findViewById(R.id.img_license_upload);
        btn_upload = findViewById(R.id.btn_upload_images);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        userCode = getIntent().getStringExtra("code");
        layout = findViewById(R.id.layout_image_upload);
        restInterface = RESTClient.getInstance().create(RESTInterface.class);

        previewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=0;
                if(!isProfileSet)
                    showAlert("Profile Image");
            }

        });
        previewnic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=1;
                if(!isIdSet)
                    showAlert("Image Of NIC");
            }

        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isProfileSet && !isIdSet)
                    setMessage("Please capture profile & National ID images");
                else if(!isProfileSet)
                    setMessage("Please capture profile image");
                else if(!isIdSet)
                    setMessage("Please capture National ID image");
                else{
                    if(allDone)
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    else
                        updateProfileData();
                }

            }
        });
    }

    /**choose image from gallery**/
    private void chooseImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select Picture"),PICK_IMAGE_REQUEST);

    }

    /**show alert box choose or capture image**/
    public void showAlert(String string)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(string);
        builder.setMessage("Capture a profile picture or Choose from gallery?");
        builder.setPositiveButton("Capture Image", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dispatchTakePictureIntent();

            }
        });
        builder.setNegativeButton("Choose from Galery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                chooseImage();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /** Set images to bitmap**/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode,resultCode,data);
        if(i == 0){
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                uri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    previewProfile.setImageBitmap(bitmap);
                    isProfileSet = true;

                    setProgressDialog("Uploading Profile Image");
                    imageUpload(previewProfile,userCode,"Profile");

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                //ImageURL=saveToInternalStorage(imageBitmap);
                previewProfile.setImageBitmap(imageBitmap);
                isProfileSet = true;

                setProgressDialog("Uploading Profile Image");
                imageUpload(previewProfile,userCode,"Profile");

            }


        }
        if(i==1) {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                uri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    previewnic.setImageBitmap(bitmap);
                    isIdSet = true;
                    setProgressDialog("Uploading NID Image");
                    imageUpload(previewProfile,userCode,"NID");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                //ImageURL=saveToInternalStorage(imageBitmap);
                previewnic.setImageBitmap(imageBitmap);
                isIdSet = true;
                setProgressDialog("Uploading NID Image");
                imageUpload(previewProfile,userCode,"NID");
            }
        }
    }

    /**upload single user profile pic and nic**/
    private void imageUpload(ImageView imageView, String token, final String type){
        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mStorageRef.child("User").child(token).child(type).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                setMessage("Image Upload failed!");
                dialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                /*if(type.equals("Profile"))
                    profileURL = taskSnapshot.getDownloadUrl().toString();
                else
                    nicURL = taskSnapshot.getDownloadUrl().toString();
                dialog.dismiss();*/
            }
        });
    }

    /**progress dialog box**/
    private void setProgressDialog(String message) {
        dialog = new MaterialDialog.Builder(ImageUploadActivity.this)
                .content(message)
                .cancelable(false)
                .progress(true, 0)
                .show();
    }

    private void setMessage(String message){
        Snackbar snackbar = Snackbar.make(layout,message,Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    /**upload to severdb**/
    private void updateProfileData(){

        /*if(isProfileSet && isIdSet){
            Call<Void> call = restInterface.updateProfileData(new UploadURLRequest(nicURL,userCode,profileURL));
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.code()==200){
                        allDone = true;
                        setMessage("Registration Complete!");
                        btn_upload.setText("Goto Login");
                    }
                    else
                        setMessage("Registration failed. Please try again");
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    setMessage("Error : " + t.getMessage());
                }
            });
        }*/

    }
}
