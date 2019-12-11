package com.meethere.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "district")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class District {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	private String name;
	@Transient
	@JsonBackReference
//	@OneToMany(mappedBy = "district")
//	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
//	@JoinColumn(name="did",referencedColumnName = "id")
	List<Venue> venues;

	public District() {
	}

	public District(Integer id, String name, List<Venue> venues) {
		this.id=id;
		this.name = name;
		this.venues=venues;
	}

	public static class DistrictBuilder{
		private Integer id;
		private String name;
		private List<Venue> venues;

		public District.DistrictBuilder id(Integer id){
			this.id = id;
			return this;
		}

		public District.DistrictBuilder name(String name){
			this.name = name;
			return this;
		}

		public District.DistrictBuilder venues(List<Venue> venues){
			this.venues = venues;
			return this;
		}

		public District build(){
			return new District(id,name,venues);
		}
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
	public List<Venue> getVenues() {
		return venues;
	}
	public void setVenues(List<Venue> venues) {
		this.venues = venues;
	}
}
