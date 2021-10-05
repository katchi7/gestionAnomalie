package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Fiche {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Date date;
    private String process;
    private Boolean nonConfirmite;
    private String remarqueStatut;
    @OneToOne
    private Emeteur emeteur;
    @OneToOne
    private Anomalie anomalie;
    @OneToOne
    private Action action;

    public String getDateFormatted() {

        return date!=null?date.toString():null;
    }

}
