package autoservice.model.service;

import autoservice.model.entities.GarageSpot;
import autoservice.model.exceptions.DBException;
import autoservice.model.repository.GarageSpotDAO;
import autoservice.model.repository.MasterDAO;
import autoservice.model.repository.OrderDAO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import config.annotation.Component;
import lombok.extern.slf4j.Slf4j;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Component
@Slf4j
public class GarageSpotService {

    private final GarageSpotDAO garageSpotsDAO = new GarageSpotDAO();
    private final OrderDAO orderDAO = new OrderDAO();


    public GarageSpotService() {
    }

    public long addGarageSpot(double size, boolean hasLift, boolean hasPit) {
        if (size < 8) {
            return -1;
        }
        GarageSpot garageSpot = new GarageSpot(size, hasLift, hasPit);
        garageSpotsDAO.save(garageSpot);
        return garageSpot.getId();
    }

    public void deleteGarageSpot(long id) {
        garageSpotsDAO.delete(id);
    }

    public void update(GarageSpot garageSpot) {
        garageSpotsDAO.update(garageSpot);
    }

    public List<GarageSpot> getGarageSpots() {
        List<GarageSpot> spots;
        spots = garageSpotsDAO.findAll();
        for (var garageSpot : spots) {
            garageSpot.setCalendar(
                    orderDAO.findTimeSlotsByGarageSpot(garageSpot.getId())
            );
        }

        return spots;
    }

    //4
    public GarageSpot getGarageSpotById(long id) {
        GarageSpot spot = garageSpotsDAO.findById(id);
        if (spot != null) {
            spot.setCalendar(
                    orderDAO.findTimeSlotsByGarageSpot(id)
            );
        }
        return spot;
    }

    public void deleteAll() {
        garageSpotsDAO.deleteAll();
    }

    //4 список свободных мест в сервисных гаражах
    @JsonIgnore
    public List<GarageSpot> getFreeSpots() {
        log.info("Fetching free garage spots");
        List<GarageSpot> freeGarageSpots = new ArrayList<>();
        List<GarageSpot> garageSpots = getGarageSpots();
        for (var v : garageSpots) {
            if (v.isAvailable(LocalDateTime.now(), LocalDateTime.now().plusMinutes(1))) {
                freeGarageSpots.add(v);
            }
        }
        log.info("Free garage spots successfully found");
        return freeGarageSpots;
    }
}
