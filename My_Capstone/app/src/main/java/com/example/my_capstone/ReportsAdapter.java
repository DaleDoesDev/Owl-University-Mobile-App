package com.example.my_capstone;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.PartViewHolder> {

    class PartViewHolder extends RecyclerView.ViewHolder {
        private final TextView partItemView;

        private PartViewHolder(View itemView) {
            super(itemView);
            partItemView = itemView.findViewById(R.id.partTextView); //the list item's xml file
        }
    }

    private final LayoutInflater mInflater;
    private final Context context;
    private List<CourseStatusReport> courses; // Cached copy of courses

    public ReportsAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public PartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.activity_list_item, parent, false);
        return new PartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PartViewHolder holder, int position) {
        if (courses != null) {
            CourseStatusReport current = courses.get(position);
            holder.partItemView.setText(current.getTitle());
        } else {
            // Covers the case of data not being ready yet.
            holder.partItemView.setText("Null");
        }
    }

    public void setWords(List<CourseStatusReport> t) {
        courses = t;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (courses != null)
            return courses.size();
        else return 0;
    }

}
