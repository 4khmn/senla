package autoservice.ui.actions.general;

import autoservice.ui.actions.IAction;
import autoservice.ui.menu.Navigator;

public class BackToMainMenuAction implements IAction {
    @Override
    public void execute() {
        Navigator.getInstance().goToMainMenu();
    }
}