package autoservice.model.mapper;

import autoservice.model.dto.create.GarageSpotCreateDto;
import autoservice.model.dto.response.GarageSpotResponseDto;
import autoservice.model.entities.GarageSpot;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GarageSpotMapper {
    GarageSpotResponseDto toDto(GarageSpot garageSpot);

    GarageSpot toEntity(GarageSpotCreateDto dto);
}
