package com.example.heartcare.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.heartcare.FindNotesQuery;
import com.example.heartcare.FindNotificationsWithEmailQuery;
import com.example.heartcare.R;
import com.example.heartcare.adapter.NotificationsAdapter;
import com.example.heartcare.backend.Backend;
import com.example.heartcare.object.NotificationItem;
import com.example.heartcare.utilities.DateFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;

    private RecyclerView recyclerNotifications;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
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
        recyclerNotifications = rootView.findViewById(R.id.recycler_notifications);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        map();

        setRecyclerView();
        return rootView;
    }

    private void setRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerNotifications.setLayoutManager(linearLayoutManager);

        NotificationsAdapter adapter = new NotificationsAdapter(getListNotification());
        recyclerNotifications.setAdapter(adapter);
    }

    private List<NotificationItem> getListNotification() {
        /*
        *
        *   Thêm thông báo
        *
        * */

        ArrayList<String> times = new ArrayList<String>();
        ArrayList<String> titles = new ArrayList<String>();
        ArrayList<String> contents = new ArrayList<String>();
        FindNotificationsWithEmailQuery.Data data = Backend.getNotificationsWithEmail();
        if(data.findNotificationsWithEmail.size() != 0) {
            for (int i = 0; i < data.findNotificationsWithEmail.size(); i++) {
                Date createdDate = DateFormat.ISO8601toDate(data.findNotificationsWithEmail.get(i).createdAt.toString());
//            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
//            String startTime = formatter.format(StartDate);
                times.add(createdDate.toLocaleString());
                titles.add(data.findNotificationsWithEmail.get(i).title);
                contents.add(data.findNotificationsWithEmail.get(i).content);
            }
        } else {
            times.add("");
            titles.add("No notification");
            contents.add("No notification");
        }

        List <NotificationItem> notificationItems = new ArrayList<>();
        for(int i = 0; i < data.findNotificationsWithEmail.size(); i++) {
            NotificationItem notificationItem = new NotificationItem(times.get(i), titles.get(i), contents.get(i));
            notificationItems.add(notificationItem);
        }
        return notificationItems;
    }

}