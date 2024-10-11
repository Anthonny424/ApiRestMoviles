package utpDesarrolloMovil.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT-5")
    private Date fecha;
    private double saldoanterior;
    private double importe;
    private double nuevosaldo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tarjetas_id")
    @JsonBackReference
    private Tarjeta tarjeta;


}
