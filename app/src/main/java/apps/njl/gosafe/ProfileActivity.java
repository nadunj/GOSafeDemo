package apps.njl.gosafe;


import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import REST_Controller.ProfileResponse;
import REST_Controller.RESTClient;
import REST_Controller.RESTInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private RESTInterface restInterface;
    private MaterialDialog dialog;
    private String token;
    private ConstraintLayout layout;


    private TextView inputname, inputresidence , inputemail , inputphonenumber;

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
      inputname.setText(BaseApplication.user.getUsername());
      inputemail.setText(BaseApplication.user.getEmail());
      inputresidence.setText(BaseApplication.user.getResidence());
      inputphonenumber.setText(BaseApplication.user.getPhone());
    }

    private void setPopupMessage(String message) {
        Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void updateUI(ProfileResponse profileResponse){

        inputname.setText(profileResponse.getUsername());
        inputresidence.setText(profileResponse.getAddress());
        inputphonenumber.setText(profileResponse.getPhonenumber());
        inputemail.setText(profileResponse.getEmail());

    }

    private void init(){
        token = getIntent().getStringExtra("token");
        restInterface = RESTClient.getInstance().create(RESTInterface.class);
        layout = findViewById(R.id.layout_profile);

        inputname = findViewById(R.id.inputname);
        inputresidence = findViewById(R.id.inputresidence);
        inputphonenumber = findViewById(R.id.inputphonenumber);
        inputemail = findViewById(R.id.inputemail);


    }
}
