package autoservice.model.dto.response;

import java.math.BigDecimal;

public record MasterResponseDto(Long id, String name, BigDecimal salary) {
}
