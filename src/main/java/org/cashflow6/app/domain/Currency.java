package org.cashflow6.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Currency.
 */
@Entity
@Table(name = "currency")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Currency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "conver")
    private Double conver;

    @OneToMany(mappedBy = "currency")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Savings> savings = new HashSet<>();

    @OneToMany(mappedBy = "currency")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Deposits> deposits = new HashSet<>();

    @OneToMany(mappedBy = "currency")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Funds> funds = new HashSet<>();

    @OneToMany(mappedBy = "currency")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Stock> stocks = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getConver() {
        return conver;
    }

    public void setConver(Double conver) {
        this.conver = conver;
    }

    public Set<Savings> getSavings() {
        return savings;
    }

    public void setSavings(Set<Savings> savings) {
        this.savings = savings;
    }

    public Set<Deposits> getDeposits() {
        return deposits;
    }

    public void setDeposits(Set<Deposits> deposits) {
        this.deposits = deposits;
    }

    public Set<Funds> getFunds() {
        return funds;
    }

    public void setFunds(Set<Funds> funds) {
        this.funds = funds;
    }

    public Set<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(Set<Stock> stocks) {
        this.stocks = stocks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Currency currency = (Currency) o;
        if(currency.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, currency.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Currency{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", code='" + code + "'" +
            ", conver='" + conver + "'" +
            '}';
    }
}
