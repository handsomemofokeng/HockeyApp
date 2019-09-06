package com.example.android.hockeyapp;

class Medical {

    private String aidName, aidPlan, aidNumber, allergies, phone1, phone2;

    public Medical() {
        this.setAidName("No data");
        this.setAidNumber("No data");
        this.setAidPlan("No data");
        this.setAllergies("No data");
        this.setPhone1("No data");
        this.setPhone2("No data");
    }

    public Medical(String aidName, String aidPlan, String aidNumber, String allergies, String phone1, String phone2) {
        setAidName(aidName);
        setAidPlan(aidPlan);
        setAidNumber(aidNumber);
        setAllergies(allergies);
        setPhone1(phone1);
        setPhone2(phone2);
    }

    public String getAidName() {
        return aidName;
    }

    public void setAidName(String aidName) {
        if (!aidName.trim().isEmpty())
            this.aidName = aidName;
        else
            this.aidName = "No Data";
    }

    public String getAidPlan() {
        return aidPlan;
    }

    public void setAidPlan(String aidPlan) {
        if (!aidPlan.trim().isEmpty())
            this.aidPlan = aidPlan;
        else
            this.aidPlan = "No Data";
    }

    public String getAidNumber() {
        return aidNumber;
    }

    public void setAidNumber(String aidNumber) {
        if (!aidNumber.trim().isEmpty())
            this.aidNumber = aidNumber;
        else
            this.aidNumber = "No Data";
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        if (!allergies.trim().isEmpty())
            this.allergies = allergies;
        else
            this.allergies = "No Data";
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        if (!phone1.trim().isEmpty())
            this.phone1 = phone1;
        else
            this.phone1 = "No Data";
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        if (!phone2.trim().isEmpty())
            this.phone2 = phone2;
        else
            this.phone2 = "No Data";
    }
}
