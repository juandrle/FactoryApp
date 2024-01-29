package de.swtpro.factorybuilder.repository;

        import de.swtpro.factorybuilder.entity.Factory;
        import org.springframework.data.jpa.repository.JpaRepository;

        import java.util.Optional;

public interface FactoryRepository extends JpaRepository<Factory, Long> {
        @Override
        Optional<Factory> findById(Long aLong);
}
