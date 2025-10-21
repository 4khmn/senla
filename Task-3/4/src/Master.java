import java.math.BigDecimal;

public class Master {
    private static int glonal_id=1; // for serial primary key
    private int WorkId;
    private String name;
    private BigDecimal salary;
    private boolean isAvailable = true; // с самого начала у работника нету заказа

    public Master(String name, BigDecimal salary) {
        this.name = name;
        this.salary = salary;
        WorkId = glonal_id++;
    }

    public void processTheOrder(Order order){

    }

    public void finishTheOrder(Order order){

    }


}
