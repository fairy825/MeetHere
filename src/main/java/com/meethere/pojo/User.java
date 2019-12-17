package com.meethere.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "user")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class User implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	private String name;
	private String password;
	private String nickname;
	private String phoneNumber;
	private String email;
	private String faceImage;

	@Transient
	private String userToken;
	@Transient
	private String anonymousName;

	public User() {
	}

	public User(Integer id, String name, String password, String nickname, String phoneNumber, String email,String faceImage) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.nickname = nickname;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.faceImage = faceImage;
	}

	public static class UserBuilder{
		private Integer id;
		private String name;
		private String password;
		private String nickname;
		private String phoneNumber;
		private String email;
		private String faceImage;

		public UserBuilder id(int id){
			this.id = id;
			return this;
		}

		public UserBuilder name(String name){
			this.name = name;
			return this;
		}

		public UserBuilder password(String password){
			this.password = password;
			return this;
		}
		public UserBuilder phoneNumber(String phoneNumber){
			this.phoneNumber = phoneNumber;
			return this;
		}
		public UserBuilder email(String email){
			this.email = email;
			return this;
		}
		public UserBuilder faceImage(String faceImage){
			this.faceImage = faceImage;
			return this;
		}
		public UserBuilder nickname(String nickname){
			this.nickname = nickname;
			return this;
		}

		public User build(){
			return new User(id,name,password,nickname,phoneNumber,email,faceImage);
		}
	}
	public String getFaceImage() {
		return faceImage;
	}

	public void setFaceImage(String faceImage) {
		this.faceImage = faceImage;
	}
	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAnonymousName(){
		if(null==nickname)
			return null;

		if(nickname.length()<=1)
			return "*";

		if(nickname.length()==2)
			return nickname.substring(0,1) +"*";

		char[] cs =nickname.toCharArray();
		for (int i = 1; i < cs.length-1; i++) {
			cs[i]='*';
		}
		return new String(cs);


	}

}
