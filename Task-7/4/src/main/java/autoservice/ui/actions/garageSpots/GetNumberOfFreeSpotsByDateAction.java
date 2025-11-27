package main.java.autoservice.ui.actions.garageSpots;

import main.java.autoservice.model.AutoService;
import main.java.autoservice.ui.actions.IAction;

import java.time.LocalDateTime;
import java.util.Scanner;

public class GetNumberOfFreeSpotsByDateAction implements IAction {

    private final AutoService service;

    public GetNumberOfFreeSpotsByDateAction(AutoService autoService) {
        this.service = autoService;
    }
    @Override
    public void execute() {
        if (service.getGarageSpotsCount() == 0) {
            System.out.println("В авто-сервисе нету гаражных мест.");
            return;
        }
        Scanner sc = new Scanner(System.in);
        int hour;
        int day;
        int month;
        int year;
        LocalDateTime date;
        while(true) {
            System.out.println("Введите интересующую вас дату в формате <hh.dd.mm.yyyy>: ");
            String inputDate = sc.nextLine();
            String[] split = inputDate.split("\\.");
            if (split.length == 4) {

                hour = Integer.parseInt(split[0]);
                day = Integer.parseInt(split[1]);
                month = Integer.parseInt(split[2]);
                year = Integer.parseInt(split[3]);
                try{
                    date = LocalDateTime.of(year, month, day, hour, 0);
                    break;
                } catch(Exception e){
                    System.out.println("Неверный ввод даты, попробуйте еще раз!");
                }
            } else {
                System.out.println("Неверный ввод даты, попробуйте еще раз!");
            }
        }
        int freeSpotsByDate = service.getNumberOfFreeSpotsByDate(date);
        if (freeSpotsByDate>0) {
            System.out.println("Всего свободных мест сейчас: " + freeSpotsByDate);
        } else{
            System.out.println("Свободных мест в это время нету.");
        }
    }
}
