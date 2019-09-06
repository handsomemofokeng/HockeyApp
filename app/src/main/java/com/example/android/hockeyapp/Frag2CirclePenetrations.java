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
public class Frag2CirclePenetrations extends Fragment {

    PieChart pieChart;

    TextView tvHalf1TeamCirclePenetration, tvHalf2TeamCirclePenetration;

    TextView tvGreenCirclePenetrations1, tvYellowCirclePenetrations1, tvOrangeCirclePenetrations1,
            tvBlueCirclePenetrations1, tvRedCirclePenetrations1;

    TextView tvGreenCirclePenetrations2, tvYellowCirclePenetrations2, tvOrangeCirclePenetrations2,
            tvBlueCirclePenetrations2, tvRedCirclePenetrations2;

    RadioButton rdbFirstHalfCP, rdbSecondHalfCP;
    LinearLayout frmHalf1CP, frmHalf2CP;
    private SetCPScoreListener setPCScoreListener;

    public Frag2CirclePenetrations() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag2_circle_penetrations, container, false);

        rdbFirstHalfCP = view.findViewById(R.id.rdbFirstHalfCP);
        rdbSecondHalfCP = view.findViewById(R.id.rdbSecondHalfCP);

        pieChart = view.findViewById(R.id.pieChartCirclePenetrations);

        frmHalf1CP = view.findViewById(R.id.frmHalf1CP);
        frmHalf2CP = view.findViewById(R.id.frmHalf2CP);

        tvHalf1TeamCirclePenetration = view.findViewById(R.id.tvHalf1TeamCirclePenetrations);
        tvHalf2TeamCirclePenetration = view.findViewById(R.id.tvHalf2TeamCirclePenetrations);

        tvGreenCirclePenetrations1 = view.findViewById(R.id.tvGreenCirclePenetrations1);
        tvYellowCirclePenetrations1 = view.findViewById(R.id.tvYellowCirclePenetrations1);
        tvOrangeCirclePenetrations1 = view.findViewById(R.id.tvOrangeCirclePenetrations1);
        tvRedCirclePenetrations1 = view.findViewById(R.id.tvRedCirclePenetrations1);
        tvBlueCirclePenetrations1 = view.findViewById(R.id.tvBlueCirclePenetrations1);

        tvGreenCirclePenetrations2 = view.findViewById(R.id.tvGreenCirclePenetrations2);
        tvYellowCirclePenetrations2 = view.findViewById(R.id.tvYellowCirclePenetrations2);
        tvOrangeCirclePenetrations2 = view.findViewById(R.id.tvOrangeCirclePenetrations2);
        tvRedCirclePenetrations2 = view.findViewById(R.id.tvRedCirclePenetrations2);
        tvBlueCirclePenetrations2 = view.findViewById(R.id.tvBlueCirclePenetrations2);

        tvGreenCirclePenetrations1.setTextColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        tvYellowCirclePenetrations1.setTextColor(ColorTemplate.VORDIPLOM_COLORS[1]);
        tvOrangeCirclePenetrations1.setTextColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        tvBlueCirclePenetrations1.setTextColor(ColorTemplate.VORDIPLOM_COLORS[3]);
        tvRedCirclePenetrations1.setTextColor(ColorTemplate.VORDIPLOM_COLORS[4]);

        tvGreenCirclePenetrations2.setTextColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        tvYellowCirclePenetrations2.setTextColor(ColorTemplate.VORDIPLOM_COLORS[1]);
        tvOrangeCirclePenetrations2.setTextColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        tvBlueCirclePenetrations2.setTextColor(ColorTemplate.VORDIPLOM_COLORS[3]);
        tvRedCirclePenetrations2.setTextColor(ColorTemplate.VORDIPLOM_COLORS[4]);

        pieChart.setData(setPieData("Circle Penetrations"));
        pieChart.setRotationEnabled(false);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutBounce);

        getMatchHalf();

        rdbFirstHalfCP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getMatchHalf();
            }
        });

        rdbSecondHalfCP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                        if (rdbFirstHalfCP.isChecked()) {
                            incrementPoints(tvGreenCirclePenetrations1, tvHalf1TeamCirclePenetration);
                        } else if (rdbSecondHalfCP.isChecked()) {
                            incrementPoints(tvGreenCirclePenetrations2, tvHalf2TeamCirclePenetration);
                        }
                        break;

                    case "1.0":
                        //Yellow
                        if (rdbFirstHalfCP.isChecked()) {
                            incrementPoints(tvYellowCirclePenetrations1, tvHalf1TeamCirclePenetration);
                        } else if (rdbSecondHalfCP.isChecked()) {
                            incrementPoints(tvYellowCirclePenetrations2, tvHalf2TeamCirclePenetration);
                        }
                        break;

                    case "2.0":
                        //Orange
                        if (rdbFirstHalfCP.isChecked()) {
                            incrementPoints(tvOrangeCirclePenetrations1, tvHalf1TeamCirclePenetration);
                        } else if (rdbSecondHalfCP.isChecked()) {
                            incrementPoints(tvOrangeCirclePenetrations2, tvHalf2TeamCirclePenetration);
                        }
                        break;
                    case "3.0":
                        //Blue
                        if (rdbFirstHalfCP.isChecked()) {
                            incrementPoints(tvBlueCirclePenetrations1, tvHalf1TeamCirclePenetration);
                        } else if (rdbSecondHalfCP.isChecked()) {
                            incrementPoints(tvBlueCirclePenetrations2, tvHalf2TeamCirclePenetration);
                        }
                        break;
                    case "4.0":
                        //Red
                        if (rdbFirstHalfCP.isChecked()) {
                            incrementPoints(tvRedCirclePenetrations1,  tvHalf1TeamCirclePenetration);
                        } else if (rdbSecondHalfCP.isChecked()) {
                            incrementPoints(tvRedCirclePenetrations2, tvHalf2TeamCirclePenetration);
                        }
                        break;
                }

                setPCScoreListener.onSetCPScore();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        return view;
    }

    private void getMatchHalf() {
        if (rdbFirstHalfCP.isChecked()) {
            showViews(frmHalf1CP);
            hideViews(frmHalf2CP);
        } else if (rdbSecondHalfCP.isChecked()) {
            showViews(frmHalf2CP);
            hideViews(frmHalf1CP);
        }
    }

    public interface SetCPScoreListener {
        void onSetCPScore();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            setPCScoreListener = (Frag2CirclePenetrations.SetCPScoreListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement interface"
                    + " onSetCPScore");
        }
    }
}
