package autoservice.ui.actions.orders;

import autoservice.model.entities.Master;
import autoservice.model.entities.Order;
import autoservice.model.exceptions.DBException;
import autoservice.model.service.MasterService;
import autoservice.model.service.OrderService;
import autoservice.ui.actions.IAction;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;
@RequiredArgsConstructor
public class GetOrderByMasterAction implements IAction {
    private final OrderService orderService;
    private final MasterService masterService;

    @Override
    public void execute() {
        if (orderService.getOrdersCount() == 0) {
            System.out.println("Список заказов пуст.");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите id мастера, заказ которого вы хотите получить: ");
        long masterId = sc.nextInt();
        Master master = masterService.getMasterById(masterId);
        if (master != null) {
            try {
                Order orderByMaster = orderService.getOrderByMaster(master);
                System.out.println(orderByMaster);
            } catch (DBException e) {
                System.out.println(e.getMessage());
            }  catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Id мастера введено не верно.");
        }
    }
}
