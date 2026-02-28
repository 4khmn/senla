package autoservice.model.service.io.serialization;

import autoservice.model.entities.GarageSpot;
import autoservice.model.entities.Master;
import autoservice.model.entities.Order;
import autoservice.model.service.GarageSpotService;
import autoservice.model.service.MasterService;
import autoservice.model.service.OrderService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@JsonAutoDetect
@RequiredArgsConstructor
@Component
public class SerializationDTO {
    private List<GarageSpot> garageSpots;
    private List<Order> orders;
    private List<Master> masters;


    //for serialization
    @Autowired
    public void setGarageSpots(GarageSpotService garageSpotService) {
        this.garageSpots = garageSpotService.getGarageSpots();
    }
    @Autowired
    public void setOrders(OrderService orderService) {
        this.orders = orderService.getOrders();
    }
    @Autowired
    public void setMasters(MasterService masterService) {
        this.masters = masterService.getMasters();
    }

    public List<GarageSpot> getGarageSpots() {
        return garageSpots;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<Master> getMasters() {
        return masters;
    }
}
