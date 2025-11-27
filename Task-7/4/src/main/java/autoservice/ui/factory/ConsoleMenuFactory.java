package main.java.autoservice.ui.factory;

import main.java.autoservice.model.AutoService;
import main.java.autoservice.ui.actions.garageSpots.AddGarageSpotAction;
import main.java.autoservice.ui.actions.garageSpots.DeleteGarageSpotAction;
import main.java.autoservice.ui.actions.garageSpots.GetFreeSpotsAction;
import main.java.autoservice.ui.actions.garageSpots.GetNumberOfFreeSpotsByDateAction;
import main.java.autoservice.ui.actions.general.*;
import main.java.autoservice.ui.actions.general.*;
import main.java.autoservice.ui.actions.masters.AddMasterAction;
import main.java.autoservice.ui.actions.masters.DeleteMasterAction;
import main.java.autoservice.ui.actions.masters.GetMasterByOrderAction;
import main.java.autoservice.ui.actions.masters.MastersSortAction;
import main.java.autoservice.ui.actions.orders.*;
import main.java.autoservice.ui.actions.orders.*;
import main.java.autoservice.ui.menu.Menu;
import main.java.autoservice.ui.menu.MenuBuilder;

public class ConsoleMenuFactory implements IMenuFactory {
    private final AutoService service = AutoService.getInstance();
    @Override
    public Menu createMainMenu() {
        Menu orderMenu = createOrderMenu();
        Menu masterMenu = createMasterMenu();
        Menu garageMenu = createGarageSpotMenu();
        Menu generalMenu = createGeneralMenu();

        return new MenuBuilder()
                .setName("Главное меню")
                .addSubMenu("Работа с заказами", orderMenu)
                .addSubMenu("Работа с мастерами", masterMenu)
                .addSubMenu("Работа с гаражными местами", garageMenu)
                .addSubMenu("Прочее", generalMenu)
                .build();
    }

    @Override
    public Menu createOrderMenu() {
        return new MenuBuilder()
                .setName("Меню заказов")
                .addItem("Добавить заказ автоматически", new AddOrderAction(service))
                .addItem("Добавить заказ в конкретное время", new AddOrderAtCurrentTimeAction(service))
                .addItem("Удалить заказ" , new DeleteOrderAction(service))
                .addItem("Закрыть заказ", new CloseOrderAction(service))
                .addItem("Отменить заказ", new CancelOrderAction(service))
                .addItem("Задержать заказ", new ShiftOrderAction(service))
                .addItem("Список заказов", new OrdersSortAction(service))
                .addItem("Список текущих выполняемых заказов", new ActiveOrdersSortAction(service))
                .addItem("Получить заказ, выполняемый конкретным мастером", new GetOrderByMasterAction(service))
                .addItem("Заказы за промежуток времени", new OrdersSortByTimeFrameAction(service))
                .addItem("Назад в главное меню", new BackToMainMenuAction())
                .build();
    }

    @Override
    public Menu createMasterMenu() {
        return new MenuBuilder()
                .setName("Меню мастеров")
                .addItem("Добавить мастера" , new AddMasterAction(service))
                .addItem("Удалить мастера", new DeleteMasterAction(service))
                .addItem("Получить мастера, выполняющий конкретный заказ", new GetMasterByOrderAction(service))
                .addItem("Список авто-мастеров", new MastersSortAction(service))
                .addItem("Назад в главное меню", new BackToMainMenuAction())
                .build();
    }

    @Override
    public Menu createGarageSpotMenu() {
        return new MenuBuilder()
                .setName("Меню гаражных мест")
                .addItem("Добавить гаражное место", new AddGarageSpotAction(service))
                .addItem("Удалить гаражное место", new DeleteGarageSpotAction(service))
                .addItem("Список свободных мест в сервисных гаражах", new GetFreeSpotsAction(service))
                .addItem("Количество свободных мест на сервисе на любую дату в будущем", new GetNumberOfFreeSpotsByDateAction(service))
                .addItem("Назад в главное меню", new BackToMainMenuAction())
                .build();
    }

    @Override
    public Menu createGeneralMenu() {
        return new MenuBuilder()
                .setName("Прочие действия")
                .addItem("Найти ближайшую свободную дату", new GetClosestDateAction(service))
                .addSubMenu("Экспорт данных", createExportMenu())
                .addSubMenu("Импорт данных", createImportMenu())
                .addItem("Назад в главное меню", new BackToMainMenuAction())
                .build();
    }

    @Override
    public Menu createExportMenu() {
        return new MenuBuilder()
                .setName("Эспорт данных")
                .addItem("Экспортировать данные о заказах", new CsvExportOrdersAction(service))
                .addItem("Экспортировать данные о мастерах", new CsvExportMasterAction(service))
                .addItem("Экспортировать данные о гаражных местах", new CsvExportGarageSpotsAction(service))
                .addItem("Назад в главное меню", new BackToMainMenuAction())
                .build();
    }

    @Override
    public Menu createImportMenu() {
        return new MenuBuilder()
                .setName("Импорт данных")
                .setHint("Обратите внимание, данные в системе являются приоритетными!")
                .addItem("Импортировать данные о заказах", new CsvImportOrdersAction(service))
                .addItem("Импортировать данные о мастерах", new CsvImportMastersAction(service))
                .addItem("Импортировать данные о гаражных местах", new CsvImportGarageSpotsAction(service))
                .addItem("Назад в главное меню", new BackToMainMenuAction())
                .build();
    }
}