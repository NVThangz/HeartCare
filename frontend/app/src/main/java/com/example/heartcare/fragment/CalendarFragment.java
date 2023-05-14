package com.example.heartcare.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.heartcare.R;
import com.example.heartcare.activity.CreateTodoActivity;
import com.example.heartcare.adapter.TodoCalendarAdapter;
import com.example.heartcare.object.TodoItem;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;

    private CalendarView calendarView;

    private RecyclerView todoCalendarRecycler;

    private ImageView btnAddTodo;
    private FrameLayout imgWhiteFading;

    private FrameLayout calendarFragment;

    private ConstraintLayout bottomSheet;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void map() {
        calendarView = rootView.findViewById(R.id.calendar_view);
        todoCalendarRecycler = rootView.findViewById(R.id.recycler_todo);
        btnAddTodo = rootView.findViewById(R.id.btn_add_todo);
        imgWhiteFading = rootView.findViewById(R.id.img_white_fading);
        calendarFragment = rootView.findViewById(R.id.calendar_fragment);
        bottomSheet = rootView.findViewById(R.id.bottom_sheet);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        map();
        clickBtnAddTodo();
        setCalendarView();
        setBottomSheetDialog();
        eventTodoCalendarRecycler();
        return rootView;
    }

    private void clickBtnAddTodo() {
        int day, month, year;
        btnAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long selectedDateInMillis = calendarView.getDate();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(selectedDateInMillis);

                // Lấy ngày, tháng, năm hiện tại
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int date = calendar.get(Calendar.DATE);
                Intent intent = new Intent(getActivity(), CreateTodoActivity.class);
                intent.putExtra("DATE", date);
                intent.putExtra("MONTH", month);
                intent.putExtra("YEAR", year);
                startActivity(intent);
            }
        });
    }

    private void eventTodoCalendarRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        todoCalendarRecycler.setLayoutManager(linearLayoutManager);

        TodoCalendarAdapter todoCalendarAdapter = new TodoCalendarAdapter(getListUsers());
        todoCalendarRecycler.setAdapter(todoCalendarAdapter);
    }

    private List<TodoItem> getListUsers() {
        /*
            contents: Sự kiện của ngày hôm nay
            times: Thời gian
         */
        String[] contents = {"Go to hospital", "Take medicines", "Meet docter"};
        String[] times = {"08:00 AM - 10:00AM","12:00 AM","02:00 PM - 03:00PM"};

        List <TodoItem> todoItems = new ArrayList<>();
        for(int i = 0; i < contents.length; i++) {
            TodoItem todoItem = new TodoItem(contents[i],times[i]);
            todoItems.add(todoItem);
        }
        return todoItems;
    }


    private void setBottomSheetDialog() {
        View bottomSheet = rootView.findViewById(R.id.bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setPeekHeight(950);
        behavior.setMaxHeight(2060);
        behavior.setHideable(false);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // Xử lý khi trạng thái của Bottom Sheet thay đổi
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
//                    btnAddTodo.setVisibility(View.GONE);
//                    imgWhiteFading.setVisibility(View.GONE);
//                    bottomSheet.setBackgroundResource(R.color.white);
                } else {
//                    bottomSheet.setBackgroundResource(R.drawable.bottom_box_white);
//                    btnAddTodo.setVisibility(View.VISIBLE);
//                    imgWhiteFading.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // Xử lý khi Bottom Sheet đang được vuốt lên hoặc xuống
            }
        });
    }



    private void setCalendarView() {
    }
}