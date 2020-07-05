package apps.njl.gosafe;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    private Switch switch_blackspot, switch_criticalData, switch_trafficData, switch_speedLimit;
    private Switch switch_voiceAss, switch_notification;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SeekBar seekBar;
    private TextView txt_radius;

    private boolean isBlackspotOn = true, isCriticalOn = true, isTrafficOn = true, isSpeedOn = true;
    private boolean isVoiceOn = true, isNotificationOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);

        Init();
        updateUI();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txt_radius.setText("Nearby Incident Radius (" + seekBar.getProgress() + " m)");
                editor.putInt("radius",seekBar.getProgress());
                editor.commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        switch_blackspot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    editor.putBoolean("blackspot",true);
                else
                    editor.putBoolean("blackspot",false);
                editor.commit();
            }
        });

        switch_criticalData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    editor.putBoolean("critical",true);
                else
                    editor.putBoolean("critical",false);
                editor.commit();
            }
        });

        switch_speedLimit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    editor.putBoolean("speed",true);
                else
                    editor.putBoolean("speed",false);
                editor.commit();
            }
        });

        switch_trafficData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    editor.putBoolean("traffic",true);
                else
                    editor.putBoolean("traffic",false);
                editor.commit();
            }
        });

        switch_voiceAss.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    editor.putBoolean("voice",true);
                else
                    editor.putBoolean("voice",false);
                editor.commit();
            }
        });

        switch_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    editor.putBoolean("notification",true);
                else
                    editor.putBoolean("notification",false);
                editor.commit();
            }
        });
    }


    private void Init() {
        switch_blackspot = findViewById(R.id.switch_blackspot);
        switch_criticalData = findViewById(R.id.switch_critical);
        switch_trafficData = findViewById(R.id.switch_traffic);
        switch_speedLimit = findViewById(R.id.switch_speedlimit);
        switch_voiceAss = findViewById(R.id.switch_voiceAssistant);
        switch_notification = findViewById(R.id.switch_notification);
        txt_radius = findViewById(R.id.textView11);
        seekBar = findViewById(R.id.seek_radius);

        sharedPreferences = getSharedPreferences("iSafe_settings",0);
        editor = sharedPreferences.edit();
    }

    private void updateUI(){
        isBlackspotOn = sharedPreferences.getBoolean("blackspot",true);
        isCriticalOn = sharedPreferences.getBoolean("critical",true);
        isTrafficOn = sharedPreferences.getBoolean("traffic",true);
        isSpeedOn = sharedPreferences.getBoolean("speed",true);

        isVoiceOn = sharedPreferences.getBoolean("voice",true);
        isNotificationOn = sharedPreferences.getBoolean("notification",true);

        if(sharedPreferences.getBoolean("blackspot",true))
            switch_blackspot.setChecked(true);
        else
            switch_blackspot.setChecked(false);

        if(sharedPreferences.getBoolean("critical",true))
            switch_criticalData.setChecked(true);
        else
            switch_criticalData.setChecked(false);

        if(sharedPreferences.getBoolean("traffic",true))
            switch_trafficData.setChecked(true);
        else
            switch_trafficData.setChecked(false);

        if(sharedPreferences.getBoolean("speed",true))
            switch_speedLimit.setChecked(true);
        else
            switch_speedLimit.setChecked(false);

        if(sharedPreferences.getBoolean("voice",true))
            switch_voiceAss.setChecked(true);
        else
            switch_voiceAss.setChecked(false);

        if(sharedPreferences.getBoolean("notification",true))
            switch_notification.setChecked(true);
        else
            switch_notification.setChecked(false);

        seekBar.setProgress(sharedPreferences.getInt("radius",0));
        txt_radius.setText("Nearby Incident Radius (" + seekBar.getProgress() + " m)");
    }
}
