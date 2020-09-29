package apps.njl.gosafe;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import apps.njl.gosafe.Model.Users;


public class HomeActivity extends AppCompatActivity {
    private Button registerButton,loginButton;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        registerButton =(Button) findViewById(R.id.main_register_btn);
        loginButton =(Button) findViewById(R.id.main_login_btn);
        loadingBar= new ProgressDialog(this);

        Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, RegisterActivity.class);
                startActivity(intent);


            }
        });


    }

    private void AllowAccess(final String username, final String password) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Users").child(username).exists())
                {
                    Users usersData= dataSnapshot.child("Users").child(username).getValue(Users.class);

                    if(usersData.getPhone().equals(username))
                    {
                        if(usersData.getPassword().equals(password))
                        {
                            Toast.makeText(HomeActivity.this, "logged in Successfully..", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(HomeActivity.this,MainMenu.class);
                            startActivity(intent);

                        }
                        else
                        {
                            Toast.makeText(HomeActivity.this, "Invalid Password..", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }

                }
                else
                {
                    Toast.makeText(HomeActivity.this, "Account with this "+ username+" id do not exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
