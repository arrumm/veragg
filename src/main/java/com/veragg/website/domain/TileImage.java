package com.veragg.website.domain;

import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TileImage extends Document {

    @OneToOne
    private Document originalImage;

}
