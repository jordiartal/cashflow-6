package org.cashflow6.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Deposits.
 */
@Entity
@Table(name = "deposits")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Deposits implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "initial_value")
    private Double initialValue;

    @Column(name = "final_value")
    private Double finalValue;

    @Column(name = "initial_date")
    private LocalDate initialDate;

    @Column(name = "exp_date")
    private LocalDate expDate;

    @Column(name = "interest")
    private Double interest;

    @ManyToOne
    private Currency currency;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Double getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(Double initialValue) {
        this.initialValue = initialValue;
    }

    public Double getFinalValue() {
        return finalValue;
    }

    public void setFinalValue(Double finalValue) {
        this.finalValue = finalValue;
    }

    public LocalDate getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(LocalDate initialDate) {
        this.initialDate = initialDate;
    }

    public LocalDate getExpDate() {
        return expDate;
    }

    public void setExpDate(LocalDate expDate) {
        this.expDate = expDate;
    }

    public Double getInterest() {
        return interest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
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
        Deposits deposits = (Deposits) o;
        if(deposits.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, deposits.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Deposits{" +
            "id=" + id +
            ", companyName='" + companyName + "'" +
            ", initialValue='" + initialValue + "'" +
            ", finalValue='" + finalValue + "'" +
            ", initialDate='" + initialDate + "'" +
            ", expDate='" + expDate + "'" +
            ", interest='" + interest + "'" +
            '}';
    }
}
