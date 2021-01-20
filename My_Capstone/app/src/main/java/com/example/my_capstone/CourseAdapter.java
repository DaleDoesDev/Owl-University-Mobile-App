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


public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.PartViewHolder> {

    class PartViewHolder extends RecyclerView.ViewHolder {
        private final TextView partItemView;

        private PartViewHolder(View itemView) {
            super(itemView);
            partItemView = itemView.findViewById(R.id.partTextView); //the list item's xml file
            partItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Course current = courses.get(position);
                    Intent intent = new Intent(context, CourseDetailsActivity.class);
                    intent.putExtra("courseId", current.getCourseId());
                    intent.putExtra("courseTermId", current.getCourseTermId());
                    intent.putExtra("courseName", current.getName());
                    intent.putExtra("courseStart", current.getStartDate());
                    intent.putExtra("courseEnd", current.getEndDate());
                    intent.putExtra("courseStatus", current.getStatus());
                    intent.putExtra("mentorName", current.getMentorName());
                    intent.putExtra("mentorPhone", current.getMentorPhone());
                    intent.putExtra("mentorEmail", current.getMentorEmail());
                    intent.putExtra("courseNotes", current.getNote());
                    context.startActivity(intent);
                }
            });
        }

    }

    private final LayoutInflater mInflater;
    private final Context context;
    private List<Course> courses; // Cached copy of courses

    public CourseAdapter(Context context) {
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
        } else {
            // Covers the case of data not being ready yet.
            holder.partItemView.setText("Null");
        }
    }

    public void setWords(List<Course> t) {
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
