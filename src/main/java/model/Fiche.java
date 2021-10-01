package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Fiche {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer date;
    private String process;
    private Boolean nonConfirmite;
    private String remarqueStatut;
    @OneToOne
    private Emeteur emeteur;
    @OneToOne
    private Anomalie anomalie;
    @OneToOne
    private Action action;
}
