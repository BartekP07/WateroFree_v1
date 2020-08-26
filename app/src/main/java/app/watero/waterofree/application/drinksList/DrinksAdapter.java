package app.watero.waterofree.application.drinksList;

import android.app.Activity;
import android.view.View;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.watero.waterofree.R;
import app.watero.waterofree.database.DBContract;

public class DrinksAdapter extends RecyclerView.Adapter<DrinksAdapter.DrinksViewHolder> {
    private Context mContext;
    private Cursor mCursor;



    ArrayList drink_id;
    ArrayList drink_name;
    ArrayList time_drinked;
    ArrayList drink_quantity;
    ArrayList drink_hydration;

    public DrinksAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public DrinksAdapter( Cursor cursor, Context context, ArrayList drink_id, ArrayList drink_name, ArrayList time_drinked, ArrayList drink_quantity, ArrayList drink_hydration) {
        this.drink_id = drink_id;
        this.mCursor = cursor;
        this.mContext = context;
        this.drink_name = drink_name;
        this.time_drinked = time_drinked;
        this.drink_quantity = drink_quantity;
        this.drink_hydration = drink_hydration;
    }
    public class DrinksViewHolder extends RecyclerView.ViewHolder {
        public TextView drink, time, drinkml, drink_hydration;
        public LinearLayout deleteButton;
        public LinearLayout mainLayout;

        public DrinksViewHolder(View itemView) {
            super(itemView);
            drink = itemView.findViewById(R.id.drink_name);
            time = itemView.findViewById(R.id.time_drinked);
            drinkml = itemView.findViewById(R.id.drink_ml);
            drink_hydration = itemView.findViewById(R.id.drink_hydration);
            mainLayout = itemView.findViewById(R.id.mainLayout);

        }
    }
    @Override
    public DrinksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new DrinksViewHolder(view);
    }
    @Override
    public void onBindViewHolder(DrinksViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        // bind data here

        // we apply animation to views here
        // first lets create an animation for user photo
        //holder.mainLayout.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_transition_animation));

        // lets create the animation for the whole card
        // first lets create a reference to it
        // you ca use the previous same animation like the following

        // but i want to use a different one so lets create it ..
        holder.mainLayout.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation));

        holder.itemView.setTag(drink_id.get(position));
        holder.drink.setText(chooseDrink(String.valueOf(drink_name.get(position))));
        holder.time.setText(String.valueOf(time_drinked.get(position)));
        holder.drinkml.setText(String.valueOf(drink_quantity.get(position)));
        holder.drink_hydration.setText(String.valueOf(drink_hydration.get(position)));
    }
    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
    public void swapCursor(Cursor newCursor,int position) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;
        if (newCursor != null) {
            notifyItemRemoved(position);
        }
    }

    private int chooseDrink(String drink) {
        int drinkName = 1;
        if (drink.equals("1")) {
            drinkName = R.string.g_water;
        } else if (drink.equals("2")) {
            drinkName = R.string.g_milk;
        } else if (drink.equals("3")) {
            drinkName = R.string.g_juice;
        } else if (drink.equals("4")) {
            drinkName = R.string.g_coffe;
        } else if (drink.equals("5")) {
            drinkName = R.string.g_tea;
        } else if (drink.equals("6")) {
            drinkName = R.string.g_alcohol;
        }
        return drinkName;
    }

}

