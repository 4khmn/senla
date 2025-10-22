package model;

import java.math.BigDecimal;

public class Master {
    private static int global_id=1; // for serial primary key
    private final int id;
    private String name;
    private BigDecimal salary;
    private boolean isAvailable = true; // с самого начала у работника нету заказа

    public Master(String name, BigDecimal salary) {
        this.name = name;
        this.salary = salary;
        id = global_id++;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Master{" +
                "WorkId=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", isAvailable=" + isAvailable +
                '}';
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void processTheOrder(Order order){

    }

    public void finishTheOrder(Order order){

    }


}
