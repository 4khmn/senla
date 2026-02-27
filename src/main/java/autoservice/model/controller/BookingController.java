package autoservice.model.controller;

import autoservice.model.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class BookingController {

    private final ScheduleService scheduleService;

    @GetMapping("/free-spots")
    public ResponseEntity<Long> getFreeSpotsCount(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        log.info("GET /api/schedule/free-spots - fetching free spots count by date {}", date);
        Long count = scheduleService.getNumberOfFreeSpotsByDate(date);
        log.info("Number of free spots by date {}", count);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/closest-date")
    public ResponseEntity<LocalDateTime> getClosestAvailableDate(
            @RequestParam(name = "duration") int durationInHours) {
        log.info("GET /api/schedule/closest-date - fetching closest date by duration {}", durationInHours);
        LocalDateTime closestDate = scheduleService.getClosestDate(durationInHours);
        log.info("Closest date with duration={} successfully found", durationInHours);
        return ResponseEntity.ok(closestDate);
    }
}
