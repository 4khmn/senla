package autoservice.model.controller;

import autoservice.model.dto.create.GarageSpotCreateDto;
import autoservice.model.dto.response.GarageSpotResponseDto;
import autoservice.model.entities.GarageSpot;
import autoservice.model.mapper.GarageSpotMapper;
import autoservice.model.service.GarageSpotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/garage-spots")
public class GarageSpotController {

    private final GarageSpotService garageSpotService;
    private final GarageSpotMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<GarageSpotResponseDto> getGarageSpotById(@PathVariable() long id) {
        log.info("GET /api/garage-spots/{} - fetching garageSpot by id={}", id, id);
        GarageSpotResponseDto garageSpotById = garageSpotService.getGarageSpotById(id);
        log.info("garageSpot with id={} was successfully fetched", id);
        return ResponseEntity.ok(garageSpotById);
    }

    @GetMapping
    public ResponseEntity<List<GarageSpotResponseDto>> getGarageSpots() {
        log.info("GET /api/garage-spots - fetching all garageSpots");
        List<GarageSpot> garageSpots = garageSpotService.getGarageSpots();
        log.info("garageSpots was successfully fetched");
        return ResponseEntity.ok(garageSpots.stream().map(mapper::toDto).toList());
    }

    @PostMapping
    public ResponseEntity<GarageSpotResponseDto> createGarageSpot(@RequestBody GarageSpotCreateDto dto) {
        log.info("POST /api/garage-spots - creating garageSpot={}", dto);
        GarageSpotResponseDto garageSpot = garageSpotService.addGarageSpot(dto);
        log.info("garageSpot with id={} was successfully created", garageSpot.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(garageSpot);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGarageSpotById(@PathVariable long id) {
        log.info("DELETE /api/garage-spots/{} - deleting garageSpot by id={}", id, id);
        garageSpotService.deleteGarageSpot(id);
        log.info("garageSpot with id={} was successfully deleted", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/free")
    public ResponseEntity<List<GarageSpotResponseDto>> getFreeGarageSpots() {
        log.info("GET /api/garage-spots/free - fetching all free garageSpots");
        List<GarageSpotResponseDto> freeSpots = garageSpotService.getFreeSpots();
        log.info("free garageSpots was  successfully fetched");
        return ResponseEntity.ok(freeSpots);
    }
}
