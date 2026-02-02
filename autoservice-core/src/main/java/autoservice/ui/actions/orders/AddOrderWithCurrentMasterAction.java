package autoservice.ui.actions.orders;

import autoservice.model.exceptions.DBException;
import autoservice.model.service.GarageSpotService;
import autoservice.model.service.MasterService;
import autoservice.model.service.OrderService;
import autoservice.ui.actions.IAction;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Scanner;

@RequiredArgsConstructor
public class AddOrderWithCurrentMasterAction implements IAction {
    private final MasterService masterService;
    private final OrderService orderService;
    private final GarageSpotService garageSpotService;

    @Override
    public void execute() {
        if (masterService.getMastersCount() == 0 || garageSpotService.getGarageSpotsCount() == 0) {
            System.out.println("В автосервисе отсувствуют мастера или гаражные места");
            return;
        }
        Scanner sc = new Scanner(System.in);
        int hour;
        int day;
        int month;
        int year;
        Long masterId;
        System.out.println("Введите id интересующего вас мастера");
        masterId = sc.nextLong();
        System.out.print("Введите описание заказа: ");
        sc.nextLine();
        String desc = sc.nextLine();
        int duration = 0;
        while (true) {
            try {
                System.out.print("Введите длительность заказа в часах: ");
                duration = sc.nextInt();
                if (duration > 0) {
                    break;
                } else {
                    System.out.println("Минимальная длительность заказа: 1");
                }
            } catch (Exception e) {
                System.out.println("Ошибка ввода!");
                sc.next();
            }
        }
        while (true) {
            try {
                System.out.print("Введите стоимость услуги: ");
                BigDecimal price = sc.nextBigDecimal();
                try {
                    long idOfOrder = orderService.addOrderWithCurrentMaster(desc, duration, price, masterId);
                    if (idOfOrder != -1) {
                        System.out.println("Заказ успешно добавлен.");
                    } else {
                        System.out.println("В данное время заказ добавить нельзя. ");
                    }
                } catch (DBException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            } catch (Exception e) {
                System.out.println("Ошибка ввода!");
                sc.next();
            }
        }
    }
}
