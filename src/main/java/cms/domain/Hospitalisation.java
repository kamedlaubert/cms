package cms.domain;

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
 * A Hospitalisation.
 */
@Entity
@Table(name = "HOSPITALISATION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="hospitalisation")
public class Hospitalisation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    
    @Column(name = "code")
    private String code;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "date_admission")
    private LocalDate dateAdmission;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "date_sorti")
    private LocalDate dateSorti;
    
    @Column(name = "motif")
    private String motif;
    
    @Column(name = "bilan")
    private String bilan;

    @ManyToOne
    private Consultation consultation;

    @OneToOne
    private BPC bPC;

    @ManyToOne
    private InstitutionSanitaire institutionSanitaire;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDate getDateAdmission() {
        return dateAdmission;
    }

    public void setDateAdmission(LocalDate dateAdmission) {
        this.dateAdmission = dateAdmission;
    }

    public LocalDate getDateSorti() {
        return dateSorti;
    }

    public void setDateSorti(LocalDate dateSorti) {
        this.dateSorti = dateSorti;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getBilan() {
        return bilan;
    }

    public void setBilan(String bilan) {
        this.bilan = bilan;
    }

    public Consultation getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

    public BPC getBPC() {
        return bPC;
    }

    public void setBPC(BPC bPC) {
        this.bPC = bPC;
    }

    public InstitutionSanitaire getInstitutionSanitaire() {
        return institutionSanitaire;
    }

    public void setInstitutionSanitaire(InstitutionSanitaire institutionSanitaire) {
        this.institutionSanitaire = institutionSanitaire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Hospitalisation hospitalisation = (Hospitalisation) o;

        if ( ! Objects.equals(id, hospitalisation.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Hospitalisation{" +
                "id=" + id +
                ", code='" + code + "'" +
                ", dateAdmission='" + dateAdmission + "'" +
                ", dateSorti='" + dateSorti + "'" +
                ", motif='" + motif + "'" +
                ", bilan='" + bilan + "'" +
                '}';
    }
}
