package de.swtpro.factorybuilder.DTO.user;

import de.swtpro.factorybuilder.entity.Factory;

import java.util.List;

public record UserWithFactoriesDTO(String username, List<Factory> factoryIDs) {
}
