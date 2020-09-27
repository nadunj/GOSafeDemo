package apps.njl.gosafe;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

//    private boolean isFirsttime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        /*final SharedPreferences sharedPref = getSharedPreferences("GOSafe_settings", 0);
        final SharedPreferences.Editor editor = sharedPref.edit();
        isFirsttime = sharedPref.getBoolean("intro", true);*/

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
         // Intent intent = new Intent(SplashScreen.this,LoginActivity.class);
                    Intent intent = new Intent(SplashScreen.this,MainMenu.class);
                    /*if(isFirsttime) {
                        intent = new Intent(SplashScreen.this, Intro.class);
                        editor.putBoolean("intro",false);
                        editor.commit();
                    }
                    else{
                        intent = new Intent(SplashScreen.this,LoginActivity.class);
                    }*/

                    startActivity(intent);
                }
            }
        };

        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
