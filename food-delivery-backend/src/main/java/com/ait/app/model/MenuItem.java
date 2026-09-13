package com.ait.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="menu_items")
public class MenuItem {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
		private long id;

	 	@Column(nullable = false)
	    private String name;
	 	
	    @Column(nullable = false)
	    private String description;
	    
	    @Column(nullable = false)
	    private String type;
	    
	    @Column(nullable = false)
	    private double halfPrice;
	    
	    @Column(nullable = false)
	    private double fullPrice;

	    @Column(nullable = false)
	    private boolean available;

	    @ManyToOne
	    @JoinColumn(name = "restaurant_id", nullable = false)
	    private Restaurant restaurant;
	    
	    

	    public MenuItem() {
	    }

	    

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getDescription() {
	        return description;
	    }

	    public void setDescription(String description) {
	        this.description = description;
	    }

	  

	    public double getHalfPrice() {
			return halfPrice;
		}

		public void setHalfPrice(double halfPrice) {
			this.halfPrice = halfPrice;
		}

		public double getFullPrice() {
			return fullPrice;
		}

		



		public void setFullPrice(double fullPrice) {
			this.fullPrice = fullPrice;
		}

		public boolean isAvailable() {
	        return available;
	    }

	

		public String getType() {
			return type;
		}

		public long getId() {
			return id;
		}



		public void setId(long id) {
			this.id = id;
		}



		public void setType(String type) {
			this.type = type;
		}

		public void setAvailable(boolean available) {
	        this.available = available;
	    }

	    public Restaurant getRestaurant() {
	        return restaurant;
	    }

	    public void setRestaurant(Restaurant restaurant) {
	        this.restaurant = restaurant;
	    }
	}
