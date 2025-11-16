package autoservice.ui.actions.garageSpots;

import autoservice.model.AutoService;
import autoservice.ui.actions.IAction;

import java.util.Scanner;

public class DeleteGarageSpotAction implements IAction {

    private final AutoService service;

    public DeleteGarageSpotAction(AutoService autoService) {
        this.service = autoService;
    }

    @Override
    public void execute() {
        if (service.getGarageSpotsCount() == 0) {
            System.out.println("В авто-сервисе нету гаражных мест.");
            return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.print("Введите id гаражного места, которое вы хотите удалить: ");
        long id = sc.nextLong();
        if (service.getGarageSpotById(id) != null) {
            if (service.getGarageSpotById(id).scheduleIsEmpty() == true) {
                service.deleteGarageSpot(id);
                System.out.println("Гаражное место успешно удалено!");
            } else {
                System.out.println("Это место еще обслуживает заказы!");
            }
        } else {
            System.out.println("Гаражного места с данным id не найдено");
        }
    }
}
