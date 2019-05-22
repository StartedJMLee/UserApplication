package com.example.userapplication;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class VisitedPages {
    private String userID;
    private List<Work> visitedWorks; //Work의 List로 해야되겠다..

    private VisitedPages() {
        visitedWorks = new ArrayList<Work>();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public List<Work> getVisitedWorks() {
        return visitedWorks;
    }
    /*
    public void setVisitedWorks(List<Work> visitedWorks) {
        this.visitedWorks = visitedWorks;
    }
    */
    public List<String> getVisitedWorkNames() {
        List<String> visitedWorkNames = new ArrayList<String>();
        for(int i=0;i<visitedWorks.size();i++)
             visitedWorkNames.add(visitedWorks.get(i).getName());
        return visitedWorkNames;
    }


    public void addToVisitedWorks(Work work) {
        this.visitedWorks.add(work);
    }

    private static VisitedPages instance = null;

    public static synchronized VisitedPages getInstance(){
        if(null == instance){
            instance = new VisitedPages();
            for (int i=0;i<2;i++)
                instance.addToVisitedWorks(new Work("Bigmouth Strikes Again","The Smiths","Morrisey & Marr","없음"));
        }
        return instance;
    }
}


