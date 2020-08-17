package app.watero.waterofree.application.language;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import app.watero.waterofree.R;
import app.watero.waterofree.application.onboarding.SplashActivity;
import app.watero.waterofree.database.MyDBHelper;
import app.watero.waterofree.helpers.App;
import app.watero.waterofree.helpers.UserStorage;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LanguagesActivity extends AppCompatActivity {

    @BindView(R.id.close_btn)
    TextView closeBtn;

    MyDBHelper dbHelper;
    Locale myLocale;
    private UserStorage userStorage;

    @BindView(R.id.pick_polish_img)
    ImageView pickPolishImg;
    @BindView(R.id.polish_language)
    LinearLayout polishLanguage;
    @BindView(R.id.pick_english_img)
    ImageView pickEnglishImg;
    @BindView(R.id.english_language)
    LinearLayout englishLanguage;

    private static final String polishCode = "pl";
    private static final String englishCode = "en";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        ButterKnife.bind(this);
        dbHelper = new MyDBHelper(this);
        userStorage = ((App) getApplication()).getUserStorage();
        checkLanguage();
    }

    @OnClick(R.id.close_btn)
    public void closeActivity() {
        finish();
    }

    @OnClick({R.id.polish_language, R.id.english_language})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.polish_language:
                setLocale(polishCode);
                break;
            case R.id.english_language:
                setLocale(englishCode);
                break;
        }
    }

    // ZMIANA JEZYKA
    public void setLocale(String language) {
        myLocale = new Locale(language);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        updateLanguage(language);
        Intent refresh = new Intent(this, SplashActivity.class);
        startActivity(refresh);
    }

    private void updateLanguage(String language) {
        userStorage.setLANGUAGE(language);
    }

    //sprawdz aktywny jezyk
    private void checkLanguage() {
        String checkIsLanguage = userStorage.getLangugae();
        if (checkIsLanguage.equals(polishCode)) {
            pickPolishImg.setVisibility(View.VISIBLE);
            pickEnglishImg.setVisibility(View.GONE);
            polishLanguage.setAlpha(1);
            englishLanguage.setAlpha((float) 0.5);
        } else if (checkIsLanguage.equals(englishCode)) {
            pickPolishImg.setVisibility(View.GONE);
            pickEnglishImg.setVisibility(View.VISIBLE);
            polishLanguage.setAlpha((float) 0.5);
            englishLanguage.setAlpha(1);
        }
    }
}
