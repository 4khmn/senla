package main.java.autoservice.ui.actions.masters;

import main.java.autoservice.model.AutoService;
import main.java.autoservice.ui.actions.IAction;

import java.math.BigDecimal;
import java.util.Scanner;

public class AddMasterAction implements IAction {

    private final AutoService service;

    public AddMasterAction(AutoService autoService) {
        this.service = autoService;
    }
    @Override
    public void execute() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите имя мастера: ");
        String name = sc.nextLine();
        while(true) {
            System.out.print("Введите его зарплату: ");
            try {
                BigDecimal salary = sc.nextBigDecimal();
                service.addMaster(name, salary);
                break;
            } catch (Exception e) {
                System.out.println("неверный ввод");
                sc.next();
            }
        }
        System.out.println("Мастер успешно добавлен!");
        System.out.println("Всего доступных мастеров: " + service.getMastersCount());
    }
}
