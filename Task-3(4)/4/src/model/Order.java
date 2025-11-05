package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
public class Order implements Comparable<Order> {
    private static int global_id=1; // for serial primary key
    private final int id;
    private String description;
    private Master master;
    private GarageSpot garageSpot;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private OrderStatus orderStatus = OrderStatus.OPEN;
    private BigDecimal price;
    private LocalDateTime createdAt;

    public int getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
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

    @Override
    public String toString() {
        return "model.Order{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", master=" + master +
                ", garageSpot=" + garageSpot +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", orderStatus=" + orderStatus +
                '}';
    }

    @Override
    public int compareTo(Order other) {
        int cmp = this.startTime.compareTo(other.startTime);
        if (cmp == 0) {
            cmp = this.endTime.compareTo(other.endTime);
        }
        if (cmp == 0) {
            cmp = Integer.compare(this.id, other.id); // или hashCode(), если нет id
        }
        return cmp;
    }
}
