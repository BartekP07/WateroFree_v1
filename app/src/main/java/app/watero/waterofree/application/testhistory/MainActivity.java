package app.watero.waterofree.application.testhistory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
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
import app.watero.waterofree.database.DBContract;
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

    //inner data
    ArrayList<String> drink_id;
    ArrayList<String> drink_name;
    ArrayList<String> time_drinked;
    ArrayList<String> drink_quantity;
    ArrayList<String> drink_hydration;

    private SQLiteDatabase mDatabase;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view_history_activity);
        ButterKnife.bind(this);

        dbHelper = new MyDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        setData();
        storeHistoryData();
        setInnerData();
        storeInnerHistoryData();

        adapterGroup = new GroupAdp(this,getAllHistoryItems(),getAllDrinksItems(),this,day_id,day_number);

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

    public void setInnerData() {
        drink_id = new ArrayList<>();
        drink_name = new ArrayList<>();
        time_drinked = new ArrayList<>();
        drink_quantity = new ArrayList<>();
        drink_hydration = new ArrayList<>();
    }
    public void storeInnerHistoryData() {
        Cursor cursor = dbHelper.redAllDataHistory();
        if (cursor.getCount() == 0) {
            emptyDataTextview.setVisibility(View.VISIBLE);
            emptyDataImg.setVisibility(View.VISIBLE);
        } else {
            emptyDataTextview.setVisibility(View.GONE);
            emptyDataImg.setVisibility(View.GONE);
            while (cursor.moveToNext()) {
                drink_id.add(cursor.getString(0));
                drink_name.add(cursor.getString(1));
                time_drinked.add(cursor.getString(2));
                drink_quantity.add(cursor.getString(3));
                drink_hydration.add(cursor.getString(4));
            }
        }
    }

    private Cursor getAllDrinksItems() {
        Cursor cursor = mDatabase.query(
                DBContract.DBEntry.TABLE_DRINKS,
                null,
                null,
                null,
                null,
                null,
                DBContract.DBEntry.DRINKS_ID + " DESC"
        );

        if (cursor.getCount() == 0) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    emptyDataTextview.setVisibility(View.VISIBLE);
                    emptyDataImg.setVisibility(View.VISIBLE);
                }
            }, 500);
        } else {
            emptyDataTextview.setVisibility(View.GONE);
            emptyDataImg.setVisibility(View.GONE);
        }
        return cursor;
    }

    private Cursor getAllHistoryItems() {
        Cursor cursor = mDatabase.query(
                DBContract.DBEntry.TABLE_HISTORY_DAY_INFO,
                null,
                null,
                null,
                null,
                null,
                DBContract.DBEntry.HISTORY_DATE + " DESC"
        );

        if (cursor.getCount() == 0) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    emptyDataTextview.setVisibility(View.VISIBLE);
                    emptyDataImg.setVisibility(View.VISIBLE);
                }
            }, 500);
        } else {
            emptyDataTextview.setVisibility(View.GONE);
            emptyDataImg.setVisibility(View.GONE);
        }
        return cursor;
    }

}
