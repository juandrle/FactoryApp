package de.swtpro.factorybuilder.repository;

        import de.swtpro.factorybuilder.entity.Field;
        import org.springframework.data.jpa.repository.JpaRepository;

public interface FactoryRepository extends JpaRepository<Field, Long> {

}
