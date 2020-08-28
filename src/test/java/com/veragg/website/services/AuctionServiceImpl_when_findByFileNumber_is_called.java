package com.veragg.website.services;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.veragg.website.domain.Auction;
import com.veragg.website.repository.AuctionRepo;
import com.veragg.website.repository.DocumentAuctionRepo;

public class AuctionServiceImpl_when_findByFileNumber_is_called {

    AuctionServiceImpl auctionService;

    @Mock
    AuctionRepo auctionRepo;

    @Mock
    DocumentAuctionRepo documentAuctionRepo;

    @Mock
    Auction auctionMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        auctionService = new AuctionServiceImpl(auctionRepo, documentAuctionRepo);
    }

    //TODO: fix tests

    //    @Test
    //    public void given_valid_fileNumber_then_auction_should_return() {
    //
    //        //Arrange
    //        when(auctionRepo.findByFileNumber(eq("1K 12"))).thenReturn(auctionMock);
    //
    //        //Act
    //        Auction result = auctionService.findByFileNumber("1K 12");
    //
    //        //Assert
    //        assertNotNull(result);
    //        assertEquals(auctionMock, result);
    //
    //    }

    //    @Test
    //    public void given_fileNumber_with_no_auction_then_null_should_return() {
    //
    //        //Arrange
    //        when(auctionRepo.findByFileNumber(eq("1K 12"))).thenReturn(null);
    //
    //        //Act
    //        Auction result = auctionService.findByFileNumber("1K 12");
    //
    //        //Assert
    //        assertNull(result);
    //
    //    }
    //
    //    @Test(expected = IllegalArgumentException.class)
    //    public void given_null_fileNumber_to_find_then_throw_IAE() {
    //
    //        //Arrange
    //        when(auctionRepo.findByFileNumber(anyString())).thenThrow(new IllegalArgumentException());
    //
    //        //Act
    //        auctionService.findByFileNumber("1K 12");
    //
    //        //Assert
    //
    //    }

}
