package com.example.android.hockeyapp;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import static com.example.android.hockeyapp.ApplicationClass.hideViews;
import static com.example.android.hockeyapp.ApplicationClass.incrementPoints;
import static com.example.android.hockeyapp.ApplicationClass.setPieData;
import static com.example.android.hockeyapp.ApplicationClass.showViews;


/**
 * A simple {@link Fragment} subclass.
 */
public class Frag4GoalShots extends Fragment {

    PieChart pieChart;
    TextView tvGreenGoalShots1, tvYellowGoalShots1, tvOrangeGoalShots1,
            tvBlueGoalShots1, tvRedGoalShots1;

    TextView tvGreenGoalShots2, tvYellowGoalShots2, tvOrangeGoalShots2,
            tvBlueGoalShots2, tvRedGoalShots2;

    RadioButton rdbFirstHalfGC, rdbSecondHalfGC;
    LinearLayout frmHalf1GC, frmHalf2GC;
    private TextView tvHalf1TeamGoalShots;
    private TextView tvHalf2TeamGoalShots;
    private SetGCScoreListener setGCScoreListener;


    public Frag4GoalShots() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag4_goal_shots, container, false);
        rdbFirstHalfGC = view.findViewById(R.id.rdbFirstHalfGC);
        rdbSecondHalfGC = view.findViewById(R.id.rdbSecondHalfGC);

        pieChart = view.findViewById(R.id.pieChartGoalShots);

        frmHalf1GC = view.findViewById(R.id.frmHalf1GC);
        frmHalf2GC = view.findViewById(R.id.frmHalf2GC);

        tvHalf1TeamGoalShots = view.findViewById(R.id.tvHalf1TeamGoalShots);
        tvHalf2TeamGoalShots = view.findViewById(R.id.tvHalf2TeamGoalShots);

        tvGreenGoalShots1 = view.findViewById(R.id.tvGreenGoalShots1);
        tvYellowGoalShots1 = view.findViewById(R.id.tvYellowGoalShots1);
        tvOrangeGoalShots1 = view.findViewById(R.id.tvOrangeGoalShots1);
        tvRedGoalShots1 = view.findViewById(R.id.tvRedGoalShots1);
        tvBlueGoalShots1 = view.findViewById(R.id.tvBlueGoalShots1);

        tvGreenGoalShots2 = view.findViewById(R.id.tvGreenGoalShots2);
        tvYellowGoalShots2 = view.findViewById(R.id.tvYellowGoalShots2);
        tvOrangeGoalShots2 = view.findViewById(R.id.tvOrangeGoalShots2);
        tvRedGoalShots2 = view.findViewById(R.id.tvRedGoalShots2);
        tvBlueGoalShots2 = view.findViewById(R.id.tvBlueGoalShots2);

        tvGreenGoalShots1.setTextColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        tvYellowGoalShots1.setTextColor(ColorTemplate.VORDIPLOM_COLORS[1]);
        tvOrangeGoalShots1.setTextColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        tvBlueGoalShots1.setTextColor(ColorTemplate.VORDIPLOM_COLORS[3]);
        tvRedGoalShots1.setTextColor(ColorTemplate.VORDIPLOM_COLORS[4]);

        tvGreenGoalShots2.setTextColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        tvYellowGoalShots2.setTextColor(ColorTemplate.VORDIPLOM_COLORS[1]);
        tvOrangeGoalShots2.setTextColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        tvBlueGoalShots2.setTextColor(ColorTemplate.VORDIPLOM_COLORS[3]);
        tvRedGoalShots2.setTextColor(ColorTemplate.VORDIPLOM_COLORS[4]);

        pieChart.setData(setPieData("Circle Penetrations"));
        pieChart.setRotationEnabled(false);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutBounce);

        getMatchHalf();

        rdbFirstHalfGC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getMatchHalf();
            }
        });

        rdbSecondHalfGC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getMatchHalf();
            }
        });


        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                getMatchHalf();

                switch (String.valueOf(h.getX())) {
                    case "0.0":
                        //Green
                        if (rdbFirstHalfGC.isChecked()) {
                            incrementPoints(tvGreenGoalShots1, tvHalf1TeamGoalShots);
                        } else if (rdbSecondHalfGC.isChecked()) {
                            incrementPoints(tvGreenGoalShots2, tvHalf2TeamGoalShots);
                        }
                        break;

                    case "1.0":
                        //Yellow
                        if (rdbFirstHalfGC.isChecked()) {
                            incrementPoints(tvYellowGoalShots1, tvHalf1TeamGoalShots);
                        } else if (rdbSecondHalfGC.isChecked()) {
                            incrementPoints(tvYellowGoalShots2, tvHalf2TeamGoalShots);
                        }
                        break;

                    case "2.0":
                        //Orange
                        if (rdbFirstHalfGC.isChecked()) {
                            incrementPoints(tvOrangeGoalShots1, tvHalf1TeamGoalShots);
                        } else if (rdbSecondHalfGC.isChecked()) {
                            incrementPoints(tvOrangeGoalShots2, tvHalf2TeamGoalShots);
                        }
                        break;
                    case "3.0":
                        //Blue
                        if (rdbFirstHalfGC.isChecked()) {
                            incrementPoints(tvBlueGoalShots1, tvHalf1TeamGoalShots);
                        } else if (rdbSecondHalfGC.isChecked()) {
                            incrementPoints(tvBlueGoalShots2, tvHalf2TeamGoalShots);
                        }
                        break;
                    case "4.0":
                        //Red
                        if (rdbFirstHalfGC.isChecked()) {
                            incrementPoints(tvRedGoalShots1,  tvHalf1TeamGoalShots);
                        } else if (rdbSecondHalfGC.isChecked()) {
                            incrementPoints(tvRedGoalShots2, tvHalf2TeamGoalShots);
                        }
                        break;
                }

                setGCScoreListener.onSetGCScore();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        return view;
    }

    private void getMatchHalf() {
        if (rdbFirstHalfGC.isChecked()) {
            showViews(frmHalf1GC);
            hideViews(frmHalf2GC);
        } else if (rdbSecondHalfGC.isChecked()) {
            showViews(frmHalf2GC);
            hideViews(frmHalf1GC);
        }
    }

    public interface SetGCScoreListener {
        void onSetGCScore();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            setGCScoreListener = (Frag4GoalShots.SetGCScoreListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement interface"
                    + " onSetGCScore");
        }
    }
}
