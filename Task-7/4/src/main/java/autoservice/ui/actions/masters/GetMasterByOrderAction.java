package main.java.autoservice.ui.actions.masters;

import main.java.autoservice.model.AutoService;
import main.java.autoservice.model.entities.Master;
import main.java.autoservice.model.entities.Order;
import main.java.autoservice.ui.actions.IAction;

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
        if (service.getOrdersCount()==0){
            System.out.println("Список заказов пуст.");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите id заказа, мастера которого вы хотите получить: ");
        long orderId = sc.nextInt();
        Order order = service.getOrderById(orderId);
        if (order != null) {
            Master masterByOrder = service.getMasterByOrder(order);
            System.out.println(masterByOrder);
        }
        else{
            System.out.println("Id заказа введено не верно");
        }
    }
}
