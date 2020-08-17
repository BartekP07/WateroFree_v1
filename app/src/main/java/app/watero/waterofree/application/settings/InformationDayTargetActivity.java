package app.watero.waterofree.application.settings;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import app.watero.waterofree.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InformationDayTargetActivity extends AppCompatActivity {

    @BindView(R.id.back_btn)
    LinearLayout backBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daytarget_info);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.back_btn)
    public void onViewClicked() {
        finish();
    }
}
