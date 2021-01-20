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


public class TermAdapter extends RecyclerView.Adapter<TermAdapter.PartViewHolder> {

    class PartViewHolder extends RecyclerView.ViewHolder {
        private final TextView partItemView;

        private PartViewHolder(View itemView) {
            super(itemView);
            partItemView = itemView.findViewById(R.id.partTextView); //the list item's xml file
            partItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Term current = terms.get(position);
                    Intent intent = new Intent(context, TermDetailsActivity.class);
                    intent.putExtra("termId", current.getTermId());
                    intent.putExtra("termTitle", current.getTitle());
                    intent.putExtra("termStart", current.getStartDate());
                    intent.putExtra("termEnd", current.getEndDate());
                    intent.putExtra("termCurrent", current.getCurrent());
                    context.startActivity(intent);
                }
            });
        }

    }

    private final LayoutInflater mInflater;
    private final Context context;
    private List<Term> terms; // Cached copy of terms

    public TermAdapter(Context context) {
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
        if (terms != null) {
            Term current = terms.get(position);
            holder.partItemView.setText(current.getTitle()+System.getProperty("line.separator")+ current.getStartDate()+ "  -  "+current.getEndDate());
        } else {
            // Covers the case of data not being ready yet.
            holder.partItemView.setText("Null");
        }
    }

    public void setWords(List<Term> t) {
        terms = t;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if (terms != null)
            return terms.size();
        else return 0;
    }

}
