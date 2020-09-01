package app.watero.waterofree.application.drinksList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Locale;

import app.watero.waterofree.R;
import app.watero.waterofree.database.DBContract;
import app.watero.waterofree.database.MyDBHelper;
import app.watero.waterofree.helpers.App;
import app.watero.waterofree.helpers.UserStorage;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class DrinksListActivity extends AppCompatActivity {

    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.empty_data_textview)
    TextView emptyDataTextview;
    @BindView(R.id.empty_data_img)
    ImageView emptyDataImg;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.adView)
    AdView adView;

    ArrayList<String> drink_id;
    ArrayList<String> drink_name;
    ArrayList<String> time_drinked;
    ArrayList<String> drink_quantity;
    ArrayList<String> drink_hydration;

    private SQLiteDatabase mDatabase;
    private DrinksAdapter mAdapter;
    private MyDBHelper dbHelper;
    private UserStorage userStorage;

    private int mAmount = 0;
    //test

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_data_activity);
        ButterKnife.bind(this);
        userStorage = ((App) getApplication()).getUserStorage();
        addAdMob();


        dbHelper = new MyDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setData();
        storeHistoryData();
        //mAdapter = new DrinksAdapter(this,getAllItems());
        mAdapter = new DrinksAdapter(getAllItems(), this, drink_id, drink_name, time_drinked, drink_quantity, drink_hydration);
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem(Integer.parseInt(String.valueOf(viewHolder.itemView.getTag())), viewHolder.getAdapterPosition());
//                int position = viewHolder.getAdapterPosition();
//                mAdapter.notifyItemRemoved(position);

            }

            //show delete image
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(DrinksListActivity.this, R.color.text_orange))
                        .addActionIcon(R.drawable.ic_baseline_delete_24)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    @OnClick(R.id.back_btn)
    public void onViewClicked() {
        finish();
    }

    public void setData() {
        drink_id = new ArrayList<>();
        drink_name = new ArrayList<>();
        time_drinked = new ArrayList<>();
        drink_quantity = new ArrayList<>();
        drink_hydration = new ArrayList<>();
    }


    public void storeHistoryData() {
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

    private void removeItem(int id, int position) {
        mDatabase.delete(DBContract.DBEntry.TABLE_DRINKS,
                DBContract.DBEntry.DRINKS_ID + "=" + id, null);
        mAdapter.swapCursor(getAllItems(), position);
        Toast.makeText(this, R.string.removed_drink, Toast.LENGTH_SHORT).show();
    }

    private Cursor getAllItems() {
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

    private void addAdMob() {
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        new Locale(userStorage.getLangugae(), userStorage.getLangugae());

    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}
