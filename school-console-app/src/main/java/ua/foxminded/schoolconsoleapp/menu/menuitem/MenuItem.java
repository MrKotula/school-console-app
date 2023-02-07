package ua.foxminded.schoolconsoleapp.menu.menuitem;

public abstract class MenuItem {
    private String name;

    protected MenuItem(String name) {
	this.name = name;
    }

    public abstract void execute();

    public String getName() {
	return this.name;
    }
}
