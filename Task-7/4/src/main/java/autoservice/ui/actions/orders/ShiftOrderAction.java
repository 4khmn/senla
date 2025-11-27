package main.java.autoservice.ui.actions.orders;

import main.java.autoservice.model.AutoService;
import main.java.autoservice.ui.actions.IAction;

import java.util.Scanner;

public class ShiftOrderAction implements IAction {
    private final AutoService service;

    public ShiftOrderAction(AutoService autoService) {
        this.service = autoService;
    }

    @Override
    public void execute() {
        if (service.getOrdersCount() == 0) {
            System.out.println("Список заказов пуст.");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите id заказа, которое вы хотите продлить: ");
        long id = sc.nextLong();
        while(true) {
            System.out.print("Введите длительность в часах (на сколько часов продлить заказ): ");
            int durationInHours = sc.nextInt();
            if (durationInHours > 0) {
                if (service.shiftOrder(id, durationInHours)) {
                    System.out.println("Заказ успешно сдвинут.");
                } else {
                    System.out.println("Id заказа введено не верно.");
                }
                break;
            } else {
                System.out.println("Минимальная длительность в часах - 1");
                sc.next();
            }
        }
    }
}
