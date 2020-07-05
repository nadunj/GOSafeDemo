package apps.njl.gosafe;

import android.content.Intent;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import REST_Controller.RESTClient;
import REST_Controller.RESTInterface;
import REST_Controller.UserRegister;

public class SignUpActivity extends AppCompatActivity {

    private RESTInterface restInterface;
    private LinearLayout layout;
    private boolean registered = false;
    private StorageReference mStorageRef;
    private static final String TAG = SignUpActivity.class.getSimpleName();
    int i;
    private Intent intent;
    Button btn_signup;
    EditText username, firstname, lastname, nic, dob, residence, pwd, telephoneNumber, email, licenseNo, dateofissue, dateofexpire;
    private String token;
    private MaterialDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        layout = findViewById(R.id.layout_register);
        btn_signup = findViewById(R.id.btn_signup);
        firstname = findViewById(R.id.firstname);
        username = findViewById(R.id.username);
        lastname = findViewById(R.id.lastname);
        nic = findViewById(R.id.nic);
        dob = findViewById(R.id.dob);
        residence = findViewById(R.id.residence);
        pwd = findViewById(R.id.pwd);
        telephoneNumber = findViewById(R.id.telephoneNumber);

        email = findViewById(R.id.email);
        licenseNo = findViewById(R.id.licenseNo);
        dateofissue = findViewById(R.id.dateofissue);
        dateofexpire = findViewById(R.id.dateofexpire);
        mStorageRef = FirebaseStorage.getInstance().getReference();


        restInterface = RESTClient.getInstance().create(RESTInterface.class);

        /**fields are nonempty-> upload to sever**/
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(registered)
                    startActivity(intent);
                else {
                    if(nonEmpty(username,firstname,lastname,nic,dob,residence,pwd,telephoneNumber,email,licenseNo,dateofissue,dateofexpire)){
                        setProgressDialog("User is registering");
                        UserRegister userRegister = new UserRegister();
                        userRegister.setUsername(username.getText().toString());
                        userRegister.setFirstName(firstname.getText().toString());
                        userRegister.setLastName(lastname.getText().toString());
                        userRegister.setNic(nic.getText().toString());
                        userRegister.setPassword(pwd.getText().toString());
                        userRegister.setDob(dob.getText().toString());
                        userRegister.setLicense_num(licenseNo.getText().toString());
                        userRegister.setAddress(residence.getText().toString());
                        userRegister.setPhonenumber(Integer.parseInt(telephoneNumber.getText().toString()));
                        userRegister.setEmail(email.getText().toString());
                        userRegister.setDateOfIssueLicense(dateofissue.getText().toString());
                        userRegister.setDateOfExpireLicense(dateofexpire.getText().toString());

                        /*Call<String> call = restInterface.newUserRegister(userRegister);
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if (response.code() == 200 && response.body()!=null) {
                                    intent = new Intent(getApplicationContext(),ImageUploadActivity.class);
                                    intent.putExtra("code",response.body());
                                    registered = true;
                                    btn_signup.setText("Next Step");
                                }else if(response.code()==400)
                                    setMessage("Incorrect form of data found! Please check and try again");
                                dialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                setMessage("Error : "+t.getMessage());
                                dialog.dismiss();
                            }
                        });*/

                    }
                    else
                        setMessage("Empty fields found! Please fill all fields to continue");
                }
            }
        });


    }

    /**check nonempty fields**/
    public boolean nonEmpty(EditText... editTexts) {

        for (EditText editText : editTexts) {
            if (TextUtils.isEmpty(editText.getText())) {
                System.out.println("false");

                editText.setError("Please Input Text");
                return false;
            }
        }
        return true;

    }

    private void setProgressDialog(String message) {
        dialog = new MaterialDialog.Builder(SignUpActivity.this)
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

