package org.cashflow6.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A SavingsAudit.
 */
@Entity
@Table(name = "savings_audit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SavingsAudit implements Serializable {

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

    @ManyToOne(cascade=CascadeType.REMOVE)
    private Savings savings;

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

    public Savings getSavings() {
        return savings;
    }

    public void setSavings(Savings savings) {
        this.savings = savings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SavingsAudit savingsAudit = (SavingsAudit) o;
        if(savingsAudit.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, savingsAudit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SavingsAudit{" +
            "id=" + id +
            ", opDate='" + opDate + "'" +
            ", antValue='" + antValue + "'" +
            ", newValue='" + newValue + "'" +
            '}';
    }
}
