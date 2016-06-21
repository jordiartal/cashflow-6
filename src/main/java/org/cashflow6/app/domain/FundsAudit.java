package org.cashflow6.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A FundsAudit.
 */
@Entity
@Table(name = "funds_audit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FundsAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "op_date")
    private LocalDate opDate;

    @Column(name = "ant_value")
    private Double antValue;

    @Column(name = "new_value")
    private Double newValue;

    @Column(name = "ant_shares")
    private Integer antShares;

    @Column(name = "new_shares")
    private Integer newShares;

    @ManyToOne(cascade=CascadeType.REMOVE)
    private Funds funds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getOpDate() {
        return opDate;
    }

    public void setOpDate(LocalDate opDate) {
        this.opDate = opDate;
    }

    public Double getAntValue() {
        return antValue;
    }

    public void setAntValue(Double antValue) {
        this.antValue = antValue;
    }

    public Double getNewValue() {
        return newValue;
    }

    public void setNewValue(Double newValue) {
        this.newValue = newValue;
    }

    public Integer getAntShares() {
        return antShares;
    }

    public void setAntShares(Integer antShares) {
        this.antShares = antShares;
    }

    public Integer getNewShares() {
        return newShares;
    }

    public void setNewShares(Integer newShares) {
        this.newShares = newShares;
    }

    public Funds getFunds() {
        return funds;
    }

    public void setFunds(Funds funds) {
        this.funds = funds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FundsAudit fundsAudit = (FundsAudit) o;
        if(fundsAudit.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, fundsAudit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FundsAudit{" +
            "id=" + id +
            ", opDate='" + opDate + "'" +
            ", antValue='" + antValue + "'" +
            ", newValue='" + newValue + "'" +
            ", antShares='" + antShares + "'" +
            ", newShares='" + newShares + "'" +
            '}';
    }
}
