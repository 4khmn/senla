package autoservice.ui.actions.masters;

import autoservice.model.exceptions.DBException;
import autoservice.model.service.MasterService;
import autoservice.ui.actions.IAction;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Scanner;
@RequiredArgsConstructor
public class AddMasterAction implements IAction {

    private final MasterService masterService;


    @Override
    public void execute() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите имя мастера: ");
        String name = sc.nextLine();
        while (true) {
            System.out.print("Введите его зарплату: ");
            try {
                BigDecimal salary = sc.nextBigDecimal();
                try {
                    masterService.addMaster(name, salary);
                } catch (DBException e) {
                    System.out.println(e.getMessage());
                }
                break;
            } catch (Exception e) {
                System.out.println("неверный ввод");
                sc.next();
            }
        }
        System.out.println("Мастер успешно добавлен!");
        System.out.println("Всего доступных мастеров: " + masterService.getMastersCount());
    }
}
