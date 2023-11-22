package de.swtpro.factorybuilder.repository;

import de.swtpro.factorybuilder.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MachineRepository extends JpaRepository<Machine, Long> {
}
