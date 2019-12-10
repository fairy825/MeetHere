package com.meethere.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "admin")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class Admin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	private String name;
	private String password;
	@Transient
	private String adminToken;

	public Admin() {
	}

	public Admin(int id, String name, String password) {
		this.id = id;
		this.name = name;
		this.password = password;
	}

	public static class AdminBuilder{
		private int id;
		private String name;
		private String password;

		public AdminBuilder id(int id){
			this.id = id;
			return this;
		}

		public AdminBuilder name(String name){
			this.name = name;
			return this;
		}

		public AdminBuilder password(String password){
			this.password = password;
			return this;
		}

		public Admin build(){
			return new Admin(id,name,password);
		}
	}

	public String getAdminToken() {
		return adminToken;
	}

	public void setAdminToken(String adminToken) {
		this.adminToken = adminToken;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
