package de.swtpro.factorybuilder.repository;

import de.swtpro.factorybuilder.entity.Field;
import de.swtpro.factorybuilder.utility.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GridRepository extends JpaRepository<Field, Long> {
    Optional<Field> findByPos(Position pos);

}
