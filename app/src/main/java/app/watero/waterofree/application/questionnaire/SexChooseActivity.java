package app.watero.waterofree.application.questionnaire;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import app.watero.waterofree.R;
import app.watero.waterofree.application.language.LanguagesActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SexChooseActivity extends AppCompatActivity {

    @BindView(R.id.man_btn)
    ImageView manBtn;
    @BindView(R.id.man_textView)
    TextView manTextView;
    @BindView(R.id.woman_btn)
    ImageView womanBtn;
    @BindView(R.id.woman_textView)
    TextView womanTextView;
    @BindView(R.id.go_next)
    Button goNext;
    @BindView(R.id.change_language)
    TextView changeLanguage;
    @BindView(R.id.manLayout)
    LinearLayout manLayout;
    @BindView(R.id.womanLayout)
    LinearLayout womanLayout;

    private int sex = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sex);
        ButterKnife.bind(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick({R.id.manLayout, R.id.womanLayout, R.id.go_next, R.id.change_language})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.manLayout:
                sexChoose(true);
                break;
            case R.id.womanLayout:
                sexChoose(false);
                break;
            case R.id.go_next:
                tryGoNext();
                break;
            case R.id.change_language:
                startActivity(new Intent(this, LanguagesActivity.class));
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void sexChoose(boolean active) {

        //active == man / unactive == woman
        if (active) {
            manBtn.setBackground(getResources().getDrawable(R.drawable.orange_man));
            manTextView.setTextColor(getColor(R.color.text_orange));
            womanBtn.setBackground(getResources().getDrawable(R.drawable.black_woman));
            womanTextView.setTextColor(getColor(R.color.text_dark));

            sex = 1;
        } else {
            manBtn.setBackground(getResources().getDrawable(R.drawable.black_man));
            manTextView.setTextColor(getColor(R.color.text_dark));
            womanBtn.setBackground(getResources().getDrawable(R.drawable.orange_woman));
            womanTextView.setTextColor(getColor(R.color.text_orange));

            sex = 0;
        }

        goNext.setBackground(getResources().getDrawable(R.drawable.button_orange));
        goNext.setTextColor(getColor(R.color.color_BackgroundDark));
    }

    private void tryGoNext(){
        if(sex != 2) {
            Intent intent = new Intent(this, WeightChooseActivity.class);
            intent.putExtra("SEX_SESSION", sex);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Wybierz swoją płeć.", Toast.LENGTH_SHORT).show();
        }
    }
}
