package de.swtpro.factorybuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.swtpro.factorybuilder.entity.User;

public interface UserRepository  extends JpaRepository<User,String>{
    
}
