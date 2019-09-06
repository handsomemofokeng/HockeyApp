package com.example.android.hockeyapp;

import java.util.Date;

public class MatchStats {

    private Date created;
    private String objectId;
    private int teamGoals1st, teamGoals2nd, opponentGoals1st, opponentGoals2nd;

    private String matchTitle, matchTeam, matchOpponent, playerPosition1, playerPosition2,
            playerPosition3, playerPosition4, playerPosition5, playerPosition6, playerPosition7,
            playerPosition8, playerPosition9, playerPosition10, playerPosition11, playerPosition12,
            playerPosition13, playerPosition14;

    private int playerGoals1, playerGoals2, playerGoals3, playerGoals4, playerGoals5, playerGoals6,
            playerGoals7, playerGoals8, playerGoals9, playerGoals10, playerGoals11, playerGoals12,
            playerGoals13, playerGoals14;

    private double playerRating1, playerRating2, playerRating3, playerRating4, playerRating5,
            playerRating6, playerRating7, playerrRating8, playerRating9, playerRating10,
            playerRating11, playerRating12, playerRating13, layerRating14;


    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getTeamGoals1st() {
        return teamGoals1st;
    }

    public void setTeamGoals1st(int teamGoals1st) {
        this.teamGoals1st = teamGoals1st;
    }

    public int getTeamGoals2nd() {
        return teamGoals2nd;
    }

    public void setTeamGoals2nd(int teamGoals2nd) {
        this.teamGoals2nd = teamGoals2nd;
    }

    public int getOpponentGoals1st() {
        return opponentGoals1st;
    }

    public void setOpponentGoals1st(int opponentGoals1st) {
        this.opponentGoals1st = opponentGoals1st;
    }

    public int getOpponentGoals2nd() {
        return opponentGoals2nd;
    }

    public void setOpponentGoals2nd(int opponentGoals2nd) {
        this.opponentGoals2nd = opponentGoals2nd;
    }

    @Override
    public String toString() {
        return matchTeam + " VS " + matchOpponent;
    }
}