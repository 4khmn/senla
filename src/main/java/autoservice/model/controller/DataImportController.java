package autoservice.model.controller;


import autoservice.model.service.io.imports.CsvImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
@Slf4j
public class DataImportController {

    private final CsvImportService csvImportService;

    @PostMapping("/masters")
    public ResponseEntity<Void> importMasters() {
        log.info("POST /api/import/masters - initiating masters import from CSV");
        csvImportService.importMasters();
        log.info("masters successfully imported");
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/garage-spots")
    public ResponseEntity<Void> importGarageSpots() {
        log.info("POST /api/import/garage-spots - initiating garage spots import from CSV");
        csvImportService.importGarageSpots();
        log.info("garage spots successfully imported");
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/orders")
    public ResponseEntity<Void> importOrders() {
        log.info("POST /api/import/orders - initiating orders import from CSV");
        csvImportService.importOrders();
        log.info("orders successfully imported");
        return ResponseEntity.noContent().build();
    }
}