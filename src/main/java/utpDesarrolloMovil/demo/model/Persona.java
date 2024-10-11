package utpDesarrolloMovil.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "personas")
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @NotBlank
    private String nombre;
    @NotNull
    @NotBlank
    private String apellido;
    @Positive
    @NotNull
    @Min(value = 10000000, message = "El número de tarjeta debe ser de al menos 8 dígitos")
    @Max(value = 99999999, message = "El número de tarjeta no puede superar los 8 dígitos")
    @Column(unique = true)
    private int dni;
    @Positive
    @NotNull
    @Min(value = 10, message = "La edad minima es 10 años")
    @Max(value = 90, message = "La edad máxima es 90 años")
    private int edad;

    @OneToOne
    @JoinColumn(name = "usuarios_id")
    @JsonIgnore
    private Usuario usuario;
}
