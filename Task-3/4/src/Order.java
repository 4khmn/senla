import java.time.LocalDateTime;

public class Order {
    private static int glonal_id=1; // for serial primary key
    private int id;
    private String description;
    private Master master;
    private GarageSpot garageSpot;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private OrderStatus orderStatus = OrderStatus.OPEN;

    public Order(String description,
                 Master master,
                 GarageSpot garageSpot,
                 LocalDateTime startTime,
                 LocalDateTime endTime) {
        this.id = glonal_id++;
        this.description = description;
        this.master = master;
        this.garageSpot = garageSpot;
        this.startTime = startTime;
        this.endTime = endTime;
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
        return "Order{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", master=" + master +
                ", garageSpot=" + garageSpot +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", orderStatus=" + orderStatus +
                '}';
    }
}
