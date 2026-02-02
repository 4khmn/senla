package autoservice.model.service;

import autoservice.model.entities.Master;
import autoservice.model.entities.Order;
import autoservice.model.enums.MastersSortEnum;
import autoservice.model.exceptions.MasterException;
import autoservice.model.repository.MasterRepository;
import autoservice.model.repository.OrderRepository;
import autoservice.model.service.domain.MasterDomainService;
import autoservice.model.utils.HibernateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static java.lang.Math.abs;
@Service
@Slf4j
@RequiredArgsConstructor
public class MasterService {
    private transient final MasterRepository masterRepository;
    private transient final OrderRepository orderRepository;


    //4 список авто-мастеров
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
                    sortedMasters = masterRepository.mastersSortByName();
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

    //4 мастер, выполняющий конкретный заказ
    public Master getMasterByOrder(Order order) {
        log.info("Getting master by order with id{}", order.getId());
        Master master = order.getMaster();
        log.info("Master successfully found by order with master_id={}", master.getId());
        return master;
    }

    public List<Master> getMasters() {
        List<Master> masters = masterRepository.findAll();
        List<Object[]> slots = orderRepository.findTimeSlotsForAllMasters();

        return MasterDomainService.getMastersWithCalendar(masters, slots);
    }

    public long addMasterFromImport(String name, BigDecimal salary) {
        Master master = new Master(name, salary);
        masterRepository.save(master);
        return master.getId();
    }

    public long addMaster(String name, BigDecimal salary) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Master master = new Master(name, salary);
            masterRepository.save(master);
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
            masterRepository.delete(id);
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
            masterRepository.update(master);
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
        Master master = masterRepository.findById(id);
        if (master != null) {
            master.setCalendar(
                    orderRepository.findTimeSlotsByMaster(id)
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
        return masterRepository.count();
    }
}