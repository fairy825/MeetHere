package com.meethere.service;

import com.meethere.pojo.*;
import com.meethere.util.Page4Navigator;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookingService {
    public static final String waitApprove = "waitApprove";
    public static final String cancelled = "cancelled";
    public static final String refused = "refused";
    public static final String waitPay = "waitPay";
    public static final String waitTime = "waitTime";
    public static final String waitArrive = "waitArrive";
    public static final String waitFinish = "waitFinish";
    public static final String waitReview = "waitReview";
    public static final String finish = "finish";
    public static final String delete = "delete";

    public Page4Navigator<Booking> list(int start, int size, int navigatePages);
    public Page4Navigator<Booking> search(Booking booking, int start, int size, int navigatePages);
    public Booking get(int id);
    public void update(Booking booking);
    public Page4Navigator<Booking> searchByUser(String keyword, int start, int size, int navigatePages);
    public Page4Navigator<Booking> searchByVenue(String keyword, int start, int size, int navigatePages);
    public Integer total(Venue venue);
    public void saveBooking(Booking booking);
    public Page4Navigator<Booking> listBookingsByUser(User user, int start, int size, int navigatePages);

}