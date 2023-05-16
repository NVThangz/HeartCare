package com.example.heartcare.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.heartcare.FindNotesQuery;
import com.example.heartcare.R;
import com.example.heartcare.activity.CreateTodoActivity;
import com.example.heartcare.adapter.TodoCalendarAdapter;
import com.example.heartcare.backend.Backend;
import com.example.heartcare.object.TodoItem;
import com.example.heartcare.utilities.DateFormat;
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

    private int chooseDate;
    private int chooseMonth;
    private int chooseYear;

    private View rootView;

    private CalendarView calendarView;

    private RecyclerView todoCalendarRecycler;

    private ImageView btnAddTodo;
    private FrameLayout imgWhiteFading;

    private FrameLayout calendarFragment;

    private ConstraintLayout bottomSheet;

    private Calendar calendar;
    private static final int CREATE_TODO_REQUEST_CODE = 1;

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
        setCalendar();
        clickBtnAddTodo();
        setCalendarView();
        setBottomSheetDialog();
        eventTodoCalendarRecycler();
        return rootView;
    }

    // Phương thức để tải lại dữ liệu trong Calendar Fragment
    private void reloadData() {
        // Thực hiện các thao tác cần thiết để cập nhật giao diện
        // Ví dụ: clear các items cũ, tạo lại các items mới
        // ...

        // Gọi phương thức onCreateView() để tạo giao diện mới
//        onCreateView(LayoutInflater.from(getContext()), null, null);
        eventTodoCalendarRecycler();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_TODO_REQUEST_CODE && resultCode == RESULT_OK) {
            // Thực hiện tải lại dữ liệu trong Calendar Fragment
            reloadData();
        }
    }

    private void setCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(calendarView.getDate());

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int date) {
                setDate(date, month + 1, year);
                eventTodoCalendarRecycler();
            }
        });

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DATE);
        setDate(date, month, year);
    }

    private void setDate(int date, int month, int year) {
        chooseDate = date;
        chooseMonth = month;
        chooseYear = year;
    }

    private void clickBtnAddTodo() {
        int day, month, year;
        btnAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateTodoActivity.class);
                intent.putExtra("DATE", chooseDate);
                intent.putExtra("MONTH", chooseMonth);
                intent.putExtra("YEAR", chooseYear);
//                startActivity(intent);
                startActivityForResult(intent, CREATE_TODO_REQUEST_CODE);
            }
        });
    }

    private void eventTodoCalendarRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        todoCalendarRecycler.setLayoutManager(linearLayoutManager);

        TodoCalendarAdapter todoCalendarAdapter = new TodoCalendarAdapter(getListUsers(), getActivity());
        todoCalendarRecycler.setAdapter(todoCalendarAdapter);
    }

    private List<TodoItem> getListUsers() {
        /*
            contents: Sự kiện của ngày hôm nay
            times: Thời gian
         */

        ArrayList<Integer> ids = new ArrayList<Integer>();
        ArrayList<String> times = new ArrayList<String>();
        ArrayList<String> contents = new ArrayList<String>();
        Date date = new Date(chooseYear-1900,chooseMonth-1,chooseDate);
        FindNotesQuery.Data data = Backend.getNotes(date);
        for (int i = 0; i < data.findNotes.size(); i++) {
            Date StartDate = DateFormat.ISO8601toDate(data.findNotes.get(i).startDate.toString());
            Date EndDate = DateFormat.ISO8601toDate(data.findNotes.get(i).endDate.toString());
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
            String startTime = formatter.format(StartDate);
            String endTime = formatter.format(EndDate);
            times.add(startTime + " - " + endTime);
            contents.add(data.findNotes.get(i).content);
            ids.add(data.findNotes.get(i).id);
        }
//        String[] contents = {"Go to hospital", "Take medicines", "Meet docter"};
//        String[] times = {"08:00 AM - 10:00AM","12:00 AM","02:00 PM - 03:00PM"};

        List <TodoItem> todoItems = new ArrayList<>();
        for(int i = 0; i < contents.size(); i++) {
            TodoItem todoItem = new TodoItem(ids.get(i), contents.get(i),times.get(i));
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

    public static void showDialogDelete(Activity activity, int id) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_item_bottom_sheet_layout);

        LinearLayout editLayout = dialog.findViewById(R.id.btn_delete);
        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();



                /*
                *  Xóa todo item ở đây
                *

                * */

                Toast.makeText(activity, activity.getResources().getString(R.string.item_deleted),Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
}