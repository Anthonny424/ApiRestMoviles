package utpDesarrolloMovil.demo.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "tarjetas")
public class Tarjeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double saldo;
    @Positive
    @NotNull
    @Min(value = 10000000, message = "El número de tarjeta debe ser de al menos 8 dígitos")
    @Max(value = 99999999, message = "El número de tarjeta no puede superar los 8 dígitos")
    @Column(unique = true)
    private int nrotarjeta;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuarios_id")
    @JsonBackReference
    private Usuario usuario;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tarifas_id")
    private Tarifa tarifa;
}
