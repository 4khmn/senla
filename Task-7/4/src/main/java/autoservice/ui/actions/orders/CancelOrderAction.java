package autoservice.ui.actions.orders;

import autoservice.model.AutoService;
import autoservice.ui.actions.IAction;

import java.util.Scanner;

public class CancelOrderAction implements IAction {
    private final AutoService service;

    public CancelOrderAction(AutoService autoService) {
        this.service = autoService;
    }

    @Override
    public void execute() {
        if (service.getOrdersCount() == 0) {
            System.out.println("Список заказов пуст.");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите id заказа, которое вы хотите отменить: ");
        long id = sc.nextLong();
        if (service.cancelOrder(id)){
            System.out.println("Заказ успешно удален!");
        } else{
            System.out.println("Заказа с данным id не найдено");
        }
    }
}
