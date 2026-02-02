package autoservice.ui.actions.masters;

import autoservice.model.entities.Master;
import autoservice.model.entities.Order;
import autoservice.model.exceptions.DBException;
import autoservice.model.service.MasterService;
import autoservice.model.service.OrderService;
import autoservice.ui.actions.IAction;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;
@RequiredArgsConstructor
public class GetMasterByOrderAction implements IAction {
    private final MasterService masterService;
    private final OrderService orderService;

    @Override
    public void execute() {
        if (masterService.getMastersCount() == 0) {
            System.out.println("В авто-сервисе нету мастеров.");
            return;
        }
        if (orderService.getOrdersCount() == 0) {
            System.out.println("Список заказов пуст.");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите id заказа, мастера которого вы хотите получить: ");
        long orderId = sc.nextInt();
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            try {
                Master masterByOrder = masterService.getMasterByOrder(order);
                System.out.println(masterByOrder);
            } catch (DBException e) {
                System.out.println(e.getMessage());
            }  catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Id заказа введено не верно");
        }
    }
}
