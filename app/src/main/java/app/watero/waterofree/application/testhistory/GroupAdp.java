package app.watero.waterofree.application.testhistory;

import android.app.Activity;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.watero.waterofree.R;

public class GroupAdp extends RecyclerView.Adapter<GroupAdp.ViewHolder> {

    private Activity activity;
    ArrayList<String> arrayListGroup;
    private Cursor cursor;

    GroupAdp(Activity activity, ArrayList<String> arrayListGroup, Cursor cursor){
        this.activity = activity;
        this.arrayListGroup = arrayListGroup;
        this.cursor = cursor;
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
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView day_name, day_date, sumary_day_ml, day_summary_precentage;
        public ImageView history_day_active;
        public RecyclerView rvMember;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            day_name = itemView.findViewById(R.id.day_name);
            day_date = itemView.findViewById(R.id.day_date);
            sumary_day_ml = itemView.findViewById(R.id.sumary_day_ml);
            day_summary_precentage = itemView.findViewById(R.id.day_summary_precentage);
            history_day_active = itemView.findViewById(R.id.history_day_active);
            rvMember = itemView.findViewById(R.id.rv_member);
        }
    }
}
