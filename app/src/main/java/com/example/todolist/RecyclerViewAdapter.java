package com.example.todolist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.logging.Handler;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<String> titleList = new ArrayList<>();
    private ArrayList<String> thoughtList = new ArrayList<>();
    private ArrayList<String> dateList = new ArrayList<>();
    private Context mContext;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public RecyclerViewAdapter(ArrayList<String> titleList, ArrayList<String> thoughtList, ArrayList<String> dateList, Context mContext) {
        this.titleList = titleList;
        this.thoughtList = thoughtList;
        this.dateList = dateList;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem,
                parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        holder.titleText.setText(titleList.get(position));
        holder.thoughtText.setText(thoughtList.get(position));
        holder.dateText.setText(dateList.get(position));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),DataEntry.class);
                intent.putExtra("title",titleList.get(position));
                intent.putExtra("thought",thoughtList.get(position));
                intent.putExtra("date",dateList.get(position));
                view.getContext().startActivity(intent);
            }
        });
        holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                new AlertDialog.Builder(view.getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure")
                        .setMessage("Do you wnat to delete ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
    //                          Toast.makeText(mContext.getApplicationContext(),"Deleted",Toast.LENGTH_SHORT).show();
                                db.collection("Notes").document(titleList.get(position)).delete();
                                MainActivity.titleList.remove(position);
                                MainActivity.thoughtList.remove(position);
                                MainActivity.dateList.remove(position);
                                MainActivity.adapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                        .show();


             return true;

            }
        });

    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView titleText;
        TextView thoughtText;
        TextView dateText;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleId);
            thoughtText = itemView.findViewById(R.id.thoughtId);
            dateText = itemView.findViewById(R.id.dateId);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
