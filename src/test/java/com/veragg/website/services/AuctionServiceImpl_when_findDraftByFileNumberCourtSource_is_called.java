package com.veragg.website.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.veragg.website.domain.Auction;
import com.veragg.website.domain.AuctionSource;
import com.veragg.website.domain.Court;
import com.veragg.website.repository.AuctionRepo;
import com.veragg.website.repository.DocumentAuctionRepo;

import static org.junit.Assert.assertThrows;

public class AuctionServiceImpl_when_findDraftByFileNumberCourtSource_is_called {

    AuctionServiceImpl sut;

    @Mock
    AuctionRepo auctionRepo;

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

        sut = new AuctionServiceImpl(auctionRepo, documentAuctionRepo);
    }

    @Test
    public void given_auctionSource_is_null_then_NPE_thrown() {

        //Arrange
        //Act
        //Assert
        assertThrows(NullPointerException.class, () -> sut.findDraftByFileNumberCourtSource("", court, null));

    }

    @Test
    public void given_fileNumber_is_null_then_NPE_thrown() {

        //Arrange
        //Act
        //Assert
        assertThrows(NullPointerException.class, () -> sut.findDraftByFileNumberCourtSource(null, court, auctionSource));

    }

    @Test
    public void given_court_is_null_then_NPE_thrown() {

        //Arrange
        //Act
        //Assert
        assertThrows(NullPointerException.class, () -> sut.findDraftByFileNumberCourtSource("", null, auctionSource));

    }

    //TODO: fix tests

    //        public void given_valid_fileNumber_then_auction_should_return() {
    //        @Test
    //
    //            //Arrange
    //
    //            //Act
    //            Auction result = null;
    //
    //            //Assert
    //            assertNotNull(result);
    //            assertEquals(auctionMock, result);
    //
    //        }

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
