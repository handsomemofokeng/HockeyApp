package com.example.android.hockeyapp;

import java.io.Serializable;

public class Player implements Serializable {
    private String name, surname, teamName, aidName, aidPlan,
            aidNumber, allergies, par1CellNo, par2CellNo, objectId;

    private int goals;
    private double rating;

    public Player() {
    }

    @Override
    public String toString() {
        return String.format("%s %s ", getName(), getSurname());
    }

    /**
     * This method populates a Player object with medical details
     *
     * @param medical contains medical details for player
     */
    public void setPlayerMedicalInfo(Medical medical) {

        if (medical != null) {
            this.setAidName(medical.getAidName());
            this.setAidNumber(medical.getAidNumber());
            this.setAidPlan(medical.getAidPlan());
            this.setAllergies(medical.getAllergies());
            this.setPar1CellNo(medical.getPhone1());
            this.setPar2CellNo(medical.getPhone2());
        }
//        else {
//            this.setAidName("No data");
//            this.setAidNumber("No data");
//            this.setAidPlan("No data");
//            this.setAllergies("No data");
//            this.setPar1CellNo("No data");
//            this.setPar2CellNo("No data");
//        }

    }

    public String getObjectId() {
        return objectId;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getAidName() {
        return aidName;
    }

    public void setAidName(String aidName) {
        if (!aidName.isEmpty())
            this.aidName = aidName;
        else
            this.aidName = "No Data";
    }

    public String getAidPlan() {
        return aidPlan;
    }

    public void setAidPlan(String aidPlan) {
        if (!aidName.isEmpty())
            this.aidPlan = aidPlan;
        else
            this.aidPlan = "No Data";
    }

    public String getAidNumber() {
        return aidNumber;
    }

    public void setAidNumber(String aidNumber) {
        if (!aidNumber.isEmpty())
            this.aidNumber = aidNumber;
        else
            this.aidNumber = "No Data";
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        if (!allergies.isEmpty())
            this.allergies = allergies;
        else
            this.allergies = "No Data";
    }

    public String getPar1CellNo() {
        return par1CellNo;
    }

    public void setPar1CellNo(String par1CellNo) {
        if (!par1CellNo.isEmpty())
            this.par1CellNo = par1CellNo;
        else
            this.par1CellNo = "No Data";
    }


    public String getPar2CellNo() {
        return par2CellNo;
    }

    public void setPar2CellNo(String par2CellNo) {
        if (!par2CellNo.isEmpty())
            this.par2CellNo = par2CellNo;
        else
            this.par2CellNo = "No Data";
    }


}
