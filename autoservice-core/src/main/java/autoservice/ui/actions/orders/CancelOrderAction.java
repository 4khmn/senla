package autoservice.ui.actions.orders;

import autoservice.model.exceptions.DBException;
import autoservice.model.service.OrderService;
import autoservice.ui.actions.IAction;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;
@RequiredArgsConstructor
public class CancelOrderAction implements IAction {
    private final OrderService orderService;

    @Override
    public void execute() {
        if (orderService.getOrdersCount() == 0) {
            System.out.println("Список заказов пуст.");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите id заказа, которое вы хотите отменить: ");
        long id = sc.nextLong();
        try {
            if (orderService.cancelOrder(id)) {
                System.out.println("Заказ успешно от отменен!");
            } else {
                System.out.println("Заказа с данным id не найдено");
            }
        } catch (DBException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
