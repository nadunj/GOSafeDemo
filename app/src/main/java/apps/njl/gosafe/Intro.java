package apps.njl.gosafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.WindowManager;

import com.github.paolorotolo.appintro.AppIntro;

import net.ralphpina.permissionsmanager.PermissionsManager;
import net.ralphpina.permissionsmanager.PermissionsResult;

import rx.functions.Action1;

/**
 * Intro for the first user to show the capabilities of the application
 */

public class Intro extends AppIntro {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setZoomAnimation();
        showSeparator(false);

        sharedPreferences = getSharedPreferences("iSafe_settings", 0);
        editor = sharedPreferences.edit();
        editor.putInt("radius", 1000);
        editor.commit();

        /*addSlide(AppIntroFragment.newInstance("",
                "Community based Incident management & Navigation app",
                R.drawable.icon,
                Color.parseColor("#049704")));

        addSlide(AppIntroFragment.newInstance("Improve Driving Safety",
                "Dynamic Range Calculation\n" +
                        "Voice & Dialog based Notifications",
                R.drawable.slide2,
                Color.parseColor("#049704")));

        addSlide(AppIntroFragment.newInstance("Quality of Driving",
                "Improve your driving skills\n" + "Check your driving quality after each journey",
                R.drawable.slide3,
                Color.parseColor("#049704")));

        addSlide(AppIntroFragment.newInstance("Earn Points!",
                "25 points -> New Real-time incident\n"
                        + "50 points -> Uploading incident photograph",
                R.drawable.slide4,
                Color.parseColor("#049704")));

        addSlide(AppIntroFragment.newInstance("User Permission",
                "Allow Camera, Location & Internet Access",
                R.drawable.slide5,
                Color.parseColor("#049704")));*/

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        //Set Permission
        getPermission();

    }

    /**Set runtime permission**/
    private void getPermission() {
        try {
            PermissionsManager.init(this);
            PermissionsManager.get()
                    .requestLocationPermission()
                    .subscribe(new Action1<PermissionsResult>() {
                        @Override
                        public void call(PermissionsResult permissionsResult) {
                            if (permissionsResult.isGranted()) { // always true pre-M
                                // do whatever
                                startActivity(new Intent(Intro.this, LoginActivity.class));
                                finish();
                            }
                            if (permissionsResult.hasAskedForPermissions()) { // false if pre-M
                                // do whatever
                            }
                        }
                    });
        } catch (Exception e) {
        }
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        //Set Permission
        getPermission();
    }

}
