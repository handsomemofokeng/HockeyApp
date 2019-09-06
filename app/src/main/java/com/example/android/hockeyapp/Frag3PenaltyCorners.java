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
public class Frag3PenaltyCorners extends Fragment {

    PieChart pieChart;
    TextView tvGreenPenaltyCorners1, tvYellowPenaltyCorners1, tvOrangePenaltyCorners1,
            tvBluePenaltyCorners1, tvRedPenaltyCorners1;

    TextView tvGreenPenaltyCorners2, tvYellowPenaltyCorners2, tvOrangePenaltyCorners2,
            tvBluePenaltyCorners2, tvRedPenaltyCorners2;

    RadioButton rdbFirstHalfPC, rdbSecondHalfPC;
    LinearLayout frmHalf1PC, frmHalf2PC;
    private TextView tvHalf1TeamPenaltyCorners,tvHalf2TeamPenaltyCorners;
    private SetPCScoreListener setPCScoreListener;

    public Frag3PenaltyCorners() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag3_penalty_corners, container, false);

        rdbFirstHalfPC = view.findViewById(R.id.rdbFirstHalfPC);
        rdbSecondHalfPC = view.findViewById(R.id.rdbSecondHalfPC);

        pieChart = view.findViewById(R.id.pieChartPenaltyCorners);

        frmHalf1PC = view.findViewById(R.id.frmHalf1PC);
        frmHalf2PC = view.findViewById(R.id.frmHalf2PC);

        tvHalf1TeamPenaltyCorners = view.findViewById(R.id.tvHalf1TeamPenaltyCorners);
        tvHalf2TeamPenaltyCorners = view.findViewById(R.id.tvHalf2TeamPenaltyCorners);

        tvGreenPenaltyCorners1 = view.findViewById(R.id.tvGreenPenaltyCorners1);
        tvYellowPenaltyCorners1 = view.findViewById(R.id.tvYellowPenaltyCorners1);
        tvOrangePenaltyCorners1 = view.findViewById(R.id.tvOrangePenaltyCorners1);
        tvRedPenaltyCorners1 = view.findViewById(R.id.tvRedPenaltyCorners1);
        tvBluePenaltyCorners1 = view.findViewById(R.id.tvBluePenaltyCorners1);

        tvGreenPenaltyCorners2 = view.findViewById(R.id.tvGreenPenaltyCorners2);
        tvYellowPenaltyCorners2 = view.findViewById(R.id.tvYellowPenaltyCorners2);
        tvOrangePenaltyCorners2 = view.findViewById(R.id.tvOrangePenaltyCorners2);
        tvRedPenaltyCorners2 = view.findViewById(R.id.tvRedPenaltyCorners2);
        tvBluePenaltyCorners2 = view.findViewById(R.id.tvBluePenaltyCorners2);

        tvGreenPenaltyCorners1.setTextColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        tvYellowPenaltyCorners1.setTextColor(ColorTemplate.VORDIPLOM_COLORS[1]);
        tvOrangePenaltyCorners1.setTextColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        tvBluePenaltyCorners1.setTextColor(ColorTemplate.VORDIPLOM_COLORS[3]);
        tvRedPenaltyCorners1.setTextColor(ColorTemplate.VORDIPLOM_COLORS[4]);

        tvGreenPenaltyCorners2.setTextColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        tvYellowPenaltyCorners2.setTextColor(ColorTemplate.VORDIPLOM_COLORS[1]);
        tvOrangePenaltyCorners2.setTextColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        tvBluePenaltyCorners2.setTextColor(ColorTemplate.VORDIPLOM_COLORS[3]);
        tvRedPenaltyCorners2.setTextColor(ColorTemplate.VORDIPLOM_COLORS[4]);

        pieChart.setData(setPieData("Circle Penetrations"));
        pieChart.setRotationEnabled(false);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutBounce);

        getMatchHalf();

        rdbFirstHalfPC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getMatchHalf();
            }
        });

        rdbSecondHalfPC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                        if (rdbFirstHalfPC.isChecked()) {
                            incrementPoints(tvGreenPenaltyCorners1, tvHalf1TeamPenaltyCorners);
                        } else if (rdbSecondHalfPC.isChecked()) {
                            incrementPoints(tvGreenPenaltyCorners2, tvHalf2TeamPenaltyCorners);
                        }
                        break;

                    case "1.0":
                        //Yellow
                        if (rdbFirstHalfPC.isChecked()) {
                            incrementPoints(tvYellowPenaltyCorners1, tvHalf1TeamPenaltyCorners);
                        } else if (rdbSecondHalfPC.isChecked()) {
                            incrementPoints(tvYellowPenaltyCorners2, tvHalf2TeamPenaltyCorners);
                        }
                        break;

                    case "2.0":
                        //Orange
                        if (rdbFirstHalfPC.isChecked()) {
                            incrementPoints(tvOrangePenaltyCorners1, tvHalf1TeamPenaltyCorners);
                        } else if (rdbSecondHalfPC.isChecked()) {
                            incrementPoints(tvOrangePenaltyCorners2, tvHalf2TeamPenaltyCorners);
                        }
                        break;
                    case "3.0":
                        //Blue
                        if (rdbFirstHalfPC.isChecked()) {
                            incrementPoints(tvBluePenaltyCorners1, tvHalf1TeamPenaltyCorners);
                        } else if (rdbSecondHalfPC.isChecked()) {
                            incrementPoints(tvBluePenaltyCorners2, tvHalf2TeamPenaltyCorners);
                        }
                        break;
                    case "4.0":
                        //Red
                        if (rdbFirstHalfPC.isChecked()) {
                            incrementPoints(tvRedPenaltyCorners1,  tvHalf1TeamPenaltyCorners);
                        } else if (rdbSecondHalfPC.isChecked()) {
                            incrementPoints(tvRedPenaltyCorners2, tvHalf2TeamPenaltyCorners);
                        }
                        break;
                }

                setPCScoreListener.onSetPCScore();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        return view;
    }

    private void getMatchHalf() {
        if (rdbFirstHalfPC.isChecked()) {
            showViews(frmHalf1PC);
            hideViews(frmHalf2PC);
        } else if (rdbSecondHalfPC.isChecked()) {
            showViews(frmHalf2PC);
            hideViews(frmHalf1PC);
        }
    }

    public interface SetPCScoreListener {
        void onSetPCScore();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            setPCScoreListener = (Frag3PenaltyCorners.SetPCScoreListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement interface"
                    + " onSetPCScore");
        }
    }

}
