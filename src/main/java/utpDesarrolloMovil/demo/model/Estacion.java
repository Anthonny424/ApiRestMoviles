package utpDesarrolloMovil.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "estaciones")
public class Estacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
    private String servicios;

    @OneToMany(mappedBy = "estacion", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Horario> horarios;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(name = "estaciones_usuarios",
    joinColumns = @JoinColumn(name = "estaciones_id"),
    inverseJoinColumns = @JoinColumn(name = "usuarios_id"))
    private List<Usuario> usuarios;
}
