package autoservice.ui.actions.orders;

import autoservice.model.AutoService;
import autoservice.model.entities.Order;
import autoservice.model.enums.OrdersSortByTimeFrameEnum;
import autoservice.model.exceptions.DBException;
import autoservice.ui.actions.IAction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class OrdersSortByTimeFrameAction implements IAction {
    private final AutoService service;

    public OrdersSortByTimeFrameAction(AutoService autoService) {
        this.service = autoService;
    }

    @Override
    public void execute() {
        if (service.getOrdersCount() == 0) {
            System.out.println("Список заказов пуст.");
            return;
        }
        Scanner sc = new Scanner(System.in);
        int hour;
        int day;
        int month;
        int year;
        LocalDateTime startDate;
        LocalDateTime endDate;
        while (true) {
            while (true) {
                System.out.println("Введите начальное время в формате <hh.dd.mm.yyyy>: ");
                String inputDate = sc.nextLine();
                String[] split = inputDate.split("\\.");
                if (split.length == 4) {

                    hour = Integer.parseInt(split[0]);
                    day = Integer.parseInt(split[1]);
                    month = Integer.parseInt(split[2]);
                    year = Integer.parseInt(split[3]);
                    try {
                        startDate = LocalDateTime.of(year, month, day, hour, 0);
                        break;
                    } catch (Exception e) {
                        System.out.println("Неверный ввод даты, попробуйте еще раз!");
                    }
                } else {
                    System.out.println("Неверный ввод даты, попробуйте еще раз!");
                }
            }
            while (true) {
                System.out.println("Введите конечное время в формате <hh.dd.mm.yyyy>: ");
                String inputDate = sc.nextLine();
                String[] split = inputDate.split("\\.");
                if (split.length == 4) {
                    hour = Integer.parseInt(split[0]);
                    day = Integer.parseInt(split[1]);
                    month = Integer.parseInt(split[2]);
                    year = Integer.parseInt(split[3]);
                    try {
                        endDate = LocalDateTime.of(year, month, day, hour, 0);
                        break;
                    } catch (Exception e) {
                        System.out.println("Неверный ввод даты, попробуйте еще раз!");
                    }
                } else {
                    System.out.println("Неверный ввод даты, попробуйте еще раз!");
                }
            }
            if (startDate.isAfter(endDate)) {
                System.out.println("Начальное время не может быть после конечного!");
            } else {
                break;
            }
        }
        System.out.println("Выберите критерий сортировки:\n" +
                "1 - по дате подачи\n" +
                "2 - по дате выполнения\n" +
                "3 - по цене");
        System.out.print("Ваш выбор: ");
        OrdersSortByTimeFrameEnum sortType = null;
        while (sortType == null) {
            int decision = sc.nextInt();
            sortType = switch (decision) {
                case 1 -> OrdersSortByTimeFrameEnum.BY_CREATION_DATE;
                case 2 -> OrdersSortByTimeFrameEnum.BY_END_DATE;
                case 3 -> OrdersSortByTimeFrameEnum.BY_PRICE;
                default -> {
                    System.out.println("Попробуйте еще раз!");
                    yield null;
                }
            };
        }
        try {
            List<Order> orders = service.ordersSortByTimeFrame(startDate, endDate, sortType);
            if (orders.size() == 0) {
                System.out.println("За выбранное время заказов нету.");
            } else {
                System.out.println("Списко заказов: ");
                for (var v : orders) {
                    System.out.println(v);
                }
            }
        } catch (DBException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
