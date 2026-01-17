package autoservice;
import autoservice.model.AutoService;
import autoservice.model.io.exports.GarageSpotsCsvExport;
import autoservice.model.io.exports.MastersCsvExport;
import autoservice.model.io.exports.OrdersCsvExport;
import autoservice.model.io.imports.CsvImportService;
import autoservice.model.io.serialization.SerializationService;
import autoservice.model.service.GarageSpotService;
import autoservice.model.service.MasterService;
import autoservice.model.service.OrderService;
import autoservice.ui.controller.MenuController;
import autoservice.ui.factory.ConsoleMenuFactory;
import autoservice.ui.menu.Navigator;
import config.AppConfig;
import config.DIContainer;

public class AutoServiceRunner {
    public static void main(String[] args) {
        DIContainer context = new DIContainer(
                GarageSpotService.class,
                MasterService.class,
                OrderService.class,
                CsvImportService.class,
                SerializationService.class,
                GarageSpotsCsvExport.class,
                MastersCsvExport.class,
                OrdersCsvExport.class,
                AutoService.class,
                Navigator.class,
                AppConfig.class,
                ConsoleMenuFactory.class,
                MenuController.class);
        MenuController controller = context.getBean(MenuController.class);
        controller.run();
    }
}
