package utpDesarrolloMovil.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utpDesarrolloMovil.demo.model.Persona;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {
}
