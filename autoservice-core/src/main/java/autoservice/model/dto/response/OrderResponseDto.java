package autoservice.model.dto.response;

import autoservice.model.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponseDto(Long id,
                               String description,
                               MasterResponseDto masterResponseDto,
                               GarageSpotResponseDto garageSpotResponseDto,
                               LocalDateTime startTime,
                               LocalDateTime endTime,
                               OrderStatus orderStatus,
                               BigDecimal price ) {
}
