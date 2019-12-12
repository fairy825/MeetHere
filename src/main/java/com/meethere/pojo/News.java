package com.meethere.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

	public News() {
	}

	public News(Integer id,String title) {
		this.id=id;
		this.title=title;
	}

	public static class NewsBuilder{
		private Integer id;
		private String title;

		public News.NewsBuilder id(Integer id){
			this.id = id;
			return this;
		}

		public News.NewsBuilder title(String title){
			this.title = title;
			return this;
		}

		public News build(){
			return new News(id,title);
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
}
