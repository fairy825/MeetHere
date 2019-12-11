package com.meethere.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "venueimage")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer"})
public class VenueImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;


    @ManyToOne
    @JoinColumn(name="vid")
    @JsonBackReference
    private Venue venue;

    public VenueImage() {
    }

    public VenueImage(Integer id,Venue venue) {
        this.id=id;
        this.venue=venue;
    }

    public static class VenueImageBuilder{
        private Integer id;
        private Venue venue;

        public VenueImage.VenueImageBuilder id(Integer id){
            this.id = id;
            return this;
        }


        public VenueImage.VenueImageBuilder venue(Venue venue){
            this.venue = venue;
            return this;
        }

        public VenueImage build(){
            return new VenueImage(id,venue);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

}
