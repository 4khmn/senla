package autoservice.model.entities;

import autoservice.model.AutoService;
import autoservice.model.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.math.BigDecimal;
import java.time.LocalDateTime;
public class Order implements Comparable<Order>, Identifiable{

    private Long id;


    private String description;


    @JsonIgnore
    private Master master;

    @JsonIgnore
    private GarageSpot garageSpot;

    @JsonProperty("masterId")
    public Long getMasterId() {
        return master != null ? master.getId() : null;
    }

    @JsonProperty("garageSpotId")
    public Long getGarageSpotId() {
        return garageSpot != null ? garageSpot.getId() : null;
    }

    @JsonProperty("masterId")
    public void setMasterId(Long masterId) {
        if (masterId != null) {
            this.master = new Master();
            this.master.setId(masterId);
        } else {
            this.master = null;
        }
    }

    @JsonProperty("garageSpotId")
    public void setGarageSpotId(Long garageSpotId) {
        if (garageSpotId != null) {
            this.garageSpot = new GarageSpot();
            this.garageSpot.setId(garageSpotId);
        } else {
            this.garageSpot = null;
        }
    }


    private LocalDateTime startTime;

    private LocalDateTime endTime;
    private OrderStatus orderStatus = OrderStatus.OPEN;
    private BigDecimal price;
    private LocalDateTime createdAt;

    public Order() {
    }


    public void setId(long id) {
        this.id = id;
    }



    public long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Order(String description,
                 Master master,
                 GarageSpot garageSpot,
                 LocalDateTime startTime,
                 LocalDateTime endTime,
                 BigDecimal price) {
        if (startTime.isBefore(endTime)) {
            this.description = description;
            this.master = master;
            this.garageSpot = garageSpot;
            this.startTime = startTime;
            this.endTime = endTime;
            this.price = price;
            this.createdAt = LocalDateTime.now();
        }
        else{
            throw new IllegalArgumentException("Invalid time settings: start time must be before end time");
        }
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    public GarageSpot getGarageSpot() {
        return garageSpot;
    }

    public void setGarageSpot(GarageSpot garageSpot) {
        this.garageSpot = garageSpot;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }


    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "id - " + id + ", description - " +description + ", created at - " + createdAt +
                ", master id - " + master.getId() + ", garage spot id - " + garageSpot.getId() + ", start time - " + startTime +
                ", end time - " + endTime + ", status - " + orderStatus.name().toLowerCase() + ", price - " + price;
    }

    @Override
    public int compareTo(Order other) {
        int cmp = this.startTime.compareTo(other.startTime);
        if (cmp == 0) {
            cmp = this.endTime.compareTo(other.endTime);
        }
        if (cmp == 0) {
            cmp = Long.compare(this.id, other.id); // или hashCode(), если нет id
        }
        return cmp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;

        Order order = (Order) o;
        return id == order.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
