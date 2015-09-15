package cms.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A Medicament.
 */
@Entity
@Table(name = "MEDICAMENT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="medicament")
public class Medicament implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    
    @Column(name = "nom")
    private String nom;
    
    @Column(name = "posologie")
    private String posologie;
    
    @Column(name = "ind_particulier")
    private String indParticulier;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "MEDICAMENT_ORDONNANCE",
               joinColumns = @JoinColumn(name="medicaments_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="ordonnances_id", referencedColumnName="ID"))
    private Set<Ordonnance> ordonnances = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPosologie() {
        return posologie;
    }

    public void setPosologie(String posologie) {
        this.posologie = posologie;
    }

    public String getIndParticulier() {
        return indParticulier;
    }

    public void setIndParticulier(String indParticulier) {
        this.indParticulier = indParticulier;
    }

    public Set<Ordonnance> getOrdonnances() {
        return ordonnances;
    }

    public void setOrdonnances(Set<Ordonnance> ordonnances) {
        this.ordonnances = ordonnances;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Medicament medicament = (Medicament) o;

        if ( ! Objects.equals(id, medicament.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Medicament{" +
                "id=" + id +
                ", nom='" + nom + "'" +
                ", posologie='" + posologie + "'" +
                ", indParticulier='" + indParticulier + "'" +
                '}';
    }
}
