package utpDesarrolloMovil.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utpDesarrolloMovil.demo.model.Permiso;

public interface PermisoRepository extends JpaRepository<Permiso, Integer> {
    Permiso findById(int id);
}
