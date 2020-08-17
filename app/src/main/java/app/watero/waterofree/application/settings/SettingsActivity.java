package app.watero.waterofree.application.settings;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import app.watero.waterofree.R;
import app.watero.waterofree.application.language.LanguagesActivity;
import app.watero.waterofree.database.MyDBHelper;
import app.watero.waterofree.helpers.App;
import app.watero.waterofree.helpers.Notification_reciever;
import app.watero.waterofree.helpers.UserStorage;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    //SETTINGS ACTIVITY
    @BindView(R.id.switch_btn)
    Switch switchBtn;
    @BindView(R.id.line6)
    ImageView line6;
    @BindView(R.id.time)
    LinearLayout time;
    @BindView(R.id.line7)
    ImageView line7;
    @BindView(R.id.language_set_name)
    TextView languageSetName;
    @BindView(R.id.items_group)
    Group itemsGroup;
    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.language)
    LinearLayout language_layout_btn;
    @BindView(R.id.units_set_type)
    TextView unitsSetType;
    @BindView(R.id.units)
    LinearLayout units_layout_btn;
    @BindView(R.id.sex_set_type)
    TextView sexSetType;
    @BindView(R.id.sex)
    LinearLayout sex_layout_btn;
    @BindView(R.id.weight_quantity)
    TextView weightQuantity;
    @BindView(R.id.weight)
    LinearLayout weight_layout_btn;
    @BindView(R.id.day_target)
    TextView dayTarget;
    @BindView(R.id.daytarget)
    LinearLayout daytarget_layout_btn;
    @BindView(R.id.target_day_active)
    TextView targetDayActive;
    @BindView(R.id.daytargetactiveday)
    LinearLayout daytargetactiveday_layout_btn;
    @BindView(R.id.nothifivation)
    LinearLayout nothifivation_layout_btn;
    @BindView(R.id.time_type)
    TextView set_timeType;
    @BindView(R.id.time_wakeup)
    TextView set_timeWakeup;
    @BindView(R.id.wakeup)
    LinearLayout wakeup_layout_btn;
    @BindView(R.id.time_sleep)
    TextView set_timeSleep;
    @BindView(R.id.gotosleep)
    LinearLayout gotosleep_layout_btn;
    @BindView(R.id.time_frequency)
    TextView set_timeFrequency;
    @BindView(R.id.frequency)
    LinearLayout frequency_layout_btn;
    @BindView(R.id.terms_of_use)
    TextView termsOfUse_btn;
    @BindView(R.id.rate_application_btn)
    TextView rateApplication_btn;
    @BindView(R.id.kg_lb_title)
    TextView kgLbTitle;
    @BindView(R.id.day_title_ml)
    TextView dayTitleMl;
    @BindView(R.id.day_active_title_ml)
    TextView dayActiveTitleMl;
    @BindView(R.id.light_mode)
    LinearLayout lightMode;


    //POPUP ACTIVITY
    Dialog mydialog;
    TextView man_txt;
    ImageView man_img;
    TextView woman_txt;
    ImageView woman_img;

    //weight popup
    NumberPicker numberPicker;
    NumberPicker firstPick;
    NumberPicker secondPick;
    TextView confirm;

    //hours popup
    TextView usTime;
    TextView euTime;
    @BindView(R.id.version_number)
    TextView versionNumber;

    private int minimalWeight = 30;
    private int maxWeight = 200;

    private String timeType = "wakeup";
    private String weightOrFrequencyType = "weight";
    private String dayType = "normal";

    //start from this settings
    private int defaultFrequency = 7;
    private int sex = 0;
    private int defaultHWakeUp = 00;
    private int defaultmWakeup = 00;
    private int defaultHSleep = 00;
    private int defaultmSleep = 00;
    private MyDBHelper dbHelper;
    private UserStorage userStorage;

    private final String app_version = "1.4.1";


    private int[] frequency = {R.string.f_15m, R.string.f_30m, R.string.f_45m, R.string.f_1h, R.string.f_1h_15m, R.string.f_1h_30m, R.string.f_1h_45m,
            R.string.f_2h, R.string.f_2h_15m, R.string.f_2h_30m, R.string.f_2h_45m, R.string.f_3h};

    private String[] frequencystr = {"00:15", "00:30", "00:45", "01:00", "01:15", "01:30", "01:45", "02:00", "02:15", "02:30", "02:45", "03:00"};

    private JobScheduler mSchedular;
    private static final int jobId = 0;
    private int selectedNetworkOption = JobInfo.NETWORK_TYPE_ANY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        mydialog = new Dialog(this);
        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dbHelper = new MyDBHelper(this);
        userStorage = ((App) getApplication()).getUserStorage();
        readDb();
        startAlarm();
        stopAlarm();
    }

    //close activity
    @OnClick(R.id.back_btn)
    public void goBack() {
        saveData();
        finish();
    }

    @Override
    protected void onDestroy() {
        saveData();
        super.onDestroy();
    }

