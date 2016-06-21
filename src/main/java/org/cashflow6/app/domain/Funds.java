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
 * A Funds.
 */
@Entity
@Table(name = "funds")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Funds implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "isin")
    private String isin;

    @Column(name = "name_fund")
    private String nameFund;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "actual_shares")
    private Integer actualShares;

    @Column(name = "initial_date")
    private LocalDate initialDate;

    @Column(name = "new_shares")
    private Integer newShares;

    @Column(name = "actual_value")
    private Double actualValue;

    @Column(name = "initial_value")
    private Double initialValue;

    @Column(name = "new_value")
    private Double newValue;

    @Column(name = "initial_shares")
    private Integer initialShares;

    @ManyToOne
    private Currency currency;

    @OneToMany(mappedBy = "funds",cascade=CascadeType.REMOVE)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<FundsAudit> fundsAudits = new HashSet<>();

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public String getNameFund() {
        return nameFund;
    }

    public void setNameFund(String nameFund) {
        this.nameFund = nameFund;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getActualShares() {
        return actualShares;
    }

    public void setActualShares(Integer actualShares) {
        this.actualShares = actualShares;
    }

    public LocalDate getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(LocalDate initialDate) {
        this.initialDate = initialDate;
    }

    public Integer getNewShares() {
        return newShares;
    }

    public void setNewShares(Integer newShares) {
        this.newShares = newShares;
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

    public Double getNewValue() {
        return newValue;
    }

    public void setNewValue(Double newValue) {
        this.newValue = newValue;
    }

    public Integer getInitialShares() {
        return initialShares;
    }

    public void setInitialShares(Integer initialShares) {
        this.initialShares = initialShares;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Set<FundsAudit> getFundsAudits() {
        return fundsAudits;
    }

    public void setFundsAudits(Set<FundsAudit> fundsAudits) {
        this.fundsAudits = fundsAudits;
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
        Funds funds = (Funds) o;
        if(funds.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, funds.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Funds{" +
            "id=" + id +
            ", isin='" + isin + "'" +
            ", nameFund='" + nameFund + "'" +
            ", companyName='" + companyName + "'" +
            ", actualShares='" + actualShares + "'" +
            ", initialDate='" + initialDate + "'" +
            ", newShares='" + newShares + "'" +
            ", actualValue='" + actualValue + "'" +
            ", initialValue='" + initialValue + "'" +
            ", newValue='" + newValue + "'" +
            ", initialShares='" + initialShares + "'" +
            '}';
    }
}
