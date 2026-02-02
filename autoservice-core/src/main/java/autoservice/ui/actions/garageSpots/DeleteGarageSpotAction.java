package autoservice.ui.actions.garageSpots;

import autoservice.model.exceptions.DBException;
import autoservice.model.service.GarageSpotService;
import autoservice.ui.actions.IAction;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;
@RequiredArgsConstructor
public class DeleteGarageSpotAction implements IAction {

    private final GarageSpotService garageSpotService;


    @Override
    public void execute() {
        if (garageSpotService.getGarageSpotsCount() == 0) {
            System.out.println("В авто-сервисе нету гаражных мест.");
            return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.print("Введите id гаражного места, которое вы хотите удалить: ");
        long id = sc.nextLong();
        try {
            if (garageSpotService.getGarageSpotById(id) != null) {
                if (garageSpotService.getGarageSpotById(id).scheduleIsEmpty() == true) {
                    garageSpotService.deleteGarageSpot(id);
                    System.out.println("Гаражное место успешно удалено!");
                } else {
                    System.out.println("Это место еще обслуживает заказы!");
                }
            } else {
                System.out.println("Гаражного места с данным id не найдено");
            }
        } catch (DBException e) {
            System.out.println(e.getMessage());
        }  catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
