package cms.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cms.domain.util.CustomLocalDateSerializer;
import cms.domain.util.ISO8601LocalDateDeserializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A Consultation.
 */
@Entity
@Table(name = "CONSULTATION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="consultation")
public class Consultation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    
    @Column(name = "motif")
    private String motif;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "date")
    private LocalDate date;
    
    @Column(name = "diagnostique_consul")
    private String diagnostiqueConsul;

    @OneToMany(mappedBy = "consultation")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Practiciens> effectuers = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "CONSULTATION_CONDUIRE",
               joinColumns = @JoinColumn(name="consultations_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="conduires_id", referencedColumnName="ID"))
    private Set<Ordonnance> conduires = new HashSet<>();

    @OneToMany(mappedBy = "consultation")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Symptome> symptomes = new HashSet<>();

    @OneToMany(mappedBy = "consultation")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Hospitalisation> hospitalisations = new HashSet<>();

    @ManyToMany(mappedBy = "consultations")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Examens> examenss = new HashSet<>();

    @ManyToOne
    private Personnel personnel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDiagnostiqueConsul() {
        return diagnostiqueConsul;
    }

    public void setDiagnostiqueConsul(String diagnostiqueConsul) {
        this.diagnostiqueConsul = diagnostiqueConsul;
    }

    public Set<Practiciens> getEffectuers() {
        return effectuers;
    }

    public void setEffectuers(Set<Practiciens> practicienss) {
        this.effectuers = practicienss;
    }

    public Set<Ordonnance> getConduires() {
        return conduires;
    }

    public void setConduires(Set<Ordonnance> ordonnances) {
        this.conduires = ordonnances;
    }

    public Set<Symptome> getSymptomes() {
        return symptomes;
    }

    public void setSymptomes(Set<Symptome> symptomes) {
        this.symptomes = symptomes;
    }

    public Set<Hospitalisation> getHospitalisations() {
        return hospitalisations;
    }

    public void setHospitalisations(Set<Hospitalisation> hospitalisations) {
        this.hospitalisations = hospitalisations;
    }

    public Set<Examens> getExamenss() {
        return examenss;
    }

    public void setExamenss(Set<Examens> examenss) {
        this.examenss = examenss;
    }

    public Personnel getPersonnel() {
        return personnel;
    }

    public void setPersonnel(Personnel personnel) {
        this.personnel = personnel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Consultation consultation = (Consultation) o;

        if ( ! Objects.equals(id, consultation.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Consultation{" +
                "id=" + id +
                ", motif='" + motif + "'" +
                ", date='" + date + "'" +
                ", diagnostiqueConsul='" + diagnostiqueConsul + "'" +
                '}';
    }
}
