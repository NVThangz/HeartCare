package com.example.heartcare.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heartcare.R;
import com.example.heartcare.object.HealthRecordItem;

import java.util.ArrayList;
import java.util.List;

public class EditHealthRecordAdapter extends RecyclerView.Adapter<EditHealthRecordAdapter.HealthRecordViewHolder> implements Filterable {
    private List<HealthRecordItem> healthRecordList;
    private List<HealthRecordItem> healthRecordListOld;

    public EditHealthRecordAdapter(List<HealthRecordItem> healthRecordList) {
        this.healthRecordList = healthRecordList;
        this.healthRecordListOld = healthRecordList;
    }

    @NonNull
    @Override
    public HealthRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_health_record_item, parent, false);
        return new HealthRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HealthRecordViewHolder holder, int position) {
        HealthRecordItem HealthRecordItem = healthRecordList.get(position);
        if (HealthRecordItem == null) return;
        holder.title.setText(HealthRecordItem.getTitle());
        holder.content.setText(HealthRecordItem.getContent());
    }

    @Override
    public int getItemCount() {
        if (healthRecordList != null) return healthRecordList.size();
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()) {
                    healthRecordList = healthRecordListOld;
                }
                else {
                    List <HealthRecordItem> list = new ArrayList<>();
                    for (HealthRecordItem HealthRecordItem : healthRecordListOld) {
                        list.add(HealthRecordItem);
                    }
                    healthRecordList = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = healthRecordList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                healthRecordList = (List<HealthRecordItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class HealthRecordViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public EditText content;

        public HealthRecordViewHolder(@NonNull View convertView) {
            super(convertView);
            title  = (TextView) convertView.findViewById(R.id.tv_title);
            content   = (EditText) convertView.findViewById(R.id.tv_content);
        }
    }
}
