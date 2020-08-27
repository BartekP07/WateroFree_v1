package app.watero.waterofree.application.testhistory;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
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
import app.watero.waterofree.database.MyDBHelper;
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
    MyDBHelper dbHelper;

    ArrayList<String> day_id;
    ArrayList<String> day_number;
    ArrayList<String> day_date;
    ArrayList<String> day_quantity;
    ArrayList<String> day_precentaes;
    ArrayList<String> day_is_active;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view_history_activity);
        ButterKnife.bind(this);

        dbHelper = new MyDBHelper(this);

    }

    public void setData() {
        day_id= new ArrayList<>();
        day_number = new ArrayList<>();
        day_date = new ArrayList<>();
        day_quantity = new ArrayList<>();
        day_precentaes = new ArrayList<>();
        day_is_active = new ArrayList<>();
    }


    public void storeHistoryData() {
        Cursor cursor = dbHelper.readAllHistoryData();
        if (cursor.getCount() == 0) {
            emptyDataTextview.setVisibility(View.VISIBLE);
            emptyDataImg.setVisibility(View.VISIBLE);
        } else {
            emptyDataTextview.setVisibility(View.GONE);
            emptyDataImg.setVisibility(View.GONE);
            while (cursor.moveToNext()) {
                day_id.add(cursor.getString(0));
                day_number.add(cursor.getString(1));
                day_date.add(cursor.getString(2));
                day_quantity.add(cursor.getString(3));
                day_precentaes.add(cursor.getString(4));
                day_is_active.add(cursor.getString(5));
            }
        }
    }
}