//    @OnClick(R.id.nothifivation)
//    public void nothifivationPro(){
//        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=pl.wateroapp");
//        startActivity(new Intent(Intent.ACTION_VIEW, uri));
//    }

    //Saving data in DB
    private void saveData() {
        String checkSex = sexSetType.getText().toString().trim();
        int weight = Integer.parseInt(weightQuantity.getText().toString().trim());
        int dayT = Integer.parseInt(dayTarget.getText().toString().trim());
        int dayA = Integer.parseInt(targetDayActive.getText().toString().trim());
        String wakeupTime = set_timeWakeup.getText().toString().trim();
        String sleepingTime = set_timeSleep.getText().toString().trim();
        int freq = returnFrequency(set_timeFrequency.getText().toString());

        int notification = 0;
        int sex = 0;
        if (checkSex.equals(getString(R.string.g_sex_woman))) {
            sex = 0;
        } else {
            sex = 1;
        }

        if (switchBtn.isChecked()) {
            notification = 1;
        } else {
            notification = 0;
        }
        dbHelper.updateSettings("1", sex, weight, dayT, dayA, notification, wakeupTime, sleepingTime, freq);

    }

    private int returnFrequency(String frequency) {

        int miliseconds = 10800000;

        if (frequency.equals(getString(R.string.f_15m))) {
            miliseconds = 900000;
        } else if (frequency.equals(getString(R.string.f_30m))) {
            miliseconds = 1800000;
        } else if (frequency.equals(getString(R.string.f_45m))) {
            miliseconds = 2700000;
        } else if (frequency.equals(getString(R.string.f_1h))) {
            miliseconds = 3600000;
        } else if (frequency.equals(getString(R.string.f_1h_15m))) {
            miliseconds = 4500000;
        } else if (frequency.equals(getString(R.string.f_1h_30m))) {
            miliseconds = 5400000;
        } else if (frequency.equals(getString(R.string.f_1h_45m))) {
            miliseconds = 6300000;
        } else if (frequency.equals(getString(R.string.f_2h))) {
            miliseconds = 7200000;
        } else if (frequency.equals(getString(R.string.f_2h_15m))) {
            miliseconds = 8100000;
        } else if (frequency.equals(getString(R.string.f_2h_30m))) {
            miliseconds = 9000000;
        } else if (frequency.equals(getString(R.string.f_2h_45m))) {
            miliseconds = 9900000;
        } else if (frequency.equals(getString(R.string.f_3h))) {
            miliseconds = 10800000;
        }
        return miliseconds;
    }

    private void readDb() {
        readSexData();
        readWeightData();
        readActiveDayTarget();
        readDayTarget();
        readLanguage();
        readNotification();
        readWakeUp();
        readSleepTime();
        readFrequency();
        readVersion();
    }

    private void readLanguage() {
        String data = userStorage.getLangugae();
        languageSetName.setText(countryCode(data));
    }

    private String countryCode(String country) {
        switch (country) {
            case "pl":
                return getString(R.string.polish_language);
            case "en":
                return getString(R.string.english_language);
            default:
                return getString(R.string.polish_language);
        }
    }

    private void readSexData() {
        int data = dbHelper.getSex();
        if (data == 0) {
            sexSetType.setText(R.string.g_sex_woman);
        } else {
            sexSetType.setText(R.string.g_sex_man);
        }
    }

    private void readWeightData() {
        int data = dbHelper.getWeight();
        if (data != 0) {
            weightQuantity.setText(data + "");
        } else {
            weightQuantity.setText(65 + "");
        }
    }

    private void readDayTarget() {
        int data = dbHelper.getDayTarget();
        if (data != 0) {
            dayTarget.setText(data + "");
        } else {
            dayTarget.setText(1500 + "");
        }
    }

    private void readActiveDayTarget() {
        int data = dbHelper.getAtiveDay();
        if (data != 0) {
            targetDayActive.setText(data + "");
        } else {
            targetDayActive.setText(1800 + "");
        }
    }

    private void readNotification() {
        int data = dbHelper.getNotification();
        if (data != 0) {
            itemsGroup.setVisibility(View.VISIBLE);
            switchBtn.setChecked(true);
        } else {
            itemsGroup.setVisibility(View.GONE);
            switchBtn.setChecked(false);
        }
    }

    private void readWakeUp() {
        String data = dbHelper.getWakeUp();
        if (data != null) {
            set_timeWakeup.setText(data + "");
        } else {
            set_timeWakeup.setText("06:00");
        }
    }

    private void readSleepTime() {
        String data = dbHelper.getSleepTime();
        if (data != null) {
            set_timeSleep.setText(data + "");
        } else {
            set_timeSleep.setText("20:00");
        }

    }

    private void readFrequency() {
        int data = dbHelper.getFrequency();
        if (data != 0) {
            set_timeFrequency.setText(changeMlsOnFrequency(data));
        } else {
            set_timeFrequency.setText(R.string.f_2h);
        }
    }

    //show notification
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.switch_btn)
    public void onViewClicked() {

        if (switchBtn.isChecked()) {
            //itemsGroup.setVisibility(View.VISIBLE);
            //scheudleJob();
        } else {
            // itemsGroup.setVisibility(View.GONE);
            //cancelJobs();
        }
    }

    //change lanugage
    @OnClick(R.id.language_set_name)
    public void onLanguageClicked() {
        startActivity(new Intent(this, LanguagesActivity.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick(R.id.sex)
    public void setUnitsSetType() {
        showSexPopUp(sexSetType.getText().toString());
    }

    @OnClick(R.id.weight)
    public void setWeightQuantity() {
        weightOrFrequencyType = "weight";
        showWeight_FrequencyPopup(getString(R.string.g_weight));
    }

    //POPUP sex choose
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showSexPopUp(String sex) {
        mydialog.setContentView(R.layout.popup_sexchoose);
        woman_img = mydialog.findViewById(R.id.woman_img);
        woman_txt = mydialog.findViewById(R.id.woman_txt);
        man_img = mydialog.findViewById(R.id.man_img);
        man_txt = mydialog.findViewById(R.id.man_txt);

        if (sex.equals(getString(R.string.g_sex_man))) {
            man_img.setBackground(getResources().getDrawable(R.drawable.orange_man));
            man_txt.setTextColor(getColor(R.color.text_orange));

            woman_img.setBackground(getResources().getDrawable(R.drawable.black_woman));
            woman_txt.setTextColor(getColor(R.color.text_dark));

        } else if (sex.equals(getString(R.string.g_sex_woman))) {
            woman_img.setBackground(getResources().getDrawable(R.drawable.orange_woman));
            woman_txt.setTextColor(getColor(R.color.text_orange));

            man_img.setBackground(getResources().getDrawable(R.drawable.black_man));
            man_txt.setTextColor(getColor(R.color.text_dark));
        }
        mydialog.show();
    }

    //on sex click
    public void onSexChoose(View view) {
        int weight = Integer.parseInt(weightQuantity.getText().toString());
        switch (view.getId()) {
            case R.id.woman_img:
                sexSetType.setText(R.string.g_sex_woman);
                targetforDay(weight);
                mydialog.dismiss();
                break;

            case R.id.man_img:
                sexSetType.setText(R.string.g_sex_man);
                targetforDay(weight);
                mydialog.dismiss();
                break;
        }
    }

    //POPUP weight and frequency picker
    private void showWeight_FrequencyPopup(String tittle) {
        int weight = Integer.parseInt(weightQuantity.getText().toString());
        mydialog.setContentView(R.layout.popup_weight);
        numberPicker = mydialog.findViewById(R.id.first_number);
        confirm = mydialog.findViewById(R.id.confirm);
        TextView title = mydialog.findViewById(R.id.title_weight);


        title.setText(tittle);
//        String units = unitsSetType.getText().toString();

        numberPicker.setScrollBarFadeDuration(1000);
        numberPicker.setFormatter(value -> String.format("%02d", value));

        if (weightOrFrequencyType.equals("weight")) {
            numberPicker.setMinValue(minimalWeight);
            numberPicker.setMaxValue(maxWeight);
            numberPicker.setValue(weight);
        } else {
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(frequencystr.length - 1);
            numberPicker.setDisplayedValues(frequencystr);
            numberPicker.setValue(defaultFrequency);
        }


        mydialog.show();
    }

    public void confirmWeight_Frequency(View view) {

        String finalFrequency = null;


        if (weightOrFrequencyType.equals("weight")) {
            int b = numberPicker.getValue();
            targetforDay(b);
            weightQuantity.setText(b + "");
        } else {

            int frequency = numberPicker.getValue();
            defaultFrequency = frequency;
            switch (frequency) {
                case 0:
                    finalFrequency = getString(R.string.f_15m);
                    break;
                case 1:
                    finalFrequency = getString(R.string.f_30m);
                    break;
                case 2:
                    finalFrequency = getString(R.string.f_45m);
                    break;
                case 3:
                    finalFrequency = getString(R.string.f_1h);
                    break;
                case 4:
                    finalFrequency = getString(R.string.f_1h_15m);
                    break;
                case 5:
                    finalFrequency = getString(R.string.f_1h_30m);
                    break;
                case 6:
                    finalFrequency = getString(R.string.f_1h_45m);
                    break;
                case 7:
                    finalFrequency = getString(R.string.f_2h);
                    break;
                case 8:
                    finalFrequency = getString(R.string.f_2h_15m);
                    break;
                case 9:
                    finalFrequency = getString(R.string.f_2h_30m);

                    break;
                case 10:
                    finalFrequency = getString(R.string.f_2h_45m);

                    break;
                case 11:
                    finalFrequency = getString(R.string.f_3h);

                    break;
                default:

                    break;
            }
            set_timeFrequency.setText(finalFrequency);
        }

        mydialog.dismiss();
    }

    private String changeMlsOnFrequency(int frequency) {

        String setFrequency = getString(R.string.f_1h);

        switch (frequency) {
            case 900000:
                setFrequency = getString(R.string.f_15m);
                break;
            case 1800000:
                setFrequency = getString(R.string.f_30m);
                break;
            case 2700000:
                setFrequency = getString(R.string.f_45m);
                break;
            case 3600000:
                setFrequency = getString(R.string.f_1h);
                break;
            case 4500000:
                setFrequency = getString(R.string.f_1h_15m);
                break;
            case 5400000:
                setFrequency = getString(R.string.f_1h_30m);
                break;
            case 6300000:
                setFrequency = getString(R.string.f_1h_45m);
                break;
            case 7200000:
                setFrequency = getString(R.string.f_2h);
                break;
            case 8100000:
                setFrequency = getString(R.string.f_2h_15m);
                break;
            case 9000000:
                setFrequency = getString(R.string.f_2h_30m);
                break;
            case 9900000:
                setFrequency = getString(R.string.f_2h_45m);
                break;
            case 10800000:
                setFrequency = getString(R.string.f_3h);
                break;

        }

        return setFrequency;
    }

    //POPUP choose time form
    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick(R.id.time)
    public void showTimePopup() {
        chooseTime();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void chooseTime() {
        mydialog.setContentView(R.layout.popup_hours);
        euTime = mydialog.findViewById(R.id.eu_time);
        usTime = mydialog.findViewById(R.id.us_time);
        String type = set_timeType.getText().toString();

        if (!type.equals("12pm")) {
            euTime.setTextColor(getColor(R.color.text_orange));
            usTime.setTextColor(getColor(R.color.text_dark));

        } else if (type.equals("12pm")) {
            usTime.setTextColor(getColor(R.color.text_orange));
            euTime.setTextColor(getColor(R.color.text_dark));
        }
        mydialog.show();

    }

    public void onTimeChoose(View view) {
        switch (view.getId()) {
            case R.id.us_time:
                set_timeType.setText("12pm");
                mydialog.dismiss();
                break;
            case R.id.eu_time:
                set_timeType.setText("24h");
                mydialog.dismiss();
                break;
        }
    }

    //Show (activity) information about targets
    @OnClick({R.id.information_about_daytarget, R.id.information_about_activeday})
    public void onInformationClicked(View view) {
        Intent intent = new Intent(this, InformationDayTargetActivity.class);
        switch (view.getId()) {
            case R.id.information_about_daytarget:
                startActivity(intent);
                break;
            case R.id.information_about_activeday:
                startActivity(intent);
                break;
        }
    }

    //water consumption conversion rate
    private void targetforDay(int weight) {
        //sex % to drink
        double sex;
        //sex % on active day
        double activeDay;

        String sexPick = sexSetType.getText().toString();

        if (!sexPick.equals(getString(R.string.g_sex_woman))) {
            activeDay = 0.2;
        } else {
            activeDay = 0.1;
        }

        int mlOnKg = weight * 30;
        double outFood = mlOnKg - (mlOnKg * 0.2);
        double summary = outFood;
        double output = 0;


        if (summary < 1500) {
            output = 1500;

            dayTarget.setText((int) output + "");
        } else {
            if ((summary % 100) >= 50) {
                output = (summary + 100) - ((summary % 1000) % 100);
            } else {
                output = summary - ((summary % 1000) % 100);
            }
            dayTarget.setText((int) output + "");
        }
        targetforActiveDay(output, activeDay);
    }

    private void targetforActiveDay(double normalDay, double activeDay) {
        double summary = normalDay + (normalDay * activeDay);
        double output = 0;
        if ((summary % 100) >= 50) {
            output = (summary + 100) - ((summary % 1000) % 100);
        } else {
            output = summary - ((summary % 1000) % 100);
        }
        targetDayActive.setText((int) output + "");
    }


    @OnClick({R.id.day_target, R.id.target_day_active})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.day_target:
                showTargetPopup(getString(R.string.g_day_target), dayTarget.getText().toString());
                dayType = "normal";
                break;
            case R.id.target_day_active:
                showTargetPopup(getString(R.string.l_target_in_active_days), targetDayActive.getText().toString());
                dayType = "active";
                break;
        }
    }

    private void showTargetPopup(String title, String target) {
        mydialog.setContentView(R.layout.popup_target);

        TextView targetTittle = mydialog.findViewById(R.id.target_title);
        EditText setTarget = mydialog.findViewById(R.id.settarget);
        TextView confirmBtn = mydialog.findViewById(R.id.confirm_target);
        setTarget.setText(target);
        targetTittle.setText(title);
        mydialog.show();

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sexPick = sexSetType.getText().toString();

                int target = Integer.parseInt(setTarget.getText().toString());

                double targetActive = 0;
                double output = 0;
                double activeDay;

                if (!sexPick.equals(getString(R.string.g_sex_woman))) {
                    activeDay = 0.2;
                } else {
                    activeDay = 0.1;
                }

                if (dayType.equals("normal")) {
                    if (target < 1500) {
                        dayTarget.setText(1500 + "");
                    } else {
                        dayTarget.setText(target + "");
                    }

                    targetActive = target + (target * activeDay);

                    if ((targetActive % 100) >= 50) {
                        output = (targetActive + 100) - ((targetActive % 1000) % 100);
                        if (output < 1500) {
                            output = 1500;
                        }
                        targetDayActive.setText((int) output + "");
                    } else {
                        output = targetActive - ((targetActive % 1000) % 100);
                        if (output < 1500) {
                            output = 1500;
                        }
                        targetDayActive.setText((int) output + "");
                    }
                } else {
                    if (target < 1500) {
                        targetDayActive.setText((1500 + ""));
                    } else {
                        targetDayActive.setText((int) target + "");
                    }
                }
                mydialog.dismiss();
            }
        });
    }


    @OnClick(R.id.terms_of_use)
    public void setTermsOfUse_btn() {
        Uri uri = Uri.parse("https://policies.itcloud.pl/");
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    @OnClick(R.id.light_mode)
    public void setProVersion() {
        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=pl.wateroapp");
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }


    private void timeToLive(String tittle) {
        mydialog.setContentView(R.layout.popup_wakeup_sleep);

        TextView tittlePopUp = mydialog.findViewById(R.id.title_wakeup);
        firstPick = mydialog.findViewById(R.id.first_pick);
        secondPick = mydialog.findViewById(R.id.second_pick);
        tittlePopUp.setText(tittle);

        int value1 = defaultHWakeUp;
        int value2 = defaultmWakeup;

        if (timeType.equals("sleep")) {
            value1 = defaultHSleep;
            value2 = defaultmSleep;
        } else {
            value1 = defaultHWakeUp;
            value2 = defaultmWakeup;
        }

        //godziny
        firstPick.setMinValue(00);
        firstPick.setMaxValue(23);
        firstPick.setScrollBarFadeDuration(1000);
        firstPick.setFormatter(value -> String.format("%02d", value));
        firstPick.setValue(value1);

        //minuty
        secondPick.setMinValue(00);
        secondPick.setMaxValue(59);
        secondPick.setScrollBarFadeDuration(1000);
        secondPick.setFormatter(value -> String.format("%02d", value));
        secondPick.setValue(value2);

        mydialog.show();

    }

    @OnClick({R.id.wakeup, R.id.gotosleep})
    public void setWakeUp_SleepTime(View view) {
        switch (view.getId()) {
            case R.id.wakeup:
                timeToLive(getString(R.string.g_wakeup));
                timeType = "wakeup";
                break;
            case R.id.gotosleep:
                timeToLive(getString(R.string.l_time_to_sleep));
                timeType = "sleep";
                break;
        }
    }

    public void confirmHour(View view) {
        int defaultH = firstPick.getValue();
        int defaultm = secondPick.getValue();
        String hour;
        String minutes;
        if (defaultH < 10) {
            hour = "0" + defaultH;
        } else {
            hour = defaultH + "";
        }
        if (defaultm < 10) {
            minutes = "0" + defaultm;
        } else {
            minutes = defaultm + "";
        }

        String finalHour = hour + ":" + minutes;

        if (timeType.equals("wakeup")) {
            set_timeWakeup.setText(finalHour);

        } else {
            set_timeSleep.setText(finalHour);
        }

        if (timeType.equals("sleep")) {
            defaultHSleep = defaultH;
            defaultmSleep = defaultm;
        } else {
            defaultHWakeUp = defaultH;
            defaultmWakeup = defaultm;
        }

        mydialog.dismiss();
    }

    @OnClick(R.id.frequency)
    public void setFrequency() {
        weightOrFrequencyType = "frequency";
        showWeight_FrequencyPopup(getString(R.string.g_notifiaction));

    }

    private String getTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(date) + "";
        String time2 = sdf.format(System.currentTimeMillis());
        Toast.makeText(this, sdf.format(System.currentTimeMillis()) + "", Toast.LENGTH_SHORT).show();
        return time2;
    }


    public void stopAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 58);
        calendar.set(Calendar.SECOND, 00);

        Intent intent = new Intent(getApplicationContext(), Notification_reciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        alarmManager.cancel(pendingIntent);
    }

    private void startAlarm() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 29);
        calendar.set(Calendar.SECOND, 00);

        Intent intent = new Intent(getApplicationContext(), Notification_reciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, calendar.getTimeInMillis(), 900000, pendingIntent);

    }

    private int checkAlarm(String startAlarm, String stopAlarm) {
        Calendar getTime = Calendar.getInstance();
        getTime.setTimeInMillis(System.currentTimeMillis());
        String currentTime = getTime.get(Calendar.HOUR_OF_DAY) + "" + getTime.get(Calendar.MINUTE) + "" + getTime.get(Calendar.SECOND);

        int onOff = 0;

        if (startAlarm.equals(currentTime)) {
            onOff = 1;
        } else if (stopAlarm.equals(startAlarm)) {
            onOff = 2;
        } else {
            onOff = 1;
        }
        return onOff;

    }

    private void readVersion() {
        versionNumber.setText(dbHelper.getVersion() +"");
    }

}

