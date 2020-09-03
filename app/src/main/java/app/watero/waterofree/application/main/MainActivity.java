package app.watero.waterofree.application.main;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import java.util.Date;
import java.util.Locale;

import app.watero.waterofree.R;
import app.watero.waterofree.application.drinksList.DrinksListActivity;

import app.watero.waterofree.application.settings.SettingsActivity;
import app.watero.waterofree.database.MyDBHelper;
import app.watero.waterofree.helpers.App;
import app.watero.waterofree.helpers.UserStorage;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.date_info)
    TextView dateInfo;
    @BindView(R.id.text_info)
    TextView textInfo;
    @BindView(R.id.amount_water_info)
    TextView amountWaterInfo;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.percentage_amount_water_info)
    TextView percentageAmountWaterInfo;
    @BindView(R.id.today_goal_info)
    TextView todayGoalInfo;
    @BindView(R.id.settings_button)
    ImageButton settingsButton;
    @BindView(R.id.historylist_button)
    ImageButton historylistButton;
    @BindView(R.id.activeday_button)
    ImageButton activedayButton;
    @BindView(R.id.drinkwater_button)
    ImageButton drinkwaterButton;
    @BindView(R.id.information)
    TextView information;
    @BindView(R.id.linearLayout2)
    LinearLayout linearLayout2;
    @BindView(R.id.activeday_group)
    Group activedayTextview;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.day_information_1)
    TextView dayInformation1;
    @BindView(R.id.day_information_2)
    TextView dayInformation2;
    @BindView(R.id.day_information_3)
    TextView dayInformation3;

    private int drinked = 0;
    private int targetOnThisDay = 1500;
    MyDBHelper dbHelper;

    UserStorage userStorage;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userStorage = ((App) getApplication()).getUserStorage();

        ButterKnife.bind(this);
        dbHelper = new MyDBHelper(this);
        upDate();
    }

    private void upDate() {

        drinked = dbHelper.getToDayDrinked_ml();

        amountWaterInfo.setText(drinked + "");

//        if (drinked <= 0) {
//            amountWaterInfo.setText(0 + "");
//        } else {
//            amountWaterInfo.setText(drinked + "");
//        }
//        isActive = dbHelper.isActiveDay();
//        isActiveBtn(isActive);
        toDayDate();
        getToDayTarget();
        getPercentageWater(targetOnThisDay, drinked);
    }

    private void getToDayTarget() {
        int data = 0;
        if (isActiveDay()) {
            data = dbHelper.getAtiveDay();
        } else {
            data = dbHelper.getDayTarget();
        }

        if (data != 0) {
            targetOnThisDay = data;
            todayGoalInfo.setText(data + "");
        } else {
            todayGoalInfo.setText(1500 + "");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        upDate();
        setButtonsActive(true);
    }

    //GET TODAY DATE
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void toDayDate() {
        String pattern = "eeee dd MMMM";
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(pattern, new Locale(userStorage.getLangugae(), userStorage.getLangugae()));
        String date = simpleDateFormat.format(new Date());
        String[] words = date.split(" ", 3);
        String dayName = words[0].substring(0, 1).toUpperCase() + words[0].substring(1, words[0].length());

        dateInfo.setText(dayName + ", " + words[1] + " " + words[2]);
    }

    @OnClick(R.id.activeday_button)
    public void setActivedayButton() {
        int activeDay = dbHelper.isActiveDay();
        if (activeDay != 1) {
            activedayButton.setBackground(getDrawable(R.drawable.button_main_action_active));
            activedayButton.setImageDrawable(getDrawable(R.drawable.active_day_on));
            //change text information
            activedayTextview.setVisibility(View.VISIBLE);
            information.setVisibility(View.INVISIBLE);
            dbHelper.addActiveInformation(1);

            int data = dbHelper.getAtiveDay();
            if (data == 0) {
                data = 1500;
            }

            todayGoalInfo.setText(data + "");
            getPercentageWater(data, drinked);


        } else if (activeDay == 1) {
            activedayButton.setBackground(getDrawable(R.drawable.button_main_action));
            activedayButton.setImageDrawable(getDrawable(R.drawable.active_day_img));
            //change text information
            activedayTextview.setVisibility(View.GONE);
            information.setVisibility(View.VISIBLE);

            dbHelper.updateInforamtion(2);

            int data = dbHelper.getDayTarget();
            if (data == 0) {
                data = 1500;
            }

            todayGoalInfo.setText(data + "");
            getPercentageWater(data, drinked);

        }
    }

    private double getPercentageWater(int target, int drinked) {

        double dri = drinked;
        double tar = target;
        double result = (dri / tar) * 100;

        if (result <= 0) {
            result = 0;
        }
        progressBar.setProgress((int) result);
        percentageAmountWaterInfo.setText((int) result + "");
        setMotivationText(result);
        return result;

    }

    private void setMotivationText(double result) {

        if (result <= 30) {
            information.setText(R.string.l_0_30);
        } else if (result > 30 & result <= 50) {
            information.setText(R.string.l_30_50);
        } else if (result > 50 & result <= 70) {
            information.setText(R.string.l_50_70);
        } else if (result > 70 & result <= 99) {
            information.setText(R.string.l_70_100);
        } else if (result >= 100 & result <= 120) {
            information.setText(R.string.l_100_120);
        } else if (result > 120) {
            information.setText(R.string.l_120);
        }

    }

    @OnClick(R.id.drinkwater_button)
    public void goToDrink() {
        setButtonsActive(false);
        startActivity(new Intent(this, AddDrinkActivity.class));
    }

    @OnClick(R.id.settings_button)
    public void goToSettings() {
        setButtonsActive(false);
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private boolean isActiveDay() {
        int activeDay = dbHelper.isActiveDay();
        if (activeDay == 1) {
            activedayButton.setBackground(getDrawable(R.drawable.button_main_action_active));
            activedayButton.setImageDrawable(getDrawable(R.drawable.active_day_on));
            //change text information
            activedayTextview.setVisibility(View.VISIBLE);
            information.setVisibility(View.INVISIBLE);
            return true;
        }
        return false;
    }

    @OnClick(R.id.historylist_button)
    public void goToHistory() {
        setButtonsActive(false);
        startActivity(new Intent(this, app.watero.waterofree.application.testhistory.MainActivity.class));

    }

    public void setButtonsActive(boolean active) {
        settingsButton.setEnabled(active);
        drinkwaterButton.setEnabled(active);
        historylistButton.setEnabled(active);
    }

    @OnClick({R.id.text_info, R.id.linearLayout2})
    public void showHistory(View view) {
        startActivity(new Intent(this, DrinksListActivity.class));
    }

    @OnClick(R.id.linearLayout)
    public void onViewClicked() {
        startActivity(new Intent(this, SettingsActivity.class));
    }
}


