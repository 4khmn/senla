package main.java.autoservice.ui.actions.general;

import main.java.autoservice.ui.actions.IAction;
import main.java.autoservice.ui.menu.Navigator;

public class BackToMainMenuAction implements IAction {
    @Override
    public void execute() {
        Navigator.getInstance().goToMainMenu();
    }
}