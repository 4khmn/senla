package autoservice.model.service;

import autoservice.model.entities.GarageSpot;
import autoservice.model.entities.Master;
import autoservice.model.repository.GarageSpotRepository;
import autoservice.model.repository.MasterRepository;
import autoservice.model.repository.OrderRepository;
import autoservice.model.service.domain.GarageSpotDomainService;
import autoservice.model.service.domain.MasterDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

import static java.lang.Math.min;
@Service
@RequiredArgsConstructor
@Slf4j
public class GeneralService {

    private final GarageSpotRepository garageSpotRepository;
    private final MasterRepository masterRepository;
    private final OrderRepository orderRepository;



    //4 количество свободных мест на сервисе на любую дату в будующем
    public int getNumberOfFreeSpotsByDate(LocalDateTime date) {
        log.info("Fetching free spots count by date {}", date);
        int spotsCount = 0;
        int mastersCount = 0;
        for (var v: this.getGarageSpotsWithCalendar()) {
            if (v.isAvailable(date, date.plusMinutes(1))) {
                spotsCount += 1;
            }
        }
        for (var v: this.getMastersWithCalendar()) {
            if (v.isAvailable(date, date.plusMinutes(1))) {
                mastersCount += 1;
            }
        }
        log.info("Number of free spots by date {}", min(spotsCount, mastersCount));
        return min(mastersCount, spotsCount);
    }



    //4 ближайшая свободная дата
    public LocalDateTime getClosestDate(int durationInHours) {
        log.info("Fetching closest date by duration {}", durationInHours);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime bestStartTime = null;
        Master selectedMaster = null;
        GarageSpot selectedSpot = null;

        for (GarageSpot spot : this.getGarageSpotsWithCalendar()) {
            LocalDateTime candidateStart = spot.findNextAvailableSlotInGarageSpotSchedule(now, durationInHours);

            for (Master master : this.getMastersWithCalendar()) {
                if (master.isAvailable(candidateStart, candidateStart.plusHours(durationInHours))) {
                    if (bestStartTime == null || candidateStart.isBefore(bestStartTime)) {
                        bestStartTime = candidateStart;
                        selectedMaster = master;
                        selectedSpot = spot;
                    }
                }
            }
        }
        if (bestStartTime == null) {
            log.error("Error while fetching closest date by duration {}", durationInHours);
            throw new RuntimeException("No available time slot found");
        }
        LocalDateTime endTime = bestStartTime.plusHours(durationInHours);
        selectedSpot.addBusyTime(bestStartTime, endTime);
        selectedMaster.addBusyTime(bestStartTime, endTime);
        log.info("Closest date with duration={} successfully found", durationInHours);
        return bestStartTime;
    }

    private List<GarageSpot> getGarageSpotsWithCalendar() {
        List<GarageSpot> spots = garageSpotRepository.findAll();
        List<Object[]> slots = orderRepository.findTimeSlotsForAllGarageSpots();

        return GarageSpotDomainService.getGarageSpotsWithCalendar(spots, slots);
    }

    private List<Master> getMastersWithCalendar() {
        List<Master> masters = masterRepository.findAll();
        List<Object[]> slots = orderRepository.findTimeSlotsForAllMasters();

        return MasterDomainService.getMastersWithCalendar(masters, slots);
    }

}
