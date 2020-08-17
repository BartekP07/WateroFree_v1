package app.watero.waterofree.application.onboarding;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Locale;

import app.watero.waterofree.R;
import app.watero.waterofree.application.main.MainActivity;
import app.watero.waterofree.application.questionnaire.SexChooseActivity;
import app.watero.waterofree.database.MyDBHelper;
import app.watero.waterofree.helpers.App;
import app.watero.waterofree.helpers.UserStorage;


public class SplashActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    int data = 0;
    int questionary = 0;
    MyDBHelper dbHelper;

    private UserStorage userStorage;
    private final String app_version = "1.4.1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userStorage = ((App) getApplication()).getUserStorage();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        setLanguage(userStorage.getLangugae());
        setContentView(R.layout.splash_activity);

        dbHelper = new MyDBHelper(this);

        data = dbHelper.getOnBoarding();
        questionary = dbHelper.getQuestionary();
        dbHelper.updateVersion(app_version);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isFinishing()) {
                if (data != 1) {
                    startActivity(new Intent(SplashActivity.this, OnBoardingActivity.class));
                    finish();
                } else if (questionary != 1) {
                    startActivity(new Intent(SplashActivity.this, SexChooseActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    public void setLanguage(String language) {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(new Locale(language.toLowerCase()));
        }else {
            conf.locale = new Locale(language.toLowerCase());
        }
        res.updateConfiguration(conf, dm);
    }
}

