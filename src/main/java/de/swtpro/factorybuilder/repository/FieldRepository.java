package de.swtpro.factorybuilder.repository;

import de.swtpro.factorybuilder.entity.Field;
import de.swtpro.factorybuilder.utility.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FieldRepository extends JpaRepository<Field, Long> {

    Optional<Field> findByPosAndFactoryID(Position position, long factoryID);

    Long countByPosAndFactoryID(Position position, long factoryID);


    void deleteAllByFactoryID(Long id);
}//   fhxhgdfzhcv
