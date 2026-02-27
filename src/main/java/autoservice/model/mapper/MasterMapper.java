package autoservice.model.mapper;

import autoservice.model.dto.create.MasterCreateDto;
import autoservice.model.dto.response.MasterResponseDto;
import autoservice.model.entities.Master;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MasterMapper {

    MasterResponseDto toDto(Master master);

    Master toEntity(MasterCreateDto dto);
}
