package com.veragg.website.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.veragg.website.domain.Auction;
import com.veragg.website.repository.AuctionRepo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AuctionServiceImpl_when_save_is_called {

    AuctionServiceImpl sut;

    @Mock
    AuctionRepo auctionRepo;

    @Mock
    Auction auctionMock;

    @Before
    public void setup() {
        initMocks(this);
        sut = new AuctionServiceImpl(auctionRepo);
    }

    @Test
    public void given_auction_not_exist_then_all_interactions_performed_and_saved_auction_return() {

        //Arrange
        when(auctionMock.getFileNumber()).thenReturn("AZ number");
        when(auctionRepo.findByFileNumber(eq("AZ number"))).thenReturn(null);
        when(auctionRepo.save(eq(auctionMock))).thenReturn(auctionMock);

        //Act
        Auction result = sut.save(auctionMock);

        //Assert
        assertNotNull(result);
        assertEquals(auctionMock, result);
        verify(auctionRepo).findByFileNumber(anyString());
        verify(auctionRepo).save(any(Auction.class));

    }

    @Test
    public void given_auction_exist_then_no_save_is_called_and_found_auction_return() {

        //Arrange
        Auction auctionFound = mock(Auction.class);
        when(auctionMock.getFileNumber()).thenReturn("any");
        when(auctionRepo.findByFileNumber(anyString())).thenReturn(auctionFound);
        when(auctionRepo.save(eq(auctionFound))).thenReturn(auctionFound);

        //Act
        Auction result = sut.save(auctionMock);

        //Assert
        assertNotNull(result);
        assertEquals(auctionFound, result);
        verify(auctionRepo).findByFileNumber(anyString());
        verifyNoMoreInteractions(auctionRepo);

    }

    @Test
    public void given_save_auction_throw_IAE_then_throw_IAE() {

        //Arrange
        when(auctionRepo.save(any())).thenThrow(new IllegalArgumentException());

        //Act
        //Assert
        assertThrows(IllegalArgumentException.class, () -> sut.save(auctionMock));

    }

}
