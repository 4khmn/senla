package autoservice.ui.actions.orders;

import autoservice.model.AutoService;
import autoservice.model.entities.Order;
import autoservice.model.enums.OrdersSortEnum;
import autoservice.ui.actions.IAction;

import java.util.List;
import java.util.Scanner;

public class OrdersSortAction implements IAction {
    private final AutoService service;

    public OrdersSortAction(AutoService autoService) {
        this.service = autoService;
    }

    @Override
    public void execute() {
        if (service.getOrdersCount() == 0) {
            System.out.println("Список заказов пуст.");
            return;
        }
        System.out.println("Выберите критерий сортировки:\n" +
                "1 - по дате подачи\n" +
                "2 - по дате выполнения\n" +
                "3 - по дате планируемого начала выполнения\n" +
                "4 - по цене");
        System.out.print("Ваш выбор: ");
        Scanner sc = new Scanner(System.in);
        OrdersSortEnum sortType = null;
        while (sortType == null) {
            int decision = sc.nextInt();
            sortType = switch (decision) {
                case 1 -> OrdersSortEnum.BY_CREATION_DATE;
                case 2 -> OrdersSortEnum.BY_END_DATE;
                case 3 -> OrdersSortEnum.BY_START_DATE;
                case 4 -> OrdersSortEnum.BY_PRICE;
                default -> {
                    System.out.println("Попробуйте еще раз!");
                    yield null;
                }
            };
        }
        List<Order> orders = service.ordersSort(sortType);

        System.out.println("Списко заказов: ");
        for (var v : orders) {
            System.out.println(v);
        }
    }
}
