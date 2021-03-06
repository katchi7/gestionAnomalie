package model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Emeteur {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String fName;
    private String lName;
    private String email;
    private String userType;
    public String getFullName(){
        if(fName==null && lName ==null) return null;
        else return (fName==null?"":fName)+" "+(lName==null?"":lName);
    }
}
