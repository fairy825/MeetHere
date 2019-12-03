package com.meethere.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;
@Entity
@Table(name = "venue")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class Venue {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	private String name;
	private String introduction;
	private Float price;
	private String phoneNumber;
	private Integer totalSeat;
	private String location;
	@ManyToOne
	@JoinColumn(name="did")
	private District district;
	private Integer startTime;
	private Integer endTime;
	@Transient
	private Integer reviewCount;
	@Transient
	private Integer saleCount;
	@Transient
	private VenueImage firstVenueImage;
	@Transient
	private List<VenueImage> venueImages;

	public List<VenueImage> getVenueImages() {
		return venueImages;
	}

	public void setVenueImages(List<VenueImage> venueImages) {
		this.venueImages = venueImages;
	}
//	private List<Message> messages;
//	private List<TimeSlot> timeSlots;

//	public List<TimeSlot> getTimeSlots() {
//		return timeSlots;
//	}
//	public void setTimeSlots(List<TimeSlot> timeSlots) {
//		this.timeSlots = timeSlots;
//	}

	public VenueImage getFirstVenueImage() {
		return firstVenueImage;
	}

	public void setFirstVenueImage(VenueImage firstVenueImage) {
		this.firstVenueImage = firstVenueImage;
	}

	public Integer getStartTime() {
		return startTime;
	}
	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}
	public Integer getEndTime() {
		return endTime;
	}
	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}
//	public List<VenueImage> getVenueSingleImages() {
//		return venueSingleImages;
//	}
//	public void setVenueSingleImages(List<VenueImage> venueSingleImages) {
//		this.venueSingleImages = venueSingleImages;
//	}

//	public List<Message> getMessages() {
//		return messages;
//	}
//	public void setMessages(List<Message> messages) {
//		this.messages = messages;
//	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Integer getTotalSeat() {
		return totalSeat;
	}
	public void setTotalSeat(Integer totalSeat) {
		this.totalSeat = totalSeat;
	}
	public Integer getReviewCount() {
		return reviewCount;
	}
	public void setReviewCount(Integer reviewCount) {
		this.reviewCount = reviewCount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public District getDistrict() {
		return district;
	}
	public void setDistrict(District district) {
		this.district = district;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
//	public VenueImage getFirstVenueImage() {
//		return firstVenueImage;
//	}
//	public void setFirstVenueImage(VenueImage firstVenueImage) {
//		this.firstVenueImage = firstVenueImage;
//	}

	public Integer getSaleCount() {
		return saleCount;
	}

	public void setSaleCount(Integer saleCount) {
		this.saleCount = saleCount;
	}

//	public List<ParkingImage> getParkingImages() {
//		return parkingImages;
//	}
//	public void setParkingImages(List<ParkingImage> parkingImages) {
//		this.parkingImages = parkingImages;
//	}
//
}
