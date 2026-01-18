package autoservice.ui.actions.masters;

import autoservice.model.AutoService;
import autoservice.model.exceptions.DBException;
import autoservice.ui.actions.IAction;

import java.util.Scanner;

public class DeleteMasterAction implements IAction {

    private final AutoService service;

    public DeleteMasterAction(AutoService autoService) {
        this.service = autoService;
    }
    @Override
    public void execute() {
        if (service.getMastersCount() == 0) {
            System.out.println("В авто-сервисе нету мастеров.");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите id мастера, которого вы хотите удалить: ");
        long id = sc.nextLong();
        try {
            service.deleteMaster(id);
            System.out.println("Мастер успешно удален!");
        } catch (DBException e) {
            System.out.println("Мастера с данным ID не найдено!");
        }
    }
}
