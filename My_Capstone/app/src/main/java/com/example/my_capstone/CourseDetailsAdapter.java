package com.example.my_capstone;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class CourseDetailsAdapter extends RecyclerView.Adapter<CourseDetailsAdapter.PartViewHolder> {

    class PartViewHolder extends RecyclerView.ViewHolder {
        private final TextView partItemView;

        private PartViewHolder(View itemView) {
            super(itemView);
            partItemView = itemView.findViewById(R.id.partTextView); //the list item's xml file id
        }

    }

    private final LayoutInflater mInflater;
    private final Context context;
    private List<Assessment> assessments; // Cached copy of assessments


    public CourseDetailsAdapter(Context context) {
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
        if (assessments != null) {
            Assessment current = assessments.get(position);
            holder.partItemView.setText(current.getAssessmentName()+System.getProperty("line.separator")+ current.getStartDate()+ "  -  "+current.getEndDate());
        }
    }

    public void setAssessmentWords(List<Assessment> a) {
        assessments = a;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (assessments != null)
            return assessments.size();
        else return 0;
    }

}
