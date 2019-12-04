package com.meethere.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.meethere.service.BookingService;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "booking")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@ManyToOne
	@JoinColumn(name="uid")
	private User user;
	@ManyToOne
	@JoinColumn(name="vid")
	private Venue venue;
	@JsonFormat( pattern="yyyy-MM-dd HH:mm", timezone = "GMT+8")
	@Column(name="createDate")
	private Date createDate;
	@JsonFormat( pattern="yyyy-MM-dd HH:mm", timezone = "GMT+8")
	@Column(name="payDate")
	private Date payDate;
	@JsonFormat( pattern="yyyy-MM-dd HH:mm", timezone = "GMT+8")
	@Column(name="arriveDate")
	private Date arriveDate;
	@ManyToOne
	@JoinColumn(name="tid")
	private TimeSlot timeSlot;
	private String state;
	private String remark;

	public String getStateDesc(){
        String desc ="未知";
        switch(state){
			case BookingService.waitApprove:
				desc="待审核";
				break;
			case BookingService.cancelled:
				desc="已取消";
				break;
			case BookingService.refused:
				desc="驳回";
				break;
            case BookingService.waitPay:
                desc="待付款";
                break;
            case BookingService.waitTime:
                desc="未到预约时间";
                break;
            case BookingService.waitArrive:
                desc="待到达";
                break;
            case BookingService.waitFinish:
                desc="进行中";
                break;
            case BookingService.waitReview:
                desc="可留言";
                break;
            case BookingService.finish:
                desc="已结束";
                break;
            case BookingService.delete:
                desc="刪除";
                break;
            default:
                desc="未知";
        }
        return desc;
    }
	public TimeSlot getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(TimeSlot timeSlot) {
		this.timeSlot = timeSlot;
	}
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	public Date getArriveDate() {
		return arriveDate;
	}
	public void setArriveDate(Date arriveDate) {
		this.arriveDate = arriveDate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
