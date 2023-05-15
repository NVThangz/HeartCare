package com.example.heartcare.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heartcare.R;
import com.example.heartcare.object.NotificationItem;

import java.util.ArrayList;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder> implements Filterable {
    private List<NotificationItem> notificationList;
    private List<NotificationItem> notificationListOld;

    public NotificationsAdapter(List<NotificationItem> notificationList) {
        this.notificationList = notificationList;
        this.notificationListOld = notificationList;
    }

    @NonNull
    @Override
    public NotificationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new NotificationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsViewHolder holder, int position) {
        NotificationItem NotificationItem = notificationList.get(position);
        if (NotificationItem == null) return;
        holder.time.setText(NotificationItem.getTime());
        holder.title.setText(NotificationItem.getTitle());
        holder.content.setText(NotificationItem.getContent());
    }

    @Override
    public int getItemCount() {
        if (notificationList != null) return notificationList.size();
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()) {
                    notificationList = notificationListOld;
                }
                else {
                    List <NotificationItem> list = new ArrayList<>();
                    for (NotificationItem NotificationItem : notificationListOld) {
                        list.add(NotificationItem);
                    }
                    notificationList = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = notificationList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notificationList = (List<NotificationItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class NotificationsViewHolder extends RecyclerView.ViewHolder {
        private TextView time;
        private TextView title;
        private TextView content;

        public NotificationsViewHolder(@NonNull View convertView) {
            super(convertView);
            time  = (TextView) convertView.findViewById(R.id.time);
            title  = (TextView) convertView.findViewById(R.id.title);
            content   = (TextView) convertView.findViewById(R.id.content);
        }
    }
}
