package com.example.userapplication;

import java.util.List;

public class VisitedPages {
    private String userID;
    private List<String> visitedWorkNames;


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public List<String> getVisitedWorkNames() {
        return visitedWorkNames;
    }

    public void setVisitedWorkNames(List<String> visitedWorkNames) {
        this.visitedWorkNames = visitedWorkNames;
    }

    public void addToVisitedWorkNames(String workName) {
        this.visitedWorkNames.add(workName);
    }

    private static VisitedPages instance = null;

    public static synchronized VisitedPages getInstance(){
        if(null == instance){
            instance = new VisitedPages();
        }
        return instance;
    }
}


