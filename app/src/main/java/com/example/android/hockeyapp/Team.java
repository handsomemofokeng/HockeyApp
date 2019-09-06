package com.example.android.hockeyapp;

public class Team {

    private String teamName, ageGroup, teamCoach, teamType;
    private boolean hasMatch;

    public Team() {
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getTeamCoach() {
        return teamCoach;
    }

    public void setTeamCoach(String teamCoach) {
        this.teamCoach = teamCoach;
    }

    public String getTeamType() {
        return teamType;
    }

    public void setTeamType(String teamType) {
        this.teamType = teamType;
    }

    public boolean hasMatch() {
        return hasMatch;
    }

    public void setHasMatch(boolean hasMatch) {
        this.hasMatch = hasMatch;
    }
}