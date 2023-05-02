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
import com.example.heartcare.object.TodoItem;

import java.util.ArrayList;
import java.util.List;

public class TodoCalendarAdapter extends RecyclerView.Adapter<TodoCalendarAdapter.TodoCalendarViewHolder> implements Filterable {
    private List<TodoItem> todoList;
    private List<TodoItem> todoListOld;

    public TodoCalendarAdapter(List<TodoItem> todoList) {
        this.todoList = todoList;
        this.todoListOld = todoList;
    }

    @NonNull
    @Override
    public TodoCalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_todo_item, parent, false);
        return new TodoCalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoCalendarViewHolder holder, int position) {
        TodoItem TodoItem = todoList.get(position);
        if (TodoItem == null) return;
        holder.content.setText(TodoItem.getContent());
        holder.time.setText(TodoItem.getTime());
    }

    @Override
    public int getItemCount() {
        if (todoList != null) return todoList.size();
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()) {
                    todoList = todoListOld;
                }
                else {
                    List <TodoItem> list = new ArrayList<>();
                    for (TodoItem TodoItem : todoListOld) {
                        list.add(TodoItem);
                    }
                    todoList = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = todoList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                todoList = (List<TodoItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class TodoCalendarViewHolder extends RecyclerView.ViewHolder {
        private TextView content;
        private TextView time;

        public TodoCalendarViewHolder(@NonNull View convertView) {
            super(convertView);
            content = (TextView) convertView.findViewById(R.id.tv_content);
            time = (TextView) convertView.findViewById(R.id.tv_time);
        }
    }
}
