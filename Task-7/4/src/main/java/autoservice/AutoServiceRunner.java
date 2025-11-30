package autoservice;
import autoservice.model.AutoService;
import autoservice.model.io.imports.CsvImportService;
import autoservice.model.io.serialization.SerializationService;
import autoservice.model.manager.GarageSpotManager;
import autoservice.model.manager.MasterManager;
import autoservice.model.manager.OrderManager;
import autoservice.ui.controller.MenuController;

import java.io.IOException;

public class AutoServiceRunner {
    public static void main(String[] args){
        final SerializationService serializer = SerializationService.getInstance();
        try {
            AutoService loaded = serializer.loadStateFromFile("autoservice.json");
            AutoService.replaceInstance(loaded);

            ///чтобы после десириализации объекты ссылались на одно и то же
            GarageSpotManager g = loaded.getGarageManager();
            MasterManager m = loaded.getMasterManager();
            OrderManager o = loaded.getOrderManager();
            CsvImportService instance = CsvImportService.getInstance(o, g, m);
            instance.setManagers(o, g, m);


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        new MenuController().run();
    }
}
