package autoservice.ui.actions.orders;

import autoservice.model.AutoService;
import autoservice.ui.actions.IAction;

import java.util.Scanner;

public class CloseOrderAction implements IAction {
    private final AutoService service;

    public CloseOrderAction(AutoService autoService) {
        this.service = autoService;
    }

    @Override
    public void execute() {
        if (service.getOrdersCount() == 0) {
            System.out.println("Список заказов пуст.");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите id заказа, которое вы хотите закрыть: ");
        long id = sc.nextLong();
        if (service.closeOrder(id)){
            System.out.println("Заказ успешно закрыт!");
        } else{
            System.out.println("Заказа с данным id не найдено");
        }
    }
}
