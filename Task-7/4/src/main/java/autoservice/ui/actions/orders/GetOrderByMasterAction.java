package main.java.autoservice.ui.actions.orders;

import main.java.autoservice.model.AutoService;
import main.java.autoservice.model.entities.Master;
import main.java.autoservice.model.entities.Order;
import main.java.autoservice.ui.actions.IAction;

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
