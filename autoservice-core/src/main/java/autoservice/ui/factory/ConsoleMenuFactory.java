package autoservice.ui.factory;


import autoservice.model.service.GeneralService;
import autoservice.model.config.PropertyConfig;
import autoservice.model.service.io.exports.GarageSpotsCsvExportService;
import autoservice.model.service.io.exports.MastersCsvExportService;
import autoservice.model.service.io.exports.OrdersCsvExportService;
import autoservice.model.service.io.imports.CsvImportService;
import autoservice.model.service.GarageSpotService;
import autoservice.model.service.MasterService;
import autoservice.model.service.OrderService;
import autoservice.ui.actions.general.BackToMainMenuAction;
import autoservice.ui.actions.general.CsvImportMastersAction;
import autoservice.ui.actions.general.CsvImportOrdersAction;
import autoservice.ui.actions.general.CsvExportOrdersAction;
import autoservice.ui.actions.general.CsvExportGarageSpotsAction;
import autoservice.ui.actions.general.CsvImportGarageSpotsAction;
import autoservice.ui.actions.general.CsvExportMasterAction;
import autoservice.ui.actions.general.GetClosestDateAction;
import autoservice.ui.actions.orders.ActiveOrdersSortAction;
import autoservice.ui.actions.orders.GetOrderByMasterAction;
import autoservice.ui.actions.orders.OrdersSortAction;
import autoservice.ui.actions.orders.OrdersSortByTimeFrameAction;
import autoservice.ui.actions.orders.ShiftOrderAction;
import autoservice.ui.actions.orders.AddOrderAction;
import autoservice.ui.actions.orders.AddOrderWithCurrentMasterAction;
import autoservice.ui.actions.orders.CloseOrderAction;
import autoservice.ui.actions.orders.DeleteOrderAction;
import autoservice.ui.actions.orders.AddOrderAtCurrentTimeAction;
import autoservice.ui.actions.orders.CancelOrderAction;
import autoservice.ui.menu.Navigator;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
@RequiredArgsConstructor
@Component
public class ConsoleMenuFactory implements IMenuFactory {
    private final PropertyConfig propertyConfig;
    private final GarageSpotService garageSpotService;
    private final OrderService orderService;
    private final MasterService masterService;
    private final GeneralService generalService;
    private final Navigator navigator;
    private final CsvImportService  csvImportService;
    private final GarageSpotsCsvExportService  garageSpotsCsvExportService;
    private final MastersCsvExportService mastersCsvExportService;
    private final OrdersCsvExportService  ordersCsvExportService;




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
                .addItem("Добавить заказ автоматически", new AddOrderAction(masterService, garageSpotService, orderService))
                .addItem("Записаться к конкретному мастеру", new AddOrderWithCurrentMasterAction(masterService, orderService, garageSpotService))
                .addItem("Добавить заказ в конкретное время", new AddOrderAtCurrentTimeAction(masterService, garageSpotService, orderService));
        if (propertyConfig.isOrderAllowToDelete()) {
            builder.addItem("Удалить заказ", new DeleteOrderAction(orderService));
        }
        builder.addItem("Закрыть заказ", new CloseOrderAction(orderService))
                .addItem("Отменить заказ", new CancelOrderAction(orderService));
        if (propertyConfig.isOrderAllowToShiftTime()) {
            builder.addItem("Задержать заказ", new ShiftOrderAction(orderService));
        }
                builder.addItem("Список заказов", new OrdersSortAction(orderService))
                .addItem("Список текущих выполняемых заказов", new ActiveOrdersSortAction(orderService))
                .addItem("Получить заказ, выполняемый конкретным мастером", new GetOrderByMasterAction(orderService, masterService))
                .addItem("Заказы за промежуток времени", new OrdersSortByTimeFrameAction(orderService))
                .addItem("Назад в главное меню", new BackToMainMenuAction(navigator));
        return builder.build();
    }

    @Override
    public Menu createMasterMenu() {
        return new MenuBuilder()
                .setName("Меню мастеров")
                .addItem("Добавить мастера", new AddMasterAction(masterService))
                .addItem("Удалить мастера", new DeleteMasterAction(masterService))
                .addItem("Получить мастера, выполняющий конкретный заказ", new GetMasterByOrderAction(masterService, orderService))
                .addItem("Список авто-мастеров", new MastersSortAction(masterService))
                .addItem("Назад в главное меню", new BackToMainMenuAction(navigator))
                .build();
    }

    @Override
    public Menu createGarageSpotMenu() {
        MenuBuilder builder = new MenuBuilder()
                .setName("Меню гаражных мест");
        if (propertyConfig.isGarageSpotAllowToAddRemove()) {

        builder.addItem("Добавить гаражное место", new AddGarageSpotAction(garageSpotService))
                .addItem("Удалить гаражное место", new DeleteGarageSpotAction(garageSpotService));
        }
        builder.addItem("Список свободных мест в сервисных гаражах", new GetFreeSpotsAction(garageSpotService))
                .addItem("Количество свободных мест на сервисе на любую дату в будущем", new GetNumberOfFreeSpotsByDateAction(garageSpotService, generalService))
                .addItem("Назад в главное меню", new BackToMainMenuAction(navigator));
        return builder.build();
    }

    @Override
    public Menu createGeneralMenu() {
        return new MenuBuilder()
                .setName("Прочие действия")
                .addItem("Найти ближайшую свободную дату", new GetClosestDateAction(generalService, masterService, garageSpotService))
                .addSubMenu("Экспорт данных", createExportMenu())
                .addSubMenu("Импорт данных", createImportMenu())
                .addItem("Назад в главное меню", new BackToMainMenuAction(navigator))
                .build();
    }

    @Override
    public Menu createExportMenu() {
        return new MenuBuilder()
                .setName("Эспорт данных")
                .addItem("Экспортировать данные о заказах", new CsvExportOrdersAction(ordersCsvExportService))
                .addItem("Экспортировать данные о мастерах", new CsvExportMasterAction(mastersCsvExportService))
                .addItem("Экспортировать данные о гаражных местах", new CsvExportGarageSpotsAction(garageSpotsCsvExportService))
                .addItem("Назад в главное меню", new BackToMainMenuAction(navigator))
                .build();
    }

    @Override
    public Menu createImportMenu() {
        return new MenuBuilder()
                .setName("Импорт данных")
                .setHint("Обратите внимание, данные в системе являются приоритетными!")
                .addItem("Импортировать данные о заказах", new CsvImportOrdersAction(csvImportService))
                .addItem("Импортировать данные о мастерах", new CsvImportMastersAction(csvImportService))
                .addItem("Импортировать данные о гаражных местах", new CsvImportGarageSpotsAction(csvImportService))
                .addItem("Назад в главное меню", new BackToMainMenuAction(navigator))
                .build();
    }
}