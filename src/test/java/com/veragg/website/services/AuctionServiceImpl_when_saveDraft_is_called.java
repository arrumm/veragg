package com.veragg.website.services;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.veragg.website.domain.Auction;
import com.veragg.website.domain.AuctionSource;
import com.veragg.website.domain.AuctionStatus;
import com.veragg.website.domain.Court;
import com.veragg.website.domain.Document;
import com.veragg.website.repository.AuctionRepo;
import com.veragg.website.repository.DocumentAuctionRepo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AuctionServiceImpl_when_saveDraft_is_called {

    AuctionServiceImpl sut;

    @Mock
    AuctionRepo auctionRepo;

    @Mock
    DocumentAuctionRepo documentAuctionRepo;

    @Mock
    Auction auction;

    @Mock
    Court court;

    @Mock
    AuctionSource source;

    @Mock
    Document document;

    @Before
    public void setup() {
        initMocks(this);
        when(auction.getFileNumber()).thenReturn("AZ number");
        when(auction.getCourt()).thenReturn(court);
        when(auction.getSource()).thenReturn(source);
        sut = new AuctionServiceImpl(auctionRepo, documentAuctionRepo);
    }

    @Test
    public void given_auction_not_exist_then_all_interactions_performed_and_saved_auction_return() {

        //Arrange
        when(auction.getDocuments()).thenReturn(Arrays.asList(document, document));
        when(auctionRepo.findByFileNumberAndCourtAndSourceAndAuctionStatus(eq("AZ number"), eq(court), eq(source), eq(AuctionStatus.DRAFT))).thenReturn(null);
        when(auctionRepo.save(eq(auction))).thenReturn(auction);

        //Act
        Auction result = sut.saveDraft(auction);

        //Assert
        assertNotNull(result);
        assertEquals(auction, result);
        verify(auctionRepo).findByFileNumberAndCourtAndSourceAndAuctionStatus(anyString(), any(Court.class), any(AuctionSource.class), any(AuctionStatus.class));
        verify(auctionRepo).save(any(Auction.class));
        verify(documentAuctionRepo, times(2)).save(any(Document.class));

    }

    @Test
    public void given_auction_exist_then_no_save_is_called_and_found_auction_return() {

        //Arrange
        Auction auctionFound = mock(Auction.class);
        when(auction.getDocuments()).thenReturn(Arrays.asList(document, document));
        when(auctionRepo.findByFileNumberAndCourtAndSourceAndAuctionStatus(anyString(), any(), any(), any())).thenReturn(auctionFound);
        when(auctionRepo.save(eq(auctionFound))).thenReturn(auctionFound);

        //Act
        Auction result = sut.saveDraft(auction);

        //Assert
        assertNotNull(result);
        assertEquals(auctionFound, result);
        verify(auctionRepo).findByFileNumberAndCourtAndSourceAndAuctionStatus(anyString(), any(Court.class), any(AuctionSource.class), any(AuctionStatus.class));
        verifyNoMoreInteractions(auctionRepo);
        verifyNoMoreInteractions(documentAuctionRepo);

    }

    @Test
    public void given_document_in_list_is_null_then_not_saved() {

        //Arrange
        when(auction.getDocuments()).thenReturn(Arrays.asList(document, null, document, null, document));
        when(auctionRepo.findByFileNumberAndCourtAndSourceAndAuctionStatus(anyString(), any(), any(), any())).thenReturn(null);
        when(auctionRepo.save(eq(auction))).thenReturn(auction);

        //Act
        sut.saveDraft(auction);

        //Assert
        verify(documentAuctionRepo, times(3)).save(any(Document.class));

    }

    @Test
    public void given_fileNumber_is_null_then_NPE_expected() {

        //Arrange
        when(auction.getFileNumber()).thenReturn(null);
        //Act

        //Assert
        assertThrows(NullPointerException.class, () -> sut.saveDraft(auction));

    }

    @Test
    public void given_court_is_null_then_NPE_expected() {

        //Arrange
        when(auction.getCourt()).thenReturn(null);
        //Act

        //Assert
        assertThrows(NullPointerException.class, () -> sut.saveDraft(auction));

    }

    @Test
    public void given_source_is_null_then_NPE_expected() {

        //Arrange
        when(auction.getSource()).thenReturn(null);
        //Act

        //Assert
        assertThrows(NullPointerException.class, () -> sut.saveDraft(auction));

    }

}
