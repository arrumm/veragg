package com.veragg.website.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auction_sources")
public class AuctionSource {

    @Id
    private Integer id;

    private String name;

    @Enumerated(EnumType.STRING)
    @NonNull
    @Column(name = "source_type")
    private AuctionSourceType auctionSourceType;

    @NonNull
    @Column(name = "base_url")
    private String baseUrl;

    private Integer priority;

}
