package autoservice.ui.actions.orders;

import autoservice.model.AutoService;
import autoservice.model.exceptions.DBException;
import autoservice.ui.actions.IAction;

import java.math.BigDecimal;
import java.util.Scanner;

public class AddOrderAction implements IAction {
    private final AutoService service;

    public AddOrderAction(AutoService autoService) {
        this.service = autoService;
    }
    @Override
    public void execute() {
        if (service.getMastersCount() == 0 || service.getGarageSpotsCount() == 0) {
            System.out.println("В автосервисе отсувствуют мастера или гаражные места");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите описание заказа: ");
        String desc = sc.nextLine();
        int duration = 0;
        while(true) {
            try {
                System.out.print("Введите длительность заказа в часах: ");
                duration = sc.nextInt();
                if (duration>0){
                    break;
                }
                else{
                    System.out.println("Минимальная длительность заказа: 1");
                }
            }
            catch(Exception e){
                System.out.println("Ошибка ввода!");
                sc.next();
            }
        }
        while(true) {
            try {
                System.out.print("Введите стоимость услуги: ");
                BigDecimal price = sc.nextBigDecimal();
                try {
                    service.addOrder(desc, duration, price);
                }
                catch (DBException e){
                    System.out.println(e.getMessage());
                }
                break;
            }
            catch(Exception e){
                System.out.println("Ошибка ввода!");
                sc.next();
            }
        }
        System.out.println("Заказ успешно добавлен.");

    }
}
