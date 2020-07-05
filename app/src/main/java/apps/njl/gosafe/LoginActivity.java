package apps.njl.gosafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import REST_Controller.RESTClient;
import REST_Controller.RESTInterface;


public class LoginActivity extends AppCompatActivity {

    private EditText usernametxt, passwordtxt;
    private TextView signuptxt;
    private Button guest;
    private RESTInterface restInterface;
    private MaterialDialog dialog;
    private ConstraintLayout layout;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        passwordtxt = findViewById(R.id.PasswordTxt);
        usernametxt = findViewById(R.id.UsernameTxt);
        signuptxt = findViewById(R.id.txtsignup);
        layout = findViewById(R.id.layout_login);
        guest = findViewById(R.id.btn_guest);

        sharedPreferences = getSharedPreferences("iSafe_settings",0);
        editor = sharedPreferences.edit();

        editor.putBoolean("guest",true);

        restInterface = RESTClient.getInstance().create(RESTInterface.class);


        signuptxt.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        }));

        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainMenu.class));
            }
        });

    }

    public void login(View view) {
        if (nonEmpty(usernametxt, passwordtxt)) {
            /*startActivity(new Intent(LoginActivity.this, MainMenu.class));*/
            setProgressDialog("Please Wait Until Verification");
            requestLogin(usernametxt.getText().toString(), passwordtxt.getText().toString());
        }

    }

    /**
     * Check non-empty fields. if a empty field found --> set an error message
     */
    public boolean nonEmpty(EditText... editTexts) {
        for (EditText editText : editTexts) {
            if (TextUtils.isEmpty(editText.getText())) {
                editText.setError("Please Input Required Fields");
                return false;
            }
        }
        return true;
    }

    /**
     * Request authentication through REST API
     */
    private void requestLogin(String username, String password) {
        /*Call<LoginResponse> call = restInterface.loginUser(new LoginRequest(username, password));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body() != null) {
                    if (response.code() == 200) {
                        LoginResponse loginResponse = response.body();
                        if (!TextUtils.isEmpty(loginResponse.getAccessToken())) {
                            Intent intent = new Intent(getApplicationContext(),MainMenu.class);
                            intent.putExtra("token",loginResponse.getAccessToken());
                            startActivity(intent);
                        } else {
                            setMessage("Invalid Username or Password");
                        }

                    } else {
                        setMessage("Network Error : " + response.code());
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                setMessage("Network error! Please check your internet connection");
                dialog.dismiss();
            }
        });*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void setProgressDialog(String message) {
        dialog = new MaterialDialog.Builder(LoginActivity.this)
                .content(message)
                .cancelable(false)
                .progress(true, 0)
                .show();
    }

    private void setMessage(String message) {
        Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}
