package autoservice.model.dto.create;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderCreateDto(LocalDateTime date,
                             String description,
                             int durationInHours,
                             BigDecimal price,
                             Long masterId) {
}
