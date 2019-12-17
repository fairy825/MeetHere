package com.meethere.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.meethere.service.MessageService;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "message")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	private String content;
	@JsonFormat( pattern="yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date createDate;
	@ManyToOne
	@JoinColumn(name="uid")
	private User user;
	@ManyToOne
	@JoinColumn(name="vid")
	private Venue venue;
	@OneToOne
	@JoinColumn(name="bid")
	private Booking booking;
	private String state;

	public Message() {
	}

	public Message(Integer id) {
		this.id=id;
	}
	public Message(Integer id,Venue venue,String state,Booking booking) {
		this.id=id;
		this.venue=venue;
		this.state=state;
		this.booking=booking;
	}
	public static class MessageBuilder{
		private Integer id;
		private Venue venue;
		private String state;
		private Booking booking;

		public Message.MessageBuilder id(Integer id){
			this.id = id;
			return this;
		}

		public Message.MessageBuilder venue(Venue venue){
			this.venue = venue;
			return this;
		}

		public Message.MessageBuilder state(String state){
			this.state = state;
			return this;
		}
		public Message.MessageBuilder booking(Booking booking){
			this.booking = booking;
			return this;
		}

		public Message build(){
			return new Message(id,venue,state,booking);
		}
	}

	public String getStateDesc(){
		String desc ="未知";
		switch(state){
			case MessageService.waitApprove:
				desc="待审核";
				break;
			case MessageService.pass:
				desc="通过";
				break;
			case MessageService.refused:
				desc="拒绝";
				break;
			default:
				desc="未知";
		}
		return desc;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreateDate() {
		return (Date)createDate.clone();
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Venue getVenue() {
		return venue;
	}

	public void setVenue(Venue venue) {
		this.venue = venue;
	}
}
