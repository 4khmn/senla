package autoservice.ui.menu;

import autoservice.ui.actions.IAction;
import java.util.ArrayList;
import java.util.List;

public class MenuBuilder {
    private String name;
    private final List<MenuItem> items = new ArrayList<>();

    public MenuBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public MenuBuilder addItem(String title, IAction action) {
        items.add(new MenuItem(title, action));
        return this;
    }

    public MenuBuilder addSubMenu(String title, Menu nextMenu) {
        items.add(new MenuItem(title, nextMenu));
        return this;
    }

    public Menu build() {
        return new Menu(name, items.toArray(new MenuItem[0]));
    }
}