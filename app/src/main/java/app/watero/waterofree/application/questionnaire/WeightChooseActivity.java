package app.watero.waterofree.application.questionnaire;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import app.watero.waterofree.R;
import app.watero.waterofree.application.language.LanguagesActivity;
import app.watero.waterofree.application.main.MainActivity;
import app.watero.waterofree.database.MyDBHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeightChooseActivity extends AppCompatActivity {

    @BindView(R.id.weight_picker)
    NumberPicker weightPicker;
    @BindView(R.id.ready_btn)
    Button readyBtn;
    @BindView(R.id.change_language)
    TextView changeLanguage;

    private final int minimalWeight = 30;
    private final int maxWeight = 200;
    private int getWeight = 70;

    private int normalDay = 0;
    private int activeDay = 0;

    private MyDBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_pick);
        ButterKnife.bind(this);
        chooseWeight();
        dbHelper = new MyDBHelper(this);
    }


    @OnClick({R.id.ready_btn, R.id.change_language})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ready_btn:
                saveAndGo();
                break;
            case R.id.change_language:
                startActivity(new Intent(this, LanguagesActivity.class));
                break;
        }
    }

    private void saveAndGo() {
        Intent intent = getIntent();
        int sex = intent.getIntExtra("SEX_SESSION", 0);
        getWeight = weightPicker.getValue();
        targetforDay(getWeight,sex);
        dbHelper.updateQuestionarySettings("1",sex, getWeight, normalDay,activeDay, 1);
        startActivity(new Intent(this, MainActivity.class));

    }

    private void chooseWeight(){
        weightPicker.setScrollBarFadeDuration(1000);
        weightPicker.setFormatter(value -> String.format("%02d", value));
        weightPicker.setMinValue(minimalWeight);
        weightPicker.setMaxValue(maxWeight);
        weightPicker.setValue(70);
    }

    private void targetforDay(int weight , int sexPick) {
        //sex % on active day
        double activeDay;

        if(sexPick == 1){
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
        } else {
            if ((summary % 100) >= 50){
                output = (summary + 100) - ((summary % 1000) % 100);
            } else {
                output = summary - ((summary % 1000) % 100);
            }
        }

        normalDay = (int)output;
        targetforActiveDay(output, activeDay);

    }

    private void targetforActiveDay(double normalDay, double active) {
        double summary = normalDay + (normalDay * active);
        double outputActive = 0;
        if ((summary % 100) >= 50) {
            outputActive  = (summary + 100) - ((summary % 1000) % 100);
        } else {
            outputActive  = summary - ((summary % 1000) % 100);
        }
        activeDay = (int) outputActive ;
    }
}

