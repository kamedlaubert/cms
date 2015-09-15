package cms.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A InstitutionSanitaire.
 */
@Entity
@Table(name = "INSTITUTIONSANITAIRE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="institutionsanitaire")
public class InstitutionSanitaire implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    
    @Column(name = "raison_social")
    private String raisonSocial;
    
    @Column(name = "nom")
    private String nom;
    
    @Column(name = "type")
    private String type;

    @OneToMany(mappedBy = "institutionSanitaire")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Hospitalisation> hospitalisations = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRaisonSocial() {
        return raisonSocial;
    }

    public void setRaisonSocial(String raisonSocial) {
        this.raisonSocial = raisonSocial;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Hospitalisation> getHospitalisations() {
        return hospitalisations;
    }

    public void setHospitalisations(Set<Hospitalisation> hospitalisations) {
        this.hospitalisations = hospitalisations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InstitutionSanitaire institutionSanitaire = (InstitutionSanitaire) o;

        if ( ! Objects.equals(id, institutionSanitaire.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InstitutionSanitaire{" +
                "id=" + id +
                ", raisonSocial='" + raisonSocial + "'" +
                ", nom='" + nom + "'" +
                ", type='" + type + "'" +
                '}';
    }
}
