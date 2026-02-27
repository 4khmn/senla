package autoservice.model.mapper;

import autoservice.model.dto.create.OrderCreateDto;
import autoservice.model.dto.response.OrderResponseDto;
import autoservice.model.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MasterMapper.class, GarageSpotMapper.class})
public interface OrderMapper {

    @Mapping(source = "master", target = "masterResponseDto")
    @Mapping(source = "garageSpot", target = "garageSpotResponseDto")
    OrderResponseDto toDto(Order order);

    Order toEntity(OrderCreateDto dto);
}
