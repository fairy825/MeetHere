package com.meethere.comparator;
 
import com.meethere.pojo.Venue;

import java.util.Comparator;


public class VenueAllComparator implements Comparator<Venue>{
 
    @Override
    public int compare(Venue p1, Venue p2) {
        return p2.getReviewCount()*p2.getSaleCount()-p1.getReviewCount()*p1.getSaleCount();
    }
 
}