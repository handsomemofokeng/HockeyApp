package com.example.android.hockeyapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import static com.example.android.hockeyapp.ApplicationClass.clearFields;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragMedicalDetails extends Fragment {

    private SaveMedicalDetailsListener saveMedicalDetailsListener;

    EditText etAidName, etAidPlan, etAidNumber, etAllergies, etPhone1, etPhone2;
    Button btnSaveMedicalInfo;

    public FragMedicalDetails() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View medicalDetailsView = inflater.inflate(R.layout.fragment_medical_details,
                container, false);

        //Initialise Fragment UI Reference
        etAidName = medicalDetailsView.findViewById(R.id.etAidName);
        etAidPlan = medicalDetailsView.findViewById(R.id.etAidPlan);
        etAidNumber = medicalDetailsView.findViewById(R.id.etAidNumber);
        etAllergies = medicalDetailsView.findViewById(R.id.etAllergies);
        etPhone1 = medicalDetailsView.findViewById(R.id.etPhone1);
        etPhone2 = medicalDetailsView.findViewById(R.id.etPhone2);
        btnSaveMedicalInfo = medicalDetailsView.findViewById(R.id.btnSaveMedicalInfo);

        btnSaveMedicalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                 * A new Medical Object is created, no need for validation as it is done on the
                 * overloaded constructor, which automatically sets the values to "No Data" if no
                 * fields are provided.
                 */
                Medical newMedical = new Medical(etAidName.getText().toString().trim(),
                        etAidPlan.getText().toString().trim(), etAidNumber.getText().toString().trim(),
                        etAllergies.getText().toString().trim(), etPhone1.getText().toString().trim(),
                        etPhone2.getText().toString().trim());

                //call interface to return the new medical object
                saveMedicalDetailsListener.onSaveMedicalDetails(newMedical);
                clearFields(etAidName, etAidPlan, etAidNumber, etAllergies, etPhone1, etPhone2);
            }
        });

        return medicalDetailsView;
    }

    /**
     * This interface callback will return a Medical Object as a parameter
     */
    public interface SaveMedicalDetailsListener {
        void onSaveMedicalDetails(Medical medical);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            saveMedicalDetailsListener = (SaveMedicalDetailsListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement interface" +
                    " onSaveMedicalDetails");
        }
    }

}