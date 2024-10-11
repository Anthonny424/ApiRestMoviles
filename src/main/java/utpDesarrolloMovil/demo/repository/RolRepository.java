package utpDesarrolloMovil.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utpDesarrolloMovil.demo.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Integer> {

    Rol findById(int id);
}
