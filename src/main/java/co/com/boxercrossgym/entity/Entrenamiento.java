package co.com.boxercrossgym.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "entrenamientos")
@Data
@AllArgsConstructor
public class Entrenamiento implements Serializable{

    private static final long serialVersionUID = 1L;
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    private String titulo;

    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "fecha_entreno")
    private Date fechaEntreno;


    @OneToMany( fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "entrenamiento_id")
    private List<Bloque> bloques;



    public void addBloque(Bloque bloque){
        this.bloques.add(bloque);
    }


    public Entrenamiento(){

        this.bloques = new ArrayList<>();

    }







    

    
}
