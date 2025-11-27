package main.java.autoservice.ui.factory;

import main.java.autoservice.ui.menu.Menu;

public interface IMenuFactory {
    Menu createMainMenu();
    Menu createOrderMenu();
    Menu createMasterMenu();
    Menu createGarageSpotMenu();
    Menu createGeneralMenu();
    Menu createExportMenu();
    Menu createImportMenu();
}