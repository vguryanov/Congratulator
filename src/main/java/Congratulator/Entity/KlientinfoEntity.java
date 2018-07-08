package Congratulator.Entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by User2 on 04.03.2018.
 */
@Entity
@Table(name = "klientinfo", schema = "dbo", catalog = "")
public class KlientinfoEntity {
    private String id;
    private String famaly;
    private String name;
    private Timestamp dateOld;
    private String email;
    private Integer saveDateOld;
    private Boolean noSms;
    private String name2;
    private String famaly2;
    private Boolean deleted;

    @Id
    @Column(name = "id", nullable = false, length = 50)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "famaly", nullable = true, length = 200)
    public String getFamaly() {
        return famaly;
    }

    public void setFamaly(String famaly) {
        this.famaly = famaly;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "dateOld", nullable = true)
    public Timestamp getDateOld() {
        return dateOld;
    }

    public void setDateOld(Timestamp dateOld) {
        this.dateOld = dateOld;
    }

    @Basic
    @Column(name = "email", nullable = true, length = 40)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "saveDateOld", nullable = true)
    public Integer getSaveDateOld() {
        return saveDateOld;
    }

    public void setSaveDateOld(Integer saveDateOld) {
        this.saveDateOld = saveDateOld;
    }

    @Basic
    @Column(name = "NoSMS", nullable = true)
    public Boolean getNoSms() {
        return noSms;
    }

    public void setNoSms(Boolean noSms) {
        this.noSms = noSms;
    }

    @Basic
    @Column(name = "name2", nullable = true, length = 200)
    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    @Basic
    @Column(name = "famaly2", nullable = true, length = 200)
    public String getFamaly2() {
        return famaly2;
    }

    public void setFamaly2(String famaly2) {
        this.famaly2 = famaly2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KlientinfoEntity that = (KlientinfoEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (famaly != null ? !famaly.equals(that.famaly) : that.famaly != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (dateOld != null ? !dateOld.equals(that.dateOld) : that.dateOld != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (saveDateOld != null ? !saveDateOld.equals(that.saveDateOld) : that.saveDateOld != null) return false;
        if (noSms != null ? !noSms.equals(that.noSms) : that.noSms != null) return false;
        if (name2 != null ? !name2.equals(that.name2) : that.name2 != null) return false;
        if (famaly2 != null ? !famaly2.equals(that.famaly2) : that.famaly2 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (famaly != null ? famaly.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (dateOld != null ? dateOld.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (saveDateOld != null ? saveDateOld.hashCode() : 0);
        result = 31 * result + (noSms != null ? noSms.hashCode() : 0);
        result = 31 * result + (name2 != null ? name2.hashCode() : 0);
        result = 31 * result + (famaly2 != null ? famaly2.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "Deleted", nullable = true)
    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
