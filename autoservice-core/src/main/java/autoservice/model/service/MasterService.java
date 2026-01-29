package autoservice.model.service;

import autoservice.model.entities.Master;
import autoservice.model.entities.Order;
import autoservice.model.entities.TimeSlot;
import autoservice.model.enums.MastersSortEnum;
import autoservice.model.exceptions.MasterException;
import autoservice.model.repository.MasterDAO;
import autoservice.model.repository.OrderDAO;
import autoservice.model.utils.HibernateUtil;
import config.annotation.Component;
import config.annotation.Inject;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import static java.lang.Math.abs;
@Component
@Slf4j
public class MasterService {
    private transient final MasterDAO masterDAO;
    private transient final OrderDAO orderDAO;

    @Inject
    public MasterService(MasterDAO masterDAO, OrderDAO orderDAO) {
        this.masterDAO = masterDAO;
        this.orderDAO = orderDAO;
    }

    //4
    public List<Master> mastersSort(MastersSortEnum decision) {
        log.info("Sorting masters by decision={}", decision);
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            List<Master> sortedMasters;
            switch (decision) {
                case BY_NAME:
                    //по алфавиту
                    sortedMasters = masterDAO.mastersSortByName();
                    transaction.commit();
                    break;
                case BY_EMPLOYMENT:
                    //по занятости
                    sortedMasters = getMasters().stream()
                            .sorted((master1, master2) -> {
                                int freeMaster1HoursToday = getFreeMasterHoursToday(master1);
                                int freeMaster2HoursToday = getFreeMasterHoursToday(master2);


                                // если оба свободны, сравниваем их по имени
                                if (freeMaster1HoursToday == freeMaster2HoursToday) {
                                    return master1.getName().compareTo(master2.getName());
                                }
                                //сначала более свободные
                                if (freeMaster1HoursToday > freeMaster2HoursToday) {
                                    return -1;
                                }
                                if (freeMaster1HoursToday < freeMaster2HoursToday) {
                                    return 1;
                                }
                                return 0;
                            })
                            .toList();
                    transaction.commit();
                    break;
                default:
                    //error
                    log.error("Invalid decision={}", decision);
                    throw new IllegalArgumentException("IInvalid decision: " + decision);
            }
            log.info("Masters successfully sorted by decision={}", decision);
            return sortedMasters;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Error sorting masters by decision={}", decision, e);
            throw new MasterException("Impossible to sort masters by decision=" + decision, e);
        }
    }

    //4
    public Master getMasterByOrder(Order order) {
        log.info("Getting master by order with id{}", order.getId());
        Master master = order.getMaster();
        log.info("Master successfully found by order with master_id={}", master.getId());
        return master;
    }

    public List<Master> getMasters() {
        List<Master> masters = masterDAO.findAll();
        List<Object[]> slots = orderDAO.findTimeSlotsForAllMasters();

        Map<Long, TreeSet<TimeSlot>> calendarMap = new HashMap<>();
        for (Object[] row : slots) {
            Long masterId = (Long) row[0];
            LocalDateTime start = (LocalDateTime) row[1];
            LocalDateTime end = (LocalDateTime) row[2];

            calendarMap.computeIfAbsent(masterId, k -> new TreeSet<>())
                    .add(new TimeSlot(start, end));
        }
        for (Master m : masters) {
            m.setCalendar(calendarMap.getOrDefault(m.getId(), new TreeSet<>()));
        }
        return masters;
    }

    public long addMasterFromImport(String name, BigDecimal salary) {
        Master master = new Master(name, salary);
        masterDAO.save(master);
        return master.getId();
    }

    public long addMaster(String name, BigDecimal salary) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Master master = new Master(name, salary);
            masterDAO.save(master);
            transaction.commit();
            return master.getId();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Error adding master with name={}, salary={}", name, salary, e);
            throw new MasterException("impossible to add master", e);
        }
    }

    public void deleteMaster(long id) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            masterDAO.delete(id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Error deleting master with id={}", id, e);
            throw new MasterException("Impossible to delete master with id=" + id, e);
        }
    }


    public void update(Master master) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            masterDAO.update(master);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Error updating master with id={}", master.getId(), e);
            throw new MasterException("Impossible to update master with id=" + master.getId(), e);
        }
    }

    public Master getMasterById(long id) {
        Master master = masterDAO.findById(id);
        if (master != null) {
            master.setCalendar(
                    orderDAO.findTimeSlotsByMaster(id)
            );
        }
        return master;
    }

    private int getFreeMasterHoursToday(Master master) {
        int freeMasterHoursToday = 24;
        int today = LocalDateTime.now().getDayOfMonth();
        for (var v : master.getCalendar()) {
            if (v.getStart().getDayOfMonth() == today && v.getEnd().getDayOfMonth() == today) {
                long duration = abs(Duration.between(v.getStart(), v.getEnd()).toHours());
                freeMasterHoursToday -= duration;
            } else if (v.getStart().getDayOfMonth() == today) {
                long duration = abs(Duration.between(v.getStart(),
                        LocalDateTime.of(v.getStart().getYear(),
                                v.getStart().getMonth(),
                                v.getStart().getDayOfMonth() + 1,
                                0,
                                0)).toHours());
                freeMasterHoursToday -= duration;
            } else if (v.getEnd().getDayOfMonth() == today) {
                long duration = abs(Duration.between(LocalDateTime.of(v.getEnd().getYear(),
                        v.getEnd().getMonth(),
                        v.getEnd().getDayOfMonth(),
                        0,
                        0), v.getEnd()).toHours());
                freeMasterHoursToday -= duration;
            }
        }
        return freeMasterHoursToday;
    }

    public Long getMastersCount() {
        return masterDAO.count();
    }
}