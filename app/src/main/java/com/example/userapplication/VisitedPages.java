package com.example.userapplication;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class VisitedPages {
    private List<Work> visitedWorks;
    private List<String> visitedWorkNames;

    //생성자
    private VisitedPages() {
        visitedWorks = new ArrayList<>();
        visitedWorkNames = new ArrayList<>();
    }

    //Getter & Setter
    public List<Work> getVisitedWorks() {
        return visitedWorks;
    }
    public void addVisitedWorks(Work work) {
        if(!visitedWorks.contains(work)) //추가할 시 중복 방지
            this.visitedWorks.add(work);
    }
    public List<String> getVisitedWorkNames() {
        return this.visitedWorkNames;
    }
    public void addVisitedWorkNames(String name){
        if(!visitedWorkNames.contains(name)) //추가할 시 중복 방지
            this.visitedWorkNames.add(name);
    }

    //싱글턴 객체 선언
    private static VisitedPages instance = null;

    public static synchronized VisitedPages getInstance(){
        if(null == instance){
            instance = new VisitedPages();
        }
        return instance;
    }
}


