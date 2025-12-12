package autoservice;
import autoservice.model.AutoService;
import autoservice.model.io.exports.GarageSpotsCsvExport;
import autoservice.model.io.exports.MastersCsvExport;
import autoservice.model.io.exports.OrdersCsvExport;
import autoservice.model.io.imports.CsvImportService;
import autoservice.model.io.serialization.SerializationService;
import autoservice.model.manager.GarageSpotManager;
import autoservice.model.manager.MasterManager;
import autoservice.model.manager.OrderManager;
import autoservice.ui.controller.MenuController;
import autoservice.ui.factory.ConsoleMenuFactory;
import autoservice.ui.menu.Navigator;
import config.AppConfig;
import config.DIContainer;

import java.io.IOException;

public class AutoServiceRunner {
    public static void main(String[] args) {
        DIContainer context = new DIContainer(
                GarageSpotManager.class,
                MasterManager.class,
                OrderManager.class,
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
        SerializationService serializer = context.getBean(SerializationService.class);

        AutoService loaded = context.getBean(AutoService.class);

        try {
            serializer.loadStateFromFile(loaded, "autoservice.json");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        ///чтобы после десириализации объекты ссылались на одно и то же
        GarageSpotManager g = loaded.getGarageManager();
        MasterManager m = loaded.getMasterManager();
        OrderManager o = loaded.getOrderManager();
        CsvImportService instance = context.getBean(CsvImportService.class);
        instance.setManagers(o, g, m);

        MenuController controller = context.getBean(MenuController.class);
        controller.run();
    }
}
