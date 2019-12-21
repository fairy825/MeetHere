package com.meethere.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.meethere.util.TimeAgoUtils;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "news")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class News {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	@JsonFormat( pattern="yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date createDate;
	private String title;
	private String content;
	@Transient
	private String timeDesc;

	public News() {
	}

	public News(Integer id,String title,Date createDate) {
		this.id=id;
		this.title=title;
		this.createDate=createDate;
	}

	public static class NewsBuilder{
		private Integer id;
		private String title;
		private Date createDate;

		public News.NewsBuilder id(Integer id){
			this.id = id;
			return this;
		}

		public News.NewsBuilder title(String title){
			this.title = title;
			return this;
		}
		public News.NewsBuilder createDate(Date date){
			this.createDate=date;
			return this;
		}

		public News build(){
			return new News(id,title,createDate);
		}
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String getTimeDesc() {
		return TimeAgoUtils.format(createDate);
	}

}
