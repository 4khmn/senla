package autoservice.ui.actions.orders;

import autoservice.model.AutoService;
import autoservice.model.entities.Order;
import autoservice.model.enums.ActiveOrdersSortEnum;
import autoservice.model.exceptions.DBException;
import autoservice.ui.actions.IAction;

import java.util.List;
import java.util.Scanner;

public class ActiveOrdersSortAction implements IAction {
    private final AutoService service;

    public ActiveOrdersSortAction(AutoService autoService) {
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
                "3 - по цене");
        System.out.print("Ваш выбор: ");
        Scanner sc = new Scanner(System.in);
        ActiveOrdersSortEnum sortType = null;
        while (sortType == null) {
            int decision = sc.nextInt();
            sortType = switch (decision) {
                case 1 -> ActiveOrdersSortEnum.BY_CREATION_DATE;
                case 2 -> ActiveOrdersSortEnum.BY_END_DATE;
                case 3 -> ActiveOrdersSortEnum.BY_PRICE;
                default -> {
                    System.out.println("Попробуйте еще раз!");
                    yield null;
                }
            };
        }
        try{
            List<Order> orders = service.activeOrdersSort(sortType);
            System.out.println("Списко заказов: ");
            for (var v : orders) {
                System.out.println(v);
            }
        }
        catch (DBException e){
            System.out.println(e.getMessage());
        }
    }
}
