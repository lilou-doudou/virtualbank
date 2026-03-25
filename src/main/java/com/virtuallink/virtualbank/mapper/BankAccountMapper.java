package com.virtuallink.virtualbank.mapper;

import com.virtuallink.virtualbank.dto.BankAccountDto;
import com.virtuallink.virtualbank.models.BankAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BankAccountMapper {

    /**
     * Convertit une entité BankAccount en record BankAccountDto.
     * MapStruct utilise le constructeur canonique du record.
     */
    BankAccountDto toDto(BankAccount bankAccount);

    /**
     * Convertit une liste d'entités en liste de records BankAccountDto.
     */
    List<BankAccountDto> toDtoList(List<BankAccount> bankAccounts);

    /**
     * Convertit un record BankAccountDto en entité BankAccount.
     * MapStruct utilise les accesseurs du record (id(), ownerName(), …).
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "operations", ignore = true)
    BankAccount toEntity(BankAccountDto dto);

    /**
     * Met à jour une entité existante depuis un record BankAccountDto.
     * La cible est l'entité (mutable), la source est le record (immuable).
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "operations", ignore = true)
    void updateEntityFromDto(BankAccountDto dto, @MappingTarget BankAccount bankAccount);
}
