package com.example.my_capstone;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class TermDetailsAdapter extends RecyclerView.Adapter<TermDetailsAdapter.PartViewHolder> {

    class PartViewHolder extends RecyclerView.ViewHolder {
        private final TextView partItemView;

        private PartViewHolder(View itemView) {
            super(itemView);
            partItemView = itemView.findViewById(R.id.partTextView); //the list item's xml file id
        }

    }

    private final LayoutInflater mInflater;
    private final Context context;
    private List<Course> courses; // Cached copy of courses


    public TermDetailsAdapter(Context context) {
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
            Course current = courses.get(position);
            holder.partItemView.setText(current.getName()+System.getProperty("line.separator")+ current.getStartDate()+ "  -  "+current.getEndDate());
        }
    }

    public void setCourseWords(List<Course> c) {
        courses = c;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (courses != null)
            return courses.size();
        else return 0;
    }

}
