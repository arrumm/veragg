package com.veragg.website.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.veragg.website.domain.Auction;
import com.veragg.website.repository.AuctionRepo;
import com.veragg.website.repository.CourtRepo;
import com.veragg.website.repository.DocumentAuctionRepo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

public class AuctionServiceImpl_when_delete_is_called {

    AuctionServiceImpl auctionService;

    @Mock
    AuctionRepo auctionRepo;

    @Mock
    CourtRepo courtRepo;

    @Mock
    DocumentAuctionRepo documentAuctionRepo;

    @Mock
    Auction auction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        auctionService = new AuctionServiceImpl(auctionRepo, documentAuctionRepo, courtRepo);
    }

    @Test
    public void given_auction_to_delete_then_delete_should_called() {

        //Arrange
        doNothing().when(auctionRepo).delete(eq(auction));

        //Act
        auctionService.delete(auction);

        //Assert
        verify(auctionRepo).delete(eq(auction));

    }

    @Test(expected = IllegalArgumentException.class)
    public void given_null_auction_to_delete_then_throw_IAE() {

        //Arrange
        doThrow(new IllegalArgumentException()).when(auctionRepo).delete(any());

        //Act
        auctionService.delete(null);

        //Assert

    }
}
