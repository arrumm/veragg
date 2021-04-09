package com.veragg.website.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.veragg.website.domain.Auction;
import com.veragg.website.domain.AuctionSource;
import com.veragg.website.domain.AuctionStatus;
import com.veragg.website.domain.Court;
import com.veragg.website.repository.AuctionRepo;
import com.veragg.website.repository.CourtRepo;
import com.veragg.website.repository.DocumentAuctionRepo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

public class AuctionServiceImpl_WHEN_findBy_is_called {

    AuctionServiceImpl sut;

    @Mock
    AuctionRepo auctionRepo;

    @Mock
    CourtRepo courtRepo;

    @Mock
    DocumentAuctionRepo documentAuctionRepo;

    @Mock
    Auction auctionMock;

    @Mock
    AuctionSource auctionSource;

    @Mock
    Court court;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        sut = new AuctionServiceImpl(auctionRepo, documentAuctionRepo, courtRepo);
    }

    @Test
    public void GIVEN_auction_is_null_THEN_NPE_thrown() {

        //Arrange
        //Act
        //Assert
        assertThrows(NullPointerException.class, () -> sut.findBy(null));

    }

    @Test
    public void GIVEN_valid_auction_THEN_auction_should_return() {

        //Arrange
        when(auctionRepo.findByFileNumberAndCourtAndSourceAndAuctionStatus("1K 12", court, auctionSource, AuctionStatus.DRAFT)).thenReturn(auctionMock);
        when(auctionMock.getFileNumber()).thenReturn("1K 12");
        when(auctionMock.getCourt()).thenReturn(court);
        when(auctionMock.getSource()).thenReturn(auctionSource);

        //Act
        Auction result = sut.findBy(auctionMock);

        //Assert
        assertNotNull(result);
        assertEquals(auctionMock, result);

    }

}
