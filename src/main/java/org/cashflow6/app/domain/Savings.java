package org.cashflow6.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Savings.
 */
@Entity
@Table(name = "savings")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Savings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "actual_value")
    private Double actualValue;

    @Column(name = "initial_value")
    private Double initialValue;

    @Column(name = "initial_date")
    private LocalDate initialDate;

    @Column(name = "new_value")
    private Double newValue;

    @ManyToOne
    private Currency currency;

    @OneToMany(mappedBy = "savings",cascade=CascadeType.REMOVE)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SavingsAudit> savingsAudits = new HashSet<>();

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Double getActualValue() {
        return actualValue;
    }

    public void setActualValue(Double actualValue) {
        this.actualValue = actualValue;
    }

    public Double getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(Double initialValue) {
        this.initialValue = initialValue;
    }

    public LocalDate getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(LocalDate initialDate) {
        this.initialDate = initialDate;
    }

    public Double getNewValue() {
        return newValue;
    }

    public void setNewValue(Double newValue) {
        this.newValue = newValue;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Set<SavingsAudit> getSavingsAudits() {
        return savingsAudits;
    }

    public void setSavingsAudits(Set<SavingsAudit> savingsAudits) {
        this.savingsAudits = savingsAudits;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Savings savings = (Savings) o;
        if(savings.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, savings.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Savings{" +
            "id=" + id +
            ", accountName='" + accountName + "'" +
            ", actualValue='" + actualValue + "'" +
            ", initialValue='" + initialValue + "'" +
            ", initialDate='" + initialDate + "'" +
            ", newValue='" + newValue + "'" +
            '}';
    }
}
