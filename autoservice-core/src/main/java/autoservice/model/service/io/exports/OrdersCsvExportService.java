package autoservice.model.service.io.exports;

import autoservice.model.entities.Order;
import autoservice.model.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrdersCsvExportService extends CsvExport {


    private final OrderService orderService;
    public OrdersCsvExportService(OrderService orderService) {
        super("id,description,masterId,garageSpotId,startTime,endTime,orderStatus,price", "orders.csv");
        this.orderService = orderService;
    }

    @Override
    protected String formatEntity(Object entity) {
        Order order = (Order)entity;
        return String.join(",",
                String.valueOf(order.getId()),
                order.getDescription(),
                String.valueOf(order.getMaster().getId()),
                String.valueOf(order.getGarageSpot().getId()),
                String.valueOf(order.getStartTime()),
                String.valueOf(order.getEndTime()),
                String.valueOf(order.getOrderStatus()),
                String.valueOf(order.getPrice())
        );
    }

    @Override
    protected Iterable<?> getEntities() {
        return orderService.getOrders();
    }
}
