package com.virtuallink.virtualbank.mapper;

import com.virtuallink.virtualbank.dto.OperationDto;
import com.virtuallink.virtualbank.models.Operation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OperationMapper {

    /**
     * Convertit une entité Operation en OperationDto (record).
     * bankAccount.id est mappé vers le composant bankAccountId du record.
     */
    @Mapping(source = "bankAccount.id", target = "bankAccountId")
    OperationDto toDto(Operation operation);

    /**
     * Convertit une liste d'entités en liste de records OperationDto.
     */
    List<OperationDto> toDtoList(List<Operation> operations);

    /**
     * Convertit un record OperationDto en entité Operation.
     * id et bankAccount sont gérés par le service.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bankAccount", ignore = true)
    Operation toEntity(OperationDto dto);
}

