package autoservice.ui.actions.orders;

import autoservice.model.service.OrderService;
import autoservice.ui.actions.IAction;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;
@RequiredArgsConstructor
public class ShiftOrderAction implements IAction {
    private final OrderService orderService;


    @Override
    public void execute() {
        if (orderService.getOrdersCount() == 0) {
            System.out.println("Список заказов пуст.");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите id заказа, которое вы хотите продлить: ");
        long id = sc.nextLong();
        while (true) {
            System.out.print("Введите длительность в часах (на сколько часов продлить заказ): ");
            int durationInHours = sc.nextInt();
            if (durationInHours > 0) {
                try {
                    if (orderService.shiftOrder(id, durationInHours)) {
                        System.out.println("Заказ успешно сдвинут.");
                    } else {
                        System.out.println("Id заказа введено не верно.");
                    }
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }  catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            } else {
                System.out.println("Минимальная длительность в часах - 1");
                sc.next();
            }
        }
    }
}
