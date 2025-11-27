package main.java.autoservice.ui.actions.masters;

import main.java.autoservice.model.AutoService;
import main.java.autoservice.model.entities.Master;
import main.java.autoservice.model.enums.MastersSortEnum;
import main.java.autoservice.ui.actions.IAction;

import java.util.List;
import java.util.Scanner;

public class MastersSortAction implements IAction {
    private final AutoService service;

    public MastersSortAction(AutoService autoService) {
        this.service = autoService;
    }

    @Override
    public void execute() {
        if (service.getMastersCount() == 0) {
            System.out.println("В авто-сервисе нету мастеров.");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("Выберите критерий сортировки:\n" +
                "1 - по алфавиту\n" +
                "2 - по занятости");
        System.out.print("Ваш выбор: ");
        MastersSortEnum sortType = null;
        while (sortType == null) {
            int decision = sc.nextInt();
            sortType = switch (decision) {
                case 1 -> MastersSortEnum.BY_NAME;
                case 2 -> MastersSortEnum.BY_EMPLOYMENT;
                default -> {
                    System.out.println("Попробуйте еще раз!");
                    yield null;
                }
            };
        }
        List<Master> masters = service.mastersSort(sortType);

        System.out.println("Списко заказов: ");
        for (var v : masters) {
            System.out.println(v);
        }
    }
}
