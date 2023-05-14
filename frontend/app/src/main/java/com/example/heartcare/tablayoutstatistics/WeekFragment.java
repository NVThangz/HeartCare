package com.example.heartcare.tablayoutstatistics;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.heartcare.R;
import com.example.heartcare.object.MyMarkerView;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeekFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekFragment extends Fragment implements OnChartValueSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView tvBpmMin;
    private TextView tvBpmMax;
    private TextView tvBpmAvg;

    private View rootView;

    private LineChart mChart;

    public WeekFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeekFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeekFragment newInstance(String param1, String param2) {
        WeekFragment fragment = new WeekFragment();
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
        mChart = rootView.findViewById(R.id.line_chart);
        tvBpmMin = rootView.findViewById(R.id.tv_bpm_min);
        tvBpmMax = rootView.findViewById(R.id.tv_bpm_max);
        tvBpmAvg = rootView.findViewById(R.id.tv_bpm_avg);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_day, container, false);
        map();
        setChart();
        return rootView;
    }

    private void setChart() {
        mChart.getDescription().setEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setTouchEnabled(true);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        MyMarkerView mv = new MyMarkerView(this.getActivity(), R.layout.custom_marker_view);
        mv.setChartView(mChart);
        mChart.setMarker(mv);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f);

        final List<String> xLabel = new ArrayList<>();
        xLabel.add(getResources().getString(R.string.mon));
        xLabel.add(getResources().getString(R.string.tue));
        xLabel.add(getResources().getString(R.string.wed));
        xLabel.add(getResources().getString(R.string.thu));
        xLabel.add(getResources().getString(R.string.fri));
        xLabel.add(getResources().getString(R.string.sat));
        xLabel.add(getResources().getString(R.string.sun));

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);

        mChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xLabel));

        LineData lineDatas = new LineData();
        lineDatas.addDataSet((ILineDataSet) dataChart());

        mChart.setData(lineDatas);
        mChart.invalidate();
    }


    @SuppressLint("SetTextI18n")
    private DataSet dataChart() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(-1, 80));


        /*
            Nhận giá trị trung bình (tuần hiện tại)
            entries.add(new Entry(giờ + phút / 60, nhịp tim trung bình));
            0 là thứ 2
            1 là thứ 3
            ...
            5 là thứ 7
            6 là CN
         */

        entries.add(new Entry(0, 70));
        entries.add(new Entry(2, 70));
        entries.add(new Entry(3, 60));
        entries.add(new Entry(4, 30));
        entries.add(new Entry(5, 40));
        entries.add(new Entry(6, 10));
        int maxBpm = 0;
        int minBpm = 300;
        int sumBpm = 0;
        int avgBpm;
        for (int i = 1; i < entries.size(); ++i) {
            Entry entry = entries.get(i);
            maxBpm = (int) Math.max(maxBpm, entry.getY());
            minBpm = (int) Math.min(minBpm, entry.getY());
            sumBpm += (int) entry.getY();
        }

        if (entries.size() - 1 == 0) {
            avgBpm = 0;
        } else {
            avgBpm = Math.round((float) sumBpm / (entries.size() - 1));
        }
        tvBpmMin.setText(Integer.toString(minBpm));
        tvBpmMax.setText(Integer.toString(maxBpm));
        tvBpmAvg.setText(Integer.toString(avgBpm));


        LineDataSet set = new LineDataSet(entries, "BPM");
        set.setColor(Color.rgb(250, 57, 57));
        set.setLineWidth(3f);
        set.setCircleColor(Color.rgb(250, 57, 57));
        set.setCircleRadius(5f);
        set.setFillColor(Color.rgb(250, 57, 57));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(false);

        set.setDrawFilled(true);
        set.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return mChart.getAxisLeft().getAxisMinimum();
            }
        });

        Drawable drawable = ContextCompat.getDrawable(this.getActivity(), R.drawable.fade_red);
        set.setFillDrawable(drawable);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return set;
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}