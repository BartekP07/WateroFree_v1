package app.watero.waterofree.application.testhistory;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Member;
import java.util.ArrayList;

import app.watero.waterofree.R;
import app.watero.waterofree.database.MyDBHelper;

public class GroupAdp extends RecyclerView.Adapter<GroupAdp.ViewHolder> {

    private Activity activity;
    private Cursor cursor_history;
    private Cursor cursor_drinks;
    private Context mContext;

    ArrayList day_id;
    ArrayList day_name;
    ArrayList day_date;
    ArrayList day_sumary;
    ArrayList day_sumary_procentage;
    ArrayList day_active;


    //Child RecyclerView
    ArrayList drink_id;
    ArrayList drink_name;
    ArrayList time_drinked;
    ArrayList drink_quantity;
    ArrayList drink_hydration;

    GroupAdp(Activity activity, Cursor cursor_history, Cursor cursor_drinks, Context context, ArrayList day_id, ArrayList day_name, ArrayList day_date,
             ArrayList day_sumary, ArrayList day_sumary_procentage, ArrayList day_active, ArrayList drink_id,
             ArrayList drink_name, ArrayList time_drinked, ArrayList drink_quantity,ArrayList drink_hydration){
        this.activity = activity;
        this.cursor_history = cursor_history;
        this.cursor_drinks = cursor_drinks;
        this.mContext = context;
        this.day_id = day_id;
        this.day_name = day_name;
        this.day_date = day_date;
        this.day_sumary = day_sumary;
        this.day_sumary_procentage = day_sumary_procentage;
        this.day_active = day_active;
        this.drink_name = drink_name;
        this.time_drinked = time_drinked;
        this.drink_quantity =drink_quantity;
        this.drink_id = drink_id;
        this.drink_hydration = drink_hydration;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_group,parent,false);

        return new GroupAdp.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (!cursor_history.moveToPosition(position)) {
            return;
        }
        holder.list_row_group_layout.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation));

        holder.itemView.setTag(day_id.get(position));
        holder.day_name.setText(String.valueOf(day_name.get(position)));
        holder.day_date.setText(String.valueOf(day_date.get(position)));
        holder.sumary_day_ml.setText(String.valueOf(day_sumary.get(position)));
        //holder.day_summary_precentage.setText(String.valueOf(day_sumary_procentage.get(position)));


        MemberAdp adapterMember = new MemberAdp(cursor_drinks, mContext,drink_id,drink_name,time_drinked,drink_quantity,drink_hydration);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        holder.rvMember.setLayoutManager(layoutManager);
        holder.rvMember.setAdapter(adapterMember);
    }

    @Override
    public int getItemCount() {
        return cursor_history.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView day_name, day_date, sumary_day_ml, day_summary_precentage;
        public RecyclerView rvMember;
        public CardView list_row_group_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            day_name = itemView.findViewById(R.id.day_name);
            day_date = itemView.findViewById(R.id.day_date);
            sumary_day_ml = itemView.findViewById(R.id.sumary_day_ml);
            day_summary_precentage = itemView.findViewById(R.id.day_summary_precentage);
            rvMember = itemView.findViewById(R.id.rv_member);
            list_row_group_layout = itemView.findViewById(R.id.list_row_group_layout);
        }
    }

    public void setInnerData() {
        drink_id = new ArrayList<>();
        drink_name = new ArrayList<>();
        time_drinked = new ArrayList<>();
        drink_quantity = new ArrayList<>();
        drink_hydration = new ArrayList<>();
    }
}
