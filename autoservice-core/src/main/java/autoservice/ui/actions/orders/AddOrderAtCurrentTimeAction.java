package autoservice.ui.actions.orders;

import autoservice.model.AutoService;
import autoservice.model.exceptions.DBException;
import autoservice.ui.actions.IAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Scanner;

public class AddOrderAtCurrentTimeAction implements IAction {
    private final AutoService service;

    public AddOrderAtCurrentTimeAction(AutoService autoService) {
        this.service = autoService;
    }

    @Override
    public void execute() {
        if (service.getMastersCount() == 0 || service.getGarageSpotsCount() == 0) {
            System.out.println("В автосервисе отсувствуют мастера или гаражные места");
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

        System.out.print("Введите описание заказа: ");
        String desc = sc.nextLine();
        int duration = 0;
        while(true) {
            try {
                System.out.print("Введите длительность заказа в часах: ");
                duration = sc.nextInt();
                if (duration>0){
                    break;
                }
                else{
                    System.out.println("Минимальная длительность заказа: 1");
                }
            }
            catch(Exception e){
                System.out.println("Ошибка ввода!");
                sc.next();
            }
        }
        while(true) {
            try {
                System.out.print("Введите стоимость услуги: ");
                BigDecimal price = sc.nextBigDecimal();
                try {
                    long idOfOrder = service.addOrderAtCurrentTime(date, desc, duration, price);
                    if (idOfOrder!=-1){
                        System.out.println("Заказ успешно добавлен.");
                    }
                    else{
                        System.out.println("В данное время заказ добавить нельзя. ");
                    }
                }
                catch(DBException e){
                    System.out.println(e.getMessage());
                }
                break;
            }
            catch(Exception e){
                System.out.println("Ошибка ввода!");
                sc.next();
            }
        }
    }
}
