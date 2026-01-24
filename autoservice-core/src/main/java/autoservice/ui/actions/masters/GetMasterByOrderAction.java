package autoservice.ui.actions.masters;

import autoservice.model.AutoService;
import autoservice.model.entities.Master;
import autoservice.model.entities.Order;
import autoservice.model.exceptions.DBException;
import autoservice.ui.actions.IAction;

import java.util.Scanner;

public class GetMasterByOrderAction implements IAction {
    private final AutoService service;

    public GetMasterByOrderAction(AutoService autoService) {
        this.service = autoService;
    }

    @Override
    public void execute() {
        if (service.getMastersCount() == 0) {
            System.out.println("В авто-сервисе нету мастеров.");
            return;
        }
        if (service.getOrdersCount() == 0) {
            System.out.println("Список заказов пуст.");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите id заказа, мастера которого вы хотите получить: ");
        long orderId = sc.nextInt();
        Order order = service.getOrderById(orderId);
        if (order != null) {
            try {
                Master masterByOrder = service.getMasterByOrder(order);
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
