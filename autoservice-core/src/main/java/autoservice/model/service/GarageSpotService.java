package autoservice.model.service;

import autoservice.model.entities.GarageSpot;
import autoservice.model.exceptions.GarageSpotException;
import autoservice.model.repository.GarageSpotRepository;
import autoservice.model.repository.OrderRepository;
import autoservice.model.service.domain.GarageSpotDomainService;
import autoservice.model.utils.HibernateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
public class GarageSpotService {

    private transient final GarageSpotRepository garageSpotRepository;
    private transient final OrderRepository orderRepository;


    //метод для импорта
    public long addGarageSpotFromImport(double size, boolean hasLift, boolean hasPit) {
        if (size < 8) {
            return -1;
        }
        GarageSpot garageSpot = new GarageSpot(size, hasLift, hasPit);
        garageSpotRepository.save(garageSpot);
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
            garageSpotRepository.save(garageSpot);
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
            garageSpotRepository.delete(id);
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
            garageSpotRepository.update(garageSpot);
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
        List<GarageSpot> spots = garageSpotRepository.findAll();

        List<Object[]> slots = orderRepository.findTimeSlotsForAllGarageSpots();

        return GarageSpotDomainService.getGarageSpotsWithCalendar(spots, slots);
    }

    public Long getGarageSpotsCount() {
        return garageSpotRepository.count();
    }

    //4
    public GarageSpot getGarageSpotById(long id) {
        GarageSpot spot = garageSpotRepository.findById(id);
        if (spot != null) {
            spot.setCalendar(
                    orderRepository.findTimeSlotsByGarageSpot(id)
            );
        }
        return spot;
    }

    //4 список свободных мест в сервисных гаражах
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
