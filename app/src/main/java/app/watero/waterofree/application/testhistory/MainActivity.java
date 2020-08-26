package app.watero.waterofree.application.testhistory;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import app.watero.waterofree.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.empty_data_textview)
    TextView emptyDataTextview;
    @BindView(R.id.empty_data_img)
    ImageView emptyDataImg;
    @BindView(R.id.rv_group)
    RecyclerView rvGroup;
    @BindView(R.id.adView)
    AdView adView;

    ArrayList<String> arrayListGroup;
    LinearLayoutManager linearLayoutManager;
    GroupAdp adapterGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view_history_activity);
        ButterKnife.bind(this);

    }
}
