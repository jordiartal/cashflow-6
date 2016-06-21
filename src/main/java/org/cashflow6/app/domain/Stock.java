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
 * A Stock.
 */
@Entity
@Table(name = "stock")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "actual_shares")
    private Integer actualShares;

    @Column(name = "new_shares")
    private Integer newShares;

    @Column(name = "initial_date")
    private LocalDate initialDate;

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

    @OneToMany(mappedBy = "stock",cascade=CascadeType.REMOVE)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<StockAudit> stockAudits = new HashSet<>();

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
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

    public Integer getNewShares() {
        return newShares;
    }

    public void setNewShares(Integer newShares) {
        this.newShares = newShares;
    }

    public LocalDate getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(LocalDate initialDate) {
        this.initialDate = initialDate;
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

    public Set<StockAudit> getStockAudits() {
        return stockAudits;
    }

    public void setStockAudits(Set<StockAudit> stockAudits) {
        this.stockAudits = stockAudits;
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
        Stock stock = (Stock) o;
        if(stock.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stock.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Stock{" +
            "id=" + id +
            ", ticker='" + ticker + "'" +
            ", companyName='" + companyName + "'" +
            ", actualShares='" + actualShares + "'" +
            ", newShares='" + newShares + "'" +
            ", initialDate='" + initialDate + "'" +
            ", actualValue='" + actualValue + "'" +
            ", initialValue='" + initialValue + "'" +
            ", newValue='" + newValue + "'" +
            ", initialShares='" + initialShares + "'" +
            '}';
    }
}
