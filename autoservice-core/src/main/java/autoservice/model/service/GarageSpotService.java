package autoservice.model.service;

import autoservice.model.entities.GarageSpot;
import autoservice.model.entities.TimeSlot;
import autoservice.model.exceptions.GarageSpotException;
import autoservice.model.repository.GarageSpotDAO;
import autoservice.model.repository.OrderDAO;
import autoservice.model.utils.HibernateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import config.annotation.Component;
import config.annotation.Inject;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;


import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.HashMap;

@Component
@Slf4j
public class GarageSpotService {

    private transient final GarageSpotDAO garageSpotsDAO;
    private transient final OrderDAO orderDAO;

    @Inject
    public GarageSpotService(GarageSpotDAO garageSpotsDAO, OrderDAO orderDAO) {
        this.garageSpotsDAO = garageSpotsDAO;
        this.orderDAO = orderDAO;
    }

    //метод для импорта
    public long addGarageSpotFromImport(double size, boolean hasLift, boolean hasPit) {
        if (size < 8) {
            return -1;
        }
        GarageSpot garageSpot = new GarageSpot(size, hasLift, hasPit);
        garageSpotsDAO.save(garageSpot);
        return garageSpot.getId();
    }

    public long addGarageSpot(double size, boolean hasLift, boolean hasPit) {
        if (size < 8) {
            return -1;
        }
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            GarageSpot garageSpot = new GarageSpot(size, hasLift, hasPit);
            garageSpotsDAO.save(garageSpot);
            transaction.commit();
            return garageSpot.getId();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Error adding new garageSpot", e);
            throw new GarageSpotException("Impossible to add new garageSpot", e);
        }
    }

    public void deleteGarageSpot(long id) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            garageSpotsDAO.delete(id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Error deleting garageSpot with id={}", id, e);
            throw new GarageSpotException("Impossible to delete garageSpot with id=" + id, e);
        }
    }

    public void update(GarageSpot garageSpot) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            garageSpotsDAO.update(garageSpot);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Error updating garageSpot with id={}", garageSpot.getId(), e);
            throw new GarageSpotException("Impossible to update garage spot with id=" + garageSpot.getId(), e);
        }
    }
    public List<GarageSpot> getGarageSpots() {
        List<GarageSpot> spots = garageSpotsDAO.findAll();

        List<Object[]> slots = orderDAO.findTimeSlotsForAllGarageSpots();

        Map<Long, TreeSet<TimeSlot>> calendarMap = new HashMap<>();
        for (Object[] row : slots) {
            Long garageSpotId = (Long) row[0];
            LocalDateTime start = (LocalDateTime) row[1];
            LocalDateTime end = (LocalDateTime) row[2];

            calendarMap.computeIfAbsent(garageSpotId, k -> new TreeSet<>())
                    .add(new TimeSlot(start, end));
        }
        for (GarageSpot gs : spots) {
            gs.setCalendar(calendarMap.getOrDefault(gs.getId(), new TreeSet<>()));
        }
        return spots;
    }

    public Long getGarageSpotsCount() {
        return garageSpotsDAO.count();
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

    //4 список свободных мест в сервисных гаражах
    @JsonIgnore
    public List<GarageSpot> getFreeSpots() {
        log.info("Fetching free garage spots");
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            List<GarageSpot> freeGarageSpots = new ArrayList<>();
            List<GarageSpot> garageSpots = getGarageSpots();
            for (var v : garageSpots) {
                if (v.isAvailable(LocalDateTime.now(), LocalDateTime.now().plusMinutes(1))) {
                    freeGarageSpots.add(v);
                }
            }
            transaction.commit();
            log.info("Free garage spots successfully found");
            return freeGarageSpots;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Error getting free garage spots", e);
            throw new GarageSpotException("Impossible to get free spots", e);
        }
    }
}
