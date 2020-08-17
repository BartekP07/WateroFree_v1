package app.watero.waterofree.application.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import app.watero.waterofree.R;
import app.watero.waterofree.application.language.LanguagesActivity;
import app.watero.waterofree.application.main.MainActivity;
import app.watero.waterofree.application.questionnaire.SexChooseActivity;
import app.watero.waterofree.database.MyDBHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class OnBoardingActivity extends AppCompatActivity {

    @BindView(R.id.onboarding_viewpager)
    ViewPager2 onboardingViewpager;
    @BindView(R.id.onboarding_indicators)
    LinearLayout onboardingIndicators;
    @BindView(R.id.skip_btn)
    TextView skipBtn;
    @BindView(R.id.country_name)
    TextView choose_country;

    private OnBoardingAdapter onBoardingAdapter;
    private LinearLayout linearIndicator;
    private int onboardingActive = 0;

    MyDBHelper dbHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        ButterKnife.bind(this);

        dbHelper =  new MyDBHelper(this);

        linearIndicator = findViewById(R.id.onboarding_indicators);

        setDateToOnboarding();
        ViewPager2 viewPager = findViewById(R.id.onboarding_viewpager);
        viewPager.setAdapter(onBoardingAdapter);
        setIndicators();
        setCurrentIndicators(0);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicators(position);
            }
        });
    }

    private void offOnboarding(){
        onboardingActive = 1;
        dbHelper.offOnBoarding(onboardingActive);
    }

    private void setDateToOnboarding() {
        List<OnBoardingItem> onBoardingItems = new ArrayList<>();

        OnBoardingItem item1 = new OnBoardingItem();
        item1.setTitle(getString(R.string.l_onboarding_first_title));
        item1.setDescription(getString(R.string.l_onboarding_first_desc));
        item1.setImage(R.drawable.dark_onboarding_1);

        OnBoardingItem item2 = new OnBoardingItem();
        item2.setTitle(getString(R.string.l_onboarding_second_title));
        item2.setDescription(getString(R.string.l_onboarding_second_desc));
        item2.setImage(R.drawable.dark_onboarding_2);

        OnBoardingItem item3 = new OnBoardingItem();
        item3.setTitle(getString(R.string.l_onboarding_third_title));
        item3.setDescription(getString(R.string.l_onboarding_third_desc));
        item3.setImage(R.drawable.dark_onboarding_3);

        onBoardingItems.add(item1);
        onBoardingItems.add(item2);
        onBoardingItems.add(item3);

        onBoardingAdapter = new OnBoardingAdapter(onBoardingItems);

    }

    private void setIndicators() {
        ImageView[] imageViews = new ImageView[onBoardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(12, 0, 12, 0);
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i] = new ImageView(getApplicationContext());
            imageViews[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_active));
            imageViews[i].setLayoutParams(layoutParams);
            linearIndicator.addView(imageViews[i]);
        }
    }

    private void setCurrentIndicators(int index) {
        int childCOunt = linearIndicator.getChildCount();
        for (int i = 0; i < childCOunt; i++) {
            ImageView imageView = (ImageView) linearIndicator.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_unactive));
            }
        }
    }

    @OnClick(R.id.skip_btn)
    public void onSkipClicked() {
        offOnboarding();
        if(dbHelper.getQuestionary() == 1){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }else{
            startActivity(new Intent(this, SexChooseActivity.class));
            finish();
        }
    }

    @OnClick(R.id.country_name)
    public void chooseLanguage(){
        startActivity(new Intent(this, LanguagesActivity.class));
    }

}

