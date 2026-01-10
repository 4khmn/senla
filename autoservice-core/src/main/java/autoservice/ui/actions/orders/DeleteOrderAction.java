package autoservice.ui.actions.orders;

import autoservice.model.AutoService;
import autoservice.model.exceptions.DBException;
import autoservice.ui.actions.IAction;

import java.util.Scanner;

public class DeleteOrderAction implements IAction {
    private final AutoService service;

    public DeleteOrderAction(AutoService autoService) {
        this.service = autoService;
    }

    @Override
    public void execute() {
        if (service.getOrdersCount() == 0) {
            System.out.println("Список заказов пуст.");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите id заказа, которое вы хотите удалить: ");
        long id = sc.nextLong();
        try {
            service.deleteOrder(id);
            System.out.println("Заказ успешно удален!");
        }
        catch (DBException e) {
            System.out.println("Заказа с данным id не найдено");
        }
    }
}
