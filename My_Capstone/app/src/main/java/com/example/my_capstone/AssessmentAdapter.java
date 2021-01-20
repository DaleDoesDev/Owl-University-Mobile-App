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


public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.PartViewHolder> {

    class PartViewHolder extends RecyclerView.ViewHolder {
        private final TextView partItemView;

        private PartViewHolder(View itemView) {
            super(itemView);
            partItemView = itemView.findViewById(R.id.partTextView); //the list item's xml file
            partItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Assessment current = assessments.get(position);
                    Intent intent = new Intent(context, AssessmentDetailsActivity.class);
                    intent.putExtra("assessmentId", current.getAssessmentId());
                    intent.putExtra("courseAssessmentId", current.getCourseAssessmentId());
                    intent.putExtra("assessmentName", current.getAssessmentName());
                    intent.putExtra("assessmentStart", current.getStartDate());
                    intent.putExtra("assessmentEnd", current.getEndDate());
                    intent.putExtra("assessmentType", current.getType());
                    context.startActivity(intent);
                }
            });
        }

    }

    private final LayoutInflater mInflater;
    private final Context context;
    private List<Assessment> assessments; // Cached copy of assessments

    public AssessmentAdapter(Context context) {
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
        } else {
            // Covers the case of data not being ready yet.
            holder.partItemView.setText("Null");
        }
    }

    public void setWords(List<Assessment> a) {
        assessments = a;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (assessments != null)
            return assessments.size();
        else return 0;
    }

    public static class Report {

        private long assessmentId, courseAssessmentId;
        private String assessmentName, type, startDate, endDate;
        private int consoleAlerts;


    }
}
