package utpDesarrolloMovil.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utpDesarrolloMovil.demo.model.Tarifa;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, Integer> {

    Tarifa findById(int id);
}
