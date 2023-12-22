package de.swtpro.factorybuilder.repository;

import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.entity.PlacedModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlacedModelRepository extends JpaRepository<PlacedModel, Long> {

    @Override
    List<PlacedModel> findAll();

    @Override
    Optional<PlacedModel> findById(Long aLong);

    List<PlacedModel> findByFactory(Factory factory);
}
