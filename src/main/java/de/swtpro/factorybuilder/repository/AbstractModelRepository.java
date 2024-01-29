package de.swtpro.factorybuilder.repository;

import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.entity.model.AbstractModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AbstractModelRepository extends JpaRepository<AbstractModel, Long> {

    @Override
    List<AbstractModel> findAll();
    @Override
    Optional<AbstractModel> findById(Long aLong);

    List<AbstractModel> findByFactory(Factory factory);
}
