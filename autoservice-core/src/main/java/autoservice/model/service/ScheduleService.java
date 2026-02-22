package autoservice.model.service;

import autoservice.model.entities.GarageSpot;
import autoservice.model.entities.Master;
import autoservice.model.repository.GarageSpotRepository;
import autoservice.model.repository.MasterRepository;
import autoservice.model.repository.OrderRepository;
import autoservice.model.service.domain.GarageSpotDomainService;
import autoservice.model.service.domain.MasterDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.Math.min;
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final GarageSpotRepository garageSpotRepository;
    private final MasterRepository masterRepository;
    private final OrderRepository orderRepository;


    //4 количество свободных мест на сервисе на любую дату в будующем
    @Transactional(readOnly = true)
    public Long getNumberOfFreeSpotsByDate(LocalDateTime date) {
        Long spotsCount = 0L;
        Long mastersCount = 0L;
        for (var v : this.getGarageSpotsWithCalendar()) {
            if (v.isAvailable(date, date.plusMinutes(1))) {
                spotsCount += 1;
            }
        }
        for (var v : this.getMastersWithCalendar()) {
            if (v.isAvailable(date, date.plusMinutes(1))) {
                mastersCount += 1;
            }
        }
        return min(mastersCount, spotsCount);
    }


    //4 ближайшая свободная дата
    @Transactional(readOnly = true)
    public LocalDateTime getClosestDate(int durationInHours) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime bestStartTime = null;

        for (GarageSpot spot : this.getGarageSpotsWithCalendar()) {
            LocalDateTime candidateStart = spot.findNextAvailableSlotInGarageSpotSchedule(now, durationInHours);

            for (Master master : this.getMastersWithCalendar()) {
                if (master.isAvailable(candidateStart, candidateStart.plusHours(durationInHours))) {
                    if (bestStartTime == null || candidateStart.isBefore(bestStartTime)) {
                        bestStartTime = candidateStart;
                    }
                }
            }
        }
        if (bestStartTime == null) {
            throw new RuntimeException("No available time slot found");
        }
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
