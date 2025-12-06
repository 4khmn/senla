package autoservice.ui.actions.general;

import autoservice.ui.actions.IAction;
import autoservice.ui.menu.Navigator;

public class BackToMainMenuAction implements IAction {

    private final Navigator navigator;
    public BackToMainMenuAction(Navigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public void execute() {
        navigator.goToMainMenu();
    }
}