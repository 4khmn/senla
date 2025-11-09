package autoservice.ui.actions.orders;

import autoservice.model.AutoService;
import autoservice.model.entities.Master;
import autoservice.model.entities.Order;
import autoservice.ui.actions.IAction;

import java.util.Scanner;

public class GetOrderByMasterAction implements IAction {
    private final AutoService service;

    public GetOrderByMasterAction(AutoService autoService) {
        this.service = autoService;
    }

    @Override
    public void execute() {
        if (service.getOrdersCount() == 0) {
            System.out.println("Список заказов пуст.");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите id мастера, заказ которого вы хотите получить: ");
        long masterId = sc.nextInt();
        Master master = service.getMasterById(masterId);
        if (master != null) {
            Order orderByMaster = service.getOrderByMaster(master);
            System.out.println(orderByMaster);
        }
        else{
            System.out.println("Id мастера введено не верно.");
        }
    }
}
