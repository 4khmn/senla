package main.java.autoservice.ui.actions.masters;

import main.java.autoservice.model.AutoService;
import main.java.autoservice.ui.actions.IAction;

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
        if (service.deleteMaster(id)){
            System.out.println("Мастер успешно удален!");
        } else{
            System.out.println("Мастера с данным id не найдено");
        }
    }
}
