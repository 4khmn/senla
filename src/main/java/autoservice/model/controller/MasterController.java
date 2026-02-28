package autoservice.model.controller;

import autoservice.model.dto.create.MasterCreateDto;
import autoservice.model.dto.response.MasterResponseDto;
import autoservice.model.dto.response.OrderResponseDto;
import autoservice.model.entities.Master;
import autoservice.model.enums.MastersSortEnum;
import autoservice.model.mapper.MasterMapper;
import autoservice.model.service.MasterService;
import autoservice.model.service.OrderService;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/masters")
public class MasterController {

    private final MasterMapper mapper;
    private final MasterService masterService;
    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<MasterResponseDto> getMasterById(@PathVariable long id) {
        log.info("GET /api/masters/{} - fetching master by id={}", id, id);
        MasterResponseDto masterById = masterService.getMasterById(id);
        log.info("master with id={} was successfully fetched", id);
        return ResponseEntity.ok(masterById);
    }

    @GetMapping
    public ResponseEntity<List<MasterResponseDto>> getMasters() {
        log.info("GET /api/masters - fetching all masters");
        List<Master> masters = masterService.getMasters();
        log.info("masters was successfully fetched");
        return ResponseEntity.ok(masters.stream().map(mapper::toDto).toList());
    }

    @PostMapping
    public ResponseEntity<MasterResponseDto> createMaster(@RequestBody MasterCreateDto dto) {
        log.info("POST /api/masters - creating master={}", dto);
        MasterResponseDto master = masterService.addMaster(dto);
        log.info("master with id={} was successfully created", master.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(master);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaster(@PathVariable long id) {
        log.info("DELETE /api/masters/{} - deleting master by id={}", id, id);
        masterService.deleteMaster(id);
        log.info("master with id={} was successfully deleted", id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/sort")
    public ResponseEntity<List<MasterResponseDto>> getSortedMasters(
            @RequestParam(value = "decision", defaultValue = "BY_NAME") MastersSortEnum decision) {
        log.info("GET /api/masters/sort?decision={}", decision);
        List<MasterResponseDto> masterResponseDtos = masterService.mastersSort(decision);
        log.info("Masters successfully sorted by decision={}", decision);
        return ResponseEntity.ok(masterResponseDtos);
    }

    @GetMapping("/{id}/active-order")
    public ResponseEntity<OrderResponseDto> getActiveOrder(@PathVariable("id") long masterId) {
        log.info("GET /api/masters/{}/active-order - fetching order by master with id={}", masterId, masterId);
        OrderResponseDto order = orderService.getOrderByMaster(masterId);
        log.info("order with id={} by master with id={} was successfully fetched", order.id(), masterId);
        return ResponseEntity.ok(order);
    }
}
