package autoservice.model.entities;

import autoservice.model.AutoService;
import autoservice.model.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.math.BigDecimal;
import java.time.LocalDateTime;
public class Order implements Comparable<Order> {
    private static long global_id=1; // for serial primary key

    private long id;

    private String description;


    private Master master;
    private GarageSpot garageSpot;



    private LocalDateTime startTime;

    private LocalDateTime endTime;
    private OrderStatus orderStatus = OrderStatus.OPEN;
    private BigDecimal price;
    private LocalDateTime createdAt;

    public Order() {
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
            this.id = global_id++;
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
    public Order(long id,
                 String description,
                 Master master,
                 GarageSpot garageSpot,
                 LocalDateTime startTime,
                 LocalDateTime endTime,
                 BigDecimal price) {
        if (startTime.isBefore(endTime)) {
            this.id = id;
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
    public static void updateGlobalId(long maxId) {
        if (maxId >= global_id) {
            global_id = maxId + 1;
        }
    }
    @Override
    public Order clone() {
        Order copy = new Order(
                this.id,
                this.description,
                this.master,
                this.garageSpot,
                this.startTime,
                this.endTime,
                this.price
        );
        copy.setCreatedAt(this.createdAt);
        copy.setOrderStatus(this.orderStatus);
        return copy;
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
}
