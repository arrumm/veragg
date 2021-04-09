package com.veragg.website.repository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.veragg.website.domain.AuctionSource;
import com.veragg.website.domain.AuctionStatus;
import com.veragg.website.domain.Court;

import static org.junit.Assert.assertThrows;

public class AuctionRepo_when_findByFileNumberAndCourtAndSourceAndAuctionStatus_is_called {

    AuctionRepo sut;

    @Mock
    AuctionSource auctionSource;

    @Mock
    Court court;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void given_auctionSource_is_null_then_NPE_thrown() {

        //Arrange
        //Act
        //Assert
        assertThrows(NullPointerException.class, () -> sut.findByFileNumberAndCourtAndSourceAndAuctionStatus("", court, null, AuctionStatus.DRAFT));

    }

    @Test
    public void given_fileNumber_is_null_then_NPE_thrown() {

        //Arrange
        //Act
        //Assert
        assertThrows(NullPointerException.class, () -> sut.findByFileNumberAndCourtAndSourceAndAuctionStatus(null, court, auctionSource, AuctionStatus.DRAFT));

    }

    @Test
    public void given_court_is_null_then_NPE_thrown() {

        //Arrange
        //Act
        //Assert
        assertThrows(NullPointerException.class, () -> sut.findByFileNumberAndCourtAndSourceAndAuctionStatus("", null, auctionSource, AuctionStatus.DRAFT));

    }

}
