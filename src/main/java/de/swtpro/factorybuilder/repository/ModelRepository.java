package de.swtpro.factorybuilder.repository;

import de.swtpro.factorybuilder.entity.Model;
import de.swtpro.factorybuilder.entity.PlacedModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ModelRepository extends JpaRepository<Model, Long> {
    @Override
    List<Model> findAll();

    @Override
    Optional<Model> findById(Long aLong);

}
