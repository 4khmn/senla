package autoservice.ui.actions.garageSpots;

import autoservice.model.service.GeneralService;
import autoservice.model.exceptions.DBException;
import autoservice.model.service.GarageSpotService;
import autoservice.ui.actions.IAction;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Scanner;
@RequiredArgsConstructor
public class GetNumberOfFreeSpotsByDateAction implements IAction {

    private final GarageSpotService garageSpotService;
    private final GeneralService generalService;

    @Override
    public void execute() {
        if (garageSpotService.getGarageSpotsCount() == 0) {
            System.out.println("В авто-сервисе нету гаражных мест.");
            return;
        }
        Scanner sc = new Scanner(System.in);
        int hour;
        int day;
        int month;
        int year;
        LocalDateTime date;
        while (true) {
            System.out.println("Введите интересующую вас дату в формате <hh.dd.mm.yyyy>: ");
            String inputDate = sc.nextLine();
            String[] split = inputDate.split("\\.");
            if (split.length == 4) {

                hour = Integer.parseInt(split[0]);
                day = Integer.parseInt(split[1]);
                month = Integer.parseInt(split[2]);
                year = Integer.parseInt(split[3]);
                try {
                    date = LocalDateTime.of(year, month, day, hour, 0);
                    if (date.isBefore(LocalDateTime.now())) {
                        throw new RuntimeException();
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Неверный ввод даты, попробуйте еще раз!");
                }
            } else {
                System.out.println("Неверный ввод даты, попробуйте еще раз!");
            }
        }
        try {
            int freeSpotsByDate = generalService.getNumberOfFreeSpotsByDate(date);
            if (freeSpotsByDate > 0) {
                System.out.println("Всего свободных мест а выбранное время: " + freeSpotsByDate);
            } else {
                System.out.println("Свободных мест в это время нету.");
            }
        } catch (DBException e) {
            System.out.println(e.getMessage());
        }  catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
