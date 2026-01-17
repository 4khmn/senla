package autoservice.ui.factory;


import autoservice.ui.actions.general.BackToMainMenuAction;
import autoservice.ui.actions.general.CsvImportMastersAction;
import autoservice.ui.actions.general.CsvImportOrdersAction;
import autoservice.ui.actions.general.CsvExportOrdersAction;
import autoservice.ui.actions.general.CsvExportGarageSpotsAction;
import autoservice.ui.actions.general.CsvImportGarageSpotsAction;
import autoservice.ui.actions.general.CsvExportMasterAction;
import autoservice.ui.actions.general.GetClosestDateAction;
import autoservice.ui.actions.orders.AddOrderAction;
import autoservice.ui.actions.orders.ActiveOrdersSortAction;
import autoservice.ui.actions.orders.GetOrderByMasterAction;
import autoservice.ui.actions.orders.CancelOrderAction;
import autoservice.ui.actions.orders.OrdersSortByTimeFrameAction;
import autoservice.ui.actions.orders.OrdersSortAction;
import autoservice.ui.actions.orders.ShiftOrderAction;
import autoservice.ui.actions.orders.AddOrderAtCurrentTimeAction;
import autoservice.ui.actions.orders.CloseOrderAction;
import autoservice.ui.actions.orders.DeleteOrderAction;
import autoservice.ui.menu.Navigator;
import config.AppConfig;
import autoservice.model.AutoService;
import autoservice.ui.actions.garageSpots.AddGarageSpotAction;
import autoservice.ui.actions.garageSpots.DeleteGarageSpotAction;
import autoservice.ui.actions.garageSpots.GetFreeSpotsAction;
import autoservice.ui.actions.garageSpots.GetNumberOfFreeSpotsByDateAction;
import autoservice.ui.actions.masters.AddMasterAction;
import autoservice.ui.actions.masters.DeleteMasterAction;
import autoservice.ui.actions.masters.GetMasterByOrderAction;
import autoservice.ui.actions.masters.MastersSortAction;
import autoservice.ui.menu.Menu;
import autoservice.ui.menu.MenuBuilder;
import config.annotation.Component;
import config.annotation.Inject;
@Component
public class ConsoleMenuFactory implements IMenuFactory {
    private final AppConfig appConfig;
    private final AutoService service;
    private final Navigator navigator;
    @Inject
    public ConsoleMenuFactory(AppConfig appConfig, AutoService autoService, Navigator navigator) {
        this.appConfig = appConfig;
        this.service = autoService;
        this.navigator = navigator;
    }
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
        MenuBuilder builder = new MenuBuilder()
                .setName("Меню заказов")
                .addItem("Добавить заказ автоматически", new AddOrderAction(service))
                .addItem("Добавить заказ в конкретное время", new AddOrderAtCurrentTimeAction(service));
        if (appConfig.isOrderAllowToDelete()) {
            builder.addItem("Удалить заказ", new DeleteOrderAction(service));
        }
        builder.addItem("Закрыть заказ", new CloseOrderAction(service))
                .addItem("Отменить заказ", new CancelOrderAction(service));
        if (appConfig.isOrderAllowToShiftTime()) {
            builder.addItem("Задержать заказ", new ShiftOrderAction(service));
        }
                builder.addItem("Список заказов", new OrdersSortAction(service))
                .addItem("Список текущих выполняемых заказов", new ActiveOrdersSortAction(service))
                .addItem("Получить заказ, выполняемый конкретным мастером", new GetOrderByMasterAction(service))
                .addItem("Заказы за промежуток времени", new OrdersSortByTimeFrameAction(service))
                .addItem("Назад в главное меню", new BackToMainMenuAction(navigator));
        return builder.build();
    }

    @Override
    public Menu createMasterMenu() {
        return new MenuBuilder()
                .setName("Меню мастеров")
                .addItem("Добавить мастера", new AddMasterAction(service))
                .addItem("Удалить мастера", new DeleteMasterAction(service))
                .addItem("Получить мастера, выполняющий конкретный заказ", new GetMasterByOrderAction(service))
                .addItem("Список авто-мастеров", new MastersSortAction(service))
                .addItem("Назад в главное меню", new BackToMainMenuAction(navigator))
                .build();
    }

    @Override
    public Menu createGarageSpotMenu() {
        MenuBuilder builder = new MenuBuilder()
                .setName("Меню гаражных мест");
        if (appConfig.isGarageSpotAllowToAddRemove()) {

        builder.addItem("Добавить гаражное место", new AddGarageSpotAction(service))
                .addItem("Удалить гаражное место", new DeleteGarageSpotAction(service));
        }
        builder.addItem("Список свободных мест в сервисных гаражах", new GetFreeSpotsAction(service))
                .addItem("Количество свободных мест на сервисе на любую дату в будущем", new GetNumberOfFreeSpotsByDateAction(service))
                .addItem("Назад в главное меню", new BackToMainMenuAction(navigator));
        return builder.build();
    }

    @Override
    public Menu createGeneralMenu() {
        return new MenuBuilder()
                .setName("Прочие действия")
                .addItem("Найти ближайшую свободную дату", new GetClosestDateAction(service))
                .addSubMenu("Экспорт данных", createExportMenu())
                .addSubMenu("Импорт данных", createImportMenu())
                .addItem("Назад в главное меню", new BackToMainMenuAction(navigator))
                .build();
    }

    @Override
    public Menu createExportMenu() {
        return new MenuBuilder()
                .setName("Эспорт данных")
                .addItem("Экспортировать данные о заказах", new CsvExportOrdersAction(service))
                .addItem("Экспортировать данные о мастерах", new CsvExportMasterAction(service))
                .addItem("Экспортировать данные о гаражных местах", new CsvExportGarageSpotsAction(service))
                .addItem("Назад в главное меню", new BackToMainMenuAction(navigator))
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
                .addItem("Назад в главное меню", new BackToMainMenuAction(navigator))
                .build();
    }
}