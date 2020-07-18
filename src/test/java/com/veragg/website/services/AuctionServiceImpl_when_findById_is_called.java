package com.veragg.website.services;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.veragg.website.domain.Auction;
import com.veragg.website.repository.AuctionRepo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class AuctionServiceImpl_when_findById_is_called {

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
    public void given_valid_id_then_auction_should_return() {

        //Arrange
        Optional<Auction> auctionOptional = Optional.of(auctionMock);
        when(auctionRepo.findById(eq(1L))).thenReturn(auctionOptional);

        //Act
        Auction result = auctionService.findById(1L);

        //Assert
        assertNotNull(result);
        assertEquals(auctionMock, result);

    }

    @Test
    public void given_id_with_no_auction_then_null_should_return() {

        //Arrange
        when(auctionRepo.findById(eq(1L))).thenReturn(Optional.empty());

        //Act
        Auction result = auctionService.findById(1L);

        //Assert
        assertNull(result);

    }

    @Test(expected = IllegalArgumentException.class)
    public void given_null_auction_id_to_find_then_throw_IAE() {

        //Arrange
        when(auctionRepo.findById(anyLong())).thenThrow(new IllegalArgumentException());

        //Act
        auctionService.findById(1L);

        //Assert

    }

}
