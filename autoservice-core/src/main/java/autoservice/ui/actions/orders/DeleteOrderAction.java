package autoservice.ui.actions.orders;

import autoservice.model.exceptions.DBException;
import autoservice.model.service.OrderService;
import autoservice.ui.actions.IAction;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;
@RequiredArgsConstructor
public class DeleteOrderAction implements IAction {
    private final OrderService orderService;

    @Override
    public void execute() {
        if (orderService.getOrdersCount() == 0) {
            System.out.println("Список заказов пуст.");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите id заказа, которое вы хотите удалить: ");
        long id = sc.nextLong();
        try {
            orderService.deleteOrder(id);
            System.out.println("Заказ успешно удален!");
        } catch (DBException e) {
            System.out.println("Заказа с данным id не найдено");
        }  catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
