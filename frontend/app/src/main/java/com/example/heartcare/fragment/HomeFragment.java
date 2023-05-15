package com.example.heartcare.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.heartcare.R;
import com.example.heartcare.activity.ChangePassword;
import com.example.heartcare.activity.EditHealthRecord;
import com.example.heartcare.activity.HealthConsultation;
import com.example.heartcare.activity.HealthRecord;
import com.example.heartcare.activity.HeartRateStatistics;
import com.example.heartcare.activity.MeasureHeartRate;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;

    private ConstraintLayout btnMeasureHeartRate;
    private ConstraintLayout btnHeartRateStatistics;
    private ConstraintLayout btnHealthConsultation;
    private TextView tvMeasureHeartRate;
    private TextView textViewFullName;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        btnHealthConsultation = rootView.findViewById(R.id.btn_health_consultation);
        btnMeasureHeartRate = rootView.findViewById(R.id.btn_measure_heart_rate);
        btnHeartRateStatistics = rootView.findViewById(R.id.btn_heart_rate_statistics);
        tvMeasureHeartRate = rootView.findViewById(R.id.tv_measure_heart_rate);
        textViewFullName = rootView.findViewById(R.id.tv_full_name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        map();

        /*
            Ghép backend
         */
        
        textViewFullName.setText("Bùi Minh Hoạt");


        clickBtnHealthConsultation();
        clickBtnMeasureHeartRate();
        clickBtnHeartRateStatistics();
        return rootView;
    }

    private void clickBtnHealthConsultation() {
        btnHealthConsultation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), HealthConsultation.class);
                Intent intent = new Intent(getActivity(), HealthRecord.class);
                startActivity(intent);
            }
        });
    }

    private void clickBtnHeartRateStatistics() {
        btnHeartRateStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HeartRateStatistics.class);
                startActivity(intent);
            }
        });
    }

    private void clickBtnMeasureHeartRate() {
        btnMeasureHeartRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MeasureHeartRate.class);
                startActivity(intent);
            }
        });
    }
}