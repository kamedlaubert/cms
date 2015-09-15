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
 * A Personnel.
 */
@Entity
@Table(name = "PERSONNEL")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="personnel")
public class Personnel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    
    @Column(name = "matricule")
    private String matricule;
    
    @Column(name = "nom")
    private String nom;
    
    @Column(name = "prenom")
    private String prenom;
    
    @Column(name = "direction")
    private String direction;
    
    @Column(name = "age")
    private String age;
    
    @Column(name = "sexe")
    private String sexe;
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "famille")
    private String famille;
    
    @Column(name = "groupe_sanguin")
    private String groupeSanguin;
    
    @Column(name = "infos_compl")
    private String infosCompl;
    
    @Column(name = "allergie")
    private String allergie;
    
    @Column(name = "statut")
    private String statut;

    @OneToMany(mappedBy = "personnel")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Consultation> realisers = new HashSet<>();

    @ManyToOne
    private Conjoint conjoint;

    @ManyToOne
    private Enfant enfant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFamille() {
        return famille;
    }

    public void setFamille(String famille) {
        this.famille = famille;
    }

    public String getGroupeSanguin() {
        return groupeSanguin;
    }

    public void setGroupeSanguin(String groupeSanguin) {
        this.groupeSanguin = groupeSanguin;
    }

    public String getInfosCompl() {
        return infosCompl;
    }

    public void setInfosCompl(String infosCompl) {
        this.infosCompl = infosCompl;
    }

    public String getAllergie() {
        return allergie;
    }

    public void setAllergie(String allergie) {
        this.allergie = allergie;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Set<Consultation> getRealisers() {
        return realisers;
    }

    public void setRealisers(Set<Consultation> consultations) {
        this.realisers = consultations;
    }

    public Conjoint getConjoint() {
        return conjoint;
    }

    public void setConjoint(Conjoint conjoint) {
        this.conjoint = conjoint;
    }

    public Enfant getEnfant() {
        return enfant;
    }

    public void setEnfant(Enfant enfant) {
        this.enfant = enfant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Personnel personnel = (Personnel) o;

        if ( ! Objects.equals(id, personnel.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Personnel{" +
                "id=" + id +
                ", matricule='" + matricule + "'" +
                ", nom='" + nom + "'" +
                ", prenom='" + prenom + "'" +
                ", direction='" + direction + "'" +
                ", age='" + age + "'" +
                ", sexe='" + sexe + "'" +
                ", type='" + type + "'" +
                ", famille='" + famille + "'" +
                ", groupeSanguin='" + groupeSanguin + "'" +
                ", infosCompl='" + infosCompl + "'" +
                ", allergie='" + allergie + "'" +
                ", statut='" + statut + "'" +
                '}';
    }
}
