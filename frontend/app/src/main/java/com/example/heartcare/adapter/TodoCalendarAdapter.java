package com.example.heartcare.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heartcare.R;
import com.example.heartcare.fragment.CalendarFragment;
import com.example.heartcare.object.TodoItem;

import java.util.ArrayList;
import java.util.List;

public class TodoCalendarAdapter extends RecyclerView.Adapter<TodoCalendarAdapter.TodoCalendarViewHolder> implements Filterable {
    private List<TodoItem> todoList = new ArrayList<>();
    private List<TodoItem> todoListOld = new ArrayList<>();

    private Activity activity;

    CalendarFragment calendarFragment;
    public TodoCalendarAdapter(CalendarFragment calendarFragment, List<TodoItem> todoList, Activity activity) {
        this.calendarFragment = calendarFragment;
        this.todoList = todoList;
        this.todoListOld = todoList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public TodoCalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_todo_item, parent, false);
        return new TodoCalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoCalendarViewHolder holder, int position) {
        TodoItem todoItem = todoList.get(position);
        if (todoItem == null) return;
        holder.content.setText(todoItem.getContent());
        holder.time.setText(todoItem.getTime());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    System.out.println(todoItem.getContent() + "long" + ' ' + todoItem.getId());

                    calendarFragment.showDialogDelete(activity, todoItem.getId());
                }
                else {
                    System.out.println(todoItem.getContent() + "click");
                }
            }
        });
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

    public class TodoCalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        private ItemClickListener itemClickListener;
        public ConstraintLayout item;
        private TextView content;
        private TextView time;

        public TodoCalendarViewHolder(@NonNull View convertView) {
            super(convertView);
            content = (TextView) convertView.findViewById(R.id.tv_content);
            time    = (TextView) convertView.findViewById(R.id.tv_time);
            item    = (ConstraintLayout) convertView.findViewById(R.id.recyclerview_todo_item);
            convertView.setOnClickListener(this);
            convertView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),true);
            return true;
        }
    }
}
