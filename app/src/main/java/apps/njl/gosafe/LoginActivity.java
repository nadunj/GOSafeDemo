package apps.njl.gosafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import apps.njl.gosafe.Model.Users;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import io.paperdb.Paper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.AlgorithmParameterGenerator;

import apps.njl.gosafe.Model.Users;

public class LoginActivity extends AppCompatActivity {
    private EditText InputUsername,InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private  String parentDbName="Users";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = (Button) findViewById(R.id.login_btn);
        InputUsername = (EditText) findViewById(R.id.login_username);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        loadingBar= new ProgressDialog(this);


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });


    }

    private void loginUser() {
        String username = InputUsername.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Please Enter the Username..", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Enter the Password..", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait while we are checking the Credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(username,password);


        }
    }

    private void AllowAccessToAccount(final String username, final String password) {


        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentDbName).child(username).exists())
                {
                    Users usersData= dataSnapshot.child(parentDbName).child(username).getValue(Users.class);

                    if(usersData.getUsername().equals(username))
                    {
                        if(usersData.getPassword().equals(password))
                        {
                           if (parentDbName.equals("Users"))
                            {
                                Toast.makeText(LoginActivity.this, "logged in Successfully..", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this,MainMenu.class);
                                startActivity(intent);
                            }

                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Invalid Password..", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }

                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Account with this "+ username +"  do not exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
