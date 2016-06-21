package org.cashflow6.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A StockAudit.
 */
@Entity
@Table(name = "stock_audit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockAudit implements Serializable {

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
    private Stock stock;

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

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockAudit stockAudit = (StockAudit) o;
        if(stockAudit.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stockAudit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockAudit{" +
            "id=" + id +
            ", opDate='" + opDate + "'" +
            ", antValue='" + antValue + "'" +
            ", newValue='" + newValue + "'" +
            ", antShares='" + antShares + "'" +
            ", newShares='" + newShares + "'" +
            '}';
    }
}
