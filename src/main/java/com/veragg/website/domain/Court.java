package com.veragg.website.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(exclude = {
        "id",
        "address"
})
@Entity
public class Court {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Embedded
    private Address address;

    @NonNull
    private String name;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

    @OneToMany(mappedBy = "court", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Auction> auctions = new HashSet<>();

    public Court() {
    }

    public Court(@NonNull String name, @NonNull State state) {
        this.name = name;
        this.state = state;
        this.address = Address.builder().city(name).build();
    }

}
