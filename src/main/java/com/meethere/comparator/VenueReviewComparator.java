package com.meethere.comparator;
 
import java.util.Comparator;
import com.meethere.pojo.Venue;

public class VenueReviewComparator implements Comparator<Venue>{
 
    @Override
    public int compare(Venue p1, Venue p2) {
        return p2.getReviewCount()-p1.getReviewCount();
    }
 
}