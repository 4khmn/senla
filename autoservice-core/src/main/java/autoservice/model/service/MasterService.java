package autoservice.model.service;

import autoservice.model.entities.Master;
import autoservice.model.entities.Order;
import autoservice.model.enums.MastersSortEnum;
import autoservice.model.repository.MasterDAO;
import autoservice.model.repository.OrderDAO;
import config.annotation.Component;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static java.lang.Math.abs;
@Component
@Slf4j
public class MasterService {
    private final MasterDAO masterDAO = new MasterDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    public MasterService() {
    }

    //4
    public List<Master> mastersSort(MastersSortEnum decision) {
        log.info("Sorting masters by decision={}", decision);
        List<Master> sortedMasters;
        switch (decision) {
            case BY_NAME:
                //по алфавиту
                sortedMasters = masterDAO.mastersSortByName();
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
                break;
            default:
                //error
                log.error("Invalid decision={}", decision);
                throw new IllegalArgumentException("Неизвестный тип: " + decision);
        }
        log.info("Masters successfully sorted by decision={}", decision);
        return sortedMasters;
    }
    //4
    public Master getMasterByOrder(Order order) {
        log.info("Getting master by order ith id{}", order.getId());
        Master master = order.getMaster();
        log.info("Master successfully found by order with master_id={}", master.getId());
        return master;
    }

    public List<Master> getMasters() {
        List<Master> masters = masterDAO.findAll();
        for (var master: masters) {
            master.setCalendar(
                    orderDAO.findTimeSlotsByMaster(master.getId())
            );
        }
        return masters;
    }

    public long addMaster(String name, BigDecimal salary) {
        Master master = new Master(name, salary);
        masterDAO.save(master);
        return master.getId();
    }

    public void deleteMaster(long id) {
        masterDAO.delete(id);
    }

    public void deleteAll() {
        masterDAO.deleteAll();
    }

    public void update(Master master) {
        masterDAO.update(master);
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
        for (var v: master.getCalendar()) {
            if (v.getStart().getDayOfMonth() == today && v.getEnd().getDayOfMonth() == today) {
                long duration = abs(Duration.between(v.getStart(), v.getEnd()).toHours());
                freeMasterHoursToday -= duration;
            } else if (v.getStart().getDayOfMonth() == today) {
                long duration = abs(Duration.between(v.getStart(),
                        LocalDateTime.of( v.getStart().getYear(),
                                v.getStart().getMonth(),
                                v.getStart().getDayOfMonth() + 1,
                                0,
                                0)).toHours());
                freeMasterHoursToday -= duration;
            } else if (v.getEnd().getDayOfMonth() == today) {
                long duration = abs(Duration.between(LocalDateTime.of( v.getEnd().getYear(),
                                v.getEnd().getMonth(),
                                v.getEnd().getDayOfMonth(),
                                0,
                                0), v.getEnd()).toHours());
                freeMasterHoursToday -= duration;
            }
        }
        return freeMasterHoursToday;
    }
}