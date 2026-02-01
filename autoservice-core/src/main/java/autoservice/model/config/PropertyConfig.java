package autoservice.model.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertyConfig {

    @Value("${garage.allowToAddRemove:false}")
    private boolean allowGarageSpotAddRemove;

    @Value("${orders.allowToShiftTime:false}")
    private boolean allowOrderShiftTime;

    @Value("${orders.allowToDelete:false}")
    private boolean allowOrderDelete;

    public boolean isGarageSpotAllowToAddRemove() {
        return allowGarageSpotAddRemove;
    }
    public boolean isOrderAllowToShiftTime() {
        return allowOrderShiftTime;
    }
    public boolean isOrderAllowToDelete() {
        return allowOrderDelete;
    }
}
