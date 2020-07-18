package com.veragg.website.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.veragg.website.domain.Auction;
import com.veragg.website.repository.AuctionRepo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuctionServiceImpl_when_save_is_called {

    AuctionServiceImpl auctionService;

    @Mock
    AuctionRepo auctionRepo;

    @Mock
    Auction auctionMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        auctionService = new AuctionServiceImpl(auctionRepo);
    }

    @Test
    public void given_draft_to_save_then_save_should_called() {

        //Arrange
        Auction auction = new Auction();
        when(auctionRepo.save(any(Auction.class))).thenReturn(auctionMock);

        //Act
        Auction result = auctionService.save(auction);

        //Assert
        assertNotNull(result);
        assertEquals(auctionMock, result);
        verify(auctionRepo).save(eq(auction));

    }

    @Test(expected = IllegalArgumentException.class)
    public void given_null_draft_to_save_then_throw_IAE() {

        //Arrange
        when(auctionRepo.save(any())).thenThrow(new IllegalArgumentException());

        //Act
        auctionService.save(null);

        //Assert

    }

}
