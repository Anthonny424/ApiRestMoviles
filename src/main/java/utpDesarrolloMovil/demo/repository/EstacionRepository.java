package utpDesarrolloMovil.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utpDesarrolloMovil.demo.model.Estacion;

import java.util.List;

@Repository
public interface EstacionRepository extends JpaRepository<Estacion, Integer> {

}
