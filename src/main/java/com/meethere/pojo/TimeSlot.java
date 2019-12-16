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

	public TimeSlot() {
	}

	public TimeSlot(Integer id, Integer beginTime, Integer endTime, int seat, Venue venue,Date bookingDate) {
		this.id=id;
		this.beginTime=beginTime;
		this.endTime=endTime;
		this.seat=seat;
		this.venue=venue;
		this.bookingDate=bookingDate;
	}

	public static class TimeSlotBuilder{
		private Integer id;
		private Integer beginTime;
		private Integer endTime;
		private int seat;
		private Venue venue;
		private Date bookingDate;

		public TimeSlotBuilder id(Integer id){
			this.id = id;
			return this;
		}

		public TimeSlotBuilder beginTime(Integer beginTime){
			this.beginTime = beginTime;
			return this;
		}

		public TimeSlotBuilder endTime(Integer endTime){
			this.endTime = endTime;
			return this;
		}

		public TimeSlotBuilder seat(Integer seat){
			this.seat = seat;
			return this;
		}

		public TimeSlotBuilder venue(Venue venue){
			this.venue = venue;
			return this;
		}

		public TimeSlotBuilder bookingDate(Date date){
			this.bookingDate = date;
			return this;
		}

		public TimeSlot build(){
			return new TimeSlot(id,beginTime,endTime,seat,venue,bookingDate);
		}
	}

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
		return (Date)bookingDate.clone();
	}
	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}
}
