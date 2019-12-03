package com.meethere.pojo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "timeslot")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class TimeSlot {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	private int seat;
	private int beginTime;
	private int endTime;
	@JsonFormat( pattern="yyyy-MM-dd", timezone = "GMT+8")
	private Date bookingDate;
	@ManyToOne
	@JoinColumn(name="vid")
	private Venue venue;

	//private User user;
	//private Booking booking;
	public Venue getVenue() {
		return venue;
	}
	public void setVenue(Venue venue) {
		this.venue = venue;
	}
	public int getSeat() {
		return seat;
	}
	public void setSeat(int seat) {
		this.seat = seat;
	}
//	public User getUser() {
//		return user;
//	}
//	public void setUser(User user) {
//		this.user = user;
//	}
//	public Booking getBooking() {
//		return booking;
//	}
//	public void setBooking(Booking booking) {
//		this.booking = booking;
//	}
	public int getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(int beginTime) {
		this.beginTime = beginTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public Date getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}
}
