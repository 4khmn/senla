package autoservice.model.controller;

import autoservice.model.service.io.exports.GarageSpotsCsvExportService;
import autoservice.model.service.io.exports.MastersCsvExportService;
import autoservice.model.service.io.exports.OrdersCsvExportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
public class DataExportController {

    private final MastersCsvExportService mastersExportService;
    private final GarageSpotsCsvExportService garageSpotsExportService;
    private final OrdersCsvExportService ordersExportService;

    @GetMapping("/masters")
    public ResponseEntity<Void> exportMasters() {
        log.info("GET /api/export/masters - initiating masters export to CSV");
        mastersExportService.export();
        log.info("masters export completed");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/garage-spots")
    public ResponseEntity<Void> exportGarageSpots() {
        log.info("GET /api/export/garage-spots - initiating garage spots export to CSV");
        garageSpotsExportService.export();
        log.info("garage spots export completed");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/orders")
    public ResponseEntity<Void> exportOrders() {
        log.info("GET /api/export/orders - initiating orders export to CSV");
        ordersExportService.export();
        log.info("orders export completed");
        return ResponseEntity.ok().build();
    }
}