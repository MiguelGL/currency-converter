package com.mgl.currencyconverter.server.api.entity;

import com.mgl.currencyconverter.server.api.entity.support.BaseEntity;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "currency__code_uidx", columnNames = {"code"})
}, indexes = {
    // For filtering by name, for example
    @Index(name = "currency__name_idx", columnList = "name")
})
@NoArgsConstructor
@Getter @Setter
@ToString(callSuper = true)
public class Currency extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public static final int CODE_LEN = 3;

    public static final int NAME_MIN_LEN = 0;
    public static final int NAME_MAX_LEN = 64;

    @NotNull
    @Size(min = CODE_LEN, max = CODE_LEN)
    @Pattern(regexp = "^[A-Z]{3}$")
    private String code;

    @NotNull
    @Size(min = NAME_MIN_LEN, max = NAME_MAX_LEN)
    private String name;

    @OneToMany(mappedBy = "baseCurrency", orphanRemoval = true)
    private Collection<Rate> basedRates;

    @OneToMany(mappedBy = "ratedCurrency", orphanRemoval = true)
    private Collection<Rate> ratedRates;

    public Currency(String code, String name) {
        this.code = code;
        this.name = name;
    }

}
