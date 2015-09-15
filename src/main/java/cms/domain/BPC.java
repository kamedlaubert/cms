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
 * A BPC.
 */
@Entity
@Table(name = "BPC")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="bpc")
public class BPC implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    
    @Column(name = "numero_bpc")
    private String numeroBpc;
    
    @Column(name = "object_bpc")
    private String objectBpc;

    @OneToOne(mappedBy = "bPC")
    @JsonIgnore
    private Hospitalisation hospitalisation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroBpc() {
        return numeroBpc;
    }

    public void setNumeroBpc(String numeroBpc) {
        this.numeroBpc = numeroBpc;
    }

    public String getObjectBpc() {
        return objectBpc;
    }

    public void setObjectBpc(String objectBpc) {
        this.objectBpc = objectBpc;
    }

    public Hospitalisation getHospitalisation() {
        return hospitalisation;
    }

    public void setHospitalisation(Hospitalisation hospitalisation) {
        this.hospitalisation = hospitalisation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BPC bPC = (BPC) o;

        if ( ! Objects.equals(id, bPC.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BPC{" +
                "id=" + id +
                ", numeroBpc='" + numeroBpc + "'" +
                ", objectBpc='" + objectBpc + "'" +
                '}';
    }
}
