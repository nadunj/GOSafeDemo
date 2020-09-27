package apps.njl.gosafe;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;

import REST_Controller.ProfileResponse;
import REST_Controller.RESTClient;
import REST_Controller.RESTInterface;

public class ProfileActivity extends AppCompatActivity {

    private RESTInterface restInterface;
    private MaterialDialog dialog;
    private String token;
    private ConstraintLayout layout;

    private ImageView profilePic;
    private TextView txt_fName, txt_nicNumber, txt_dob, txt_address, txt_tpNumber, txt_email, txt_lic_no, txt_issueDate, txt_expData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        requestProfileData();

    }

    private void setProgressDialog(String message) {
        dialog = new MaterialDialog.Builder(ProfileActivity.this)
                .content(message)
                .cancelable(false)
                .progress(true, 0)
                .show();
    }

    /**Send token and get related profile data**/
    private void requestProfileData(){
        setProgressDialog("Importing profile data");
        /*Call<ProfileResponse> call = restInterface.getProfileData("Bearer " + token);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if(response.code()==200)
                    updateUI(response.body());
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                setPopupMessage("Error : " + t.getMessage());
                dialog.dismiss();
            }
        });*/
    }

    private void setPopupMessage(String message) {
        Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void updateUI(ProfileResponse profileResponse){
        Glide.with(this).load(profileResponse.getImageOfDriverUrl()).into(profilePic);
        txt_fName.setText(profileResponse.getUsername());
        txt_address.setText(profileResponse.getAddress());
        txt_email.setText(profileResponse.getEmail());

    }

    private void init(){
        token = getIntent().getStringExtra("token");
        restInterface = RESTClient.getInstance().create(RESTInterface.class);
        layout = findViewById(R.id.layout_profile);
        txt_fName = findViewById(R.id.fname);
        txt_address = findViewById(R.id.inputresidence);
        txt_email = findViewById(R.id.inputemail);

    }
}
