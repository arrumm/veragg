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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class AuctionServiceImpl_when_findDraftByFileNumberCourtSource_is_called {

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
    public void given_auctionSource_is_null_then_NPE_thrown() {

        //Arrange
        //Act
        //Assert
        assertThrows(NullPointerException.class, () -> sut.findDraftBy("", court, null));

    }

    @Test
    public void given_fileNumber_is_null_then_NPE_thrown() {

        //Arrange
        //Act
        //Assert
        assertThrows(NullPointerException.class, () -> sut.findDraftBy(null, court, auctionSource));

    }

    @Test
    public void given_court_is_null_then_NPE_thrown() {

        //Arrange
        //Act
        //Assert
        assertThrows(NullPointerException.class, () -> sut.findDraftBy("", null, auctionSource));

    }

    @Test
    public void given_valid_fileNumber_and_court_and_source_then_auction_draft_should_return() {

        //Arrange
        when(auctionRepo.findByFileNumberAndCourtAndSourceAndAuctionStatus(eq("1K 12"), eq(court), eq(auctionSource), eq(AuctionStatus.DRAFT))).thenReturn(auctionMock);

        //Act
        Auction result = sut.findDraftBy("1K 12", court, auctionSource);

        //Assert
        assertNotNull(result);
        assertEquals(auctionMock, result);

    }

    @Test
    public void given_fileNumber_with_no_auction_then_null_should_return() {

        //Arrange
        when(auctionRepo.findByFileNumberAndCourtAndSourceAndAuctionStatus(eq("1K 12"), eq(court), eq(auctionSource), eq(AuctionStatus.DRAFT))).thenReturn(null);

        //Act
        Auction result = sut.findDraftBy("1K 12", court, auctionSource);

        //Assert
        assertNull(result);

    }

}
