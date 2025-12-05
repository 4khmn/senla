package config;

import config.annotation.ConfigProperty;

public class AppConfig {
    public AppConfig() {
        ConfigLoader.load(this);
    }

    @ConfigProperty(propertyName = "garage.allowToAddRemove")
    private boolean allowGarageSpotAddRemove;
    @ConfigProperty(propertyName = "orders.allowToShiftTime")
    private boolean allowOrderShiftTime;
    @ConfigProperty(propertyName = "orders.allowToDelete")
    private boolean allowOrderDelete;



    public boolean isGarageSpotAllowToAddRemove() { return allowGarageSpotAddRemove; }
    public boolean isOrderAllowToShiftTime() { return allowOrderShiftTime; }
    public boolean isOrderAllowToDelete() { return allowOrderDelete; }
}
