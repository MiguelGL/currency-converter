package com.mgl.currencyconverter.server.api.entity;

import java.time.Instant;

import com.mgl.currencyconverter.server.api.entity.support.BaseEntity;

import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.ValidationException;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(
            name = "rate__base_currency__rated_currency_uidx",
            columnNames = {"basecurrency_id", "ratedcurrency_id"})
})
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@ToString(callSuper = true)
public class Rate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.REMOVE)
    private Currency baseCurrency;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.REMOVE)
    private Currency ratedCurrency;

    @Min(0)
    @Column(nullable = false)
    private double rateValue;

    @NotNull
    // Not usable with Instant:
    //   http://stackoverflow.com/questions/35507985/hibernate-temporal-for-java-8-java-time-instant/35508263
    // @Temporal(TemporalType.TIMESTAMP)
    @Basic
    private Instant lastUpdateTs;

    @PrePersist @PreUpdate
    void prePersistOrUpdate() {
        if (Objects.equals(getBaseCurrency(), getRatedCurrency())) {
            if (rateValue != 1) {
                throw new ValidationException(String.format(
                        "Rate for same base and rated currencies (%s) must be one (not %f)",
                        getBaseCurrency().getCode(), getRateValue()));
            }
        }
    }

    public Rate relativise(Rate toOtherRate) {
        Instant minTs = getLastUpdateTs().isAfter(toOtherRate.getLastUpdateTs())
                ? toOtherRate.getLastUpdateTs()
                : getLastUpdateTs();
        double relRateValue = toOtherRate.getRateValue() / getRateValue();
        return new Rate(
                getBaseCurrency(),
                getRatedCurrency(),
                relRateValue,
                minTs);
    }

}
