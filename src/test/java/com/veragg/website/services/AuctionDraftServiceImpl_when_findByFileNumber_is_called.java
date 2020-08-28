package com.veragg.website.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.veragg.website.domain.AuctionDraft;
import com.veragg.website.repository.AuctionDraftRepo;
import com.veragg.website.repository.DocumentAuctionDraftRepo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class AuctionDraftServiceImpl_when_findByFileNumber_is_called {

    AuctionDraftServiceImpl auctionDraftService;

    @Mock
    AuctionDraftRepo auctionDraftRepo;

    @Mock
    DocumentAuctionDraftRepo documentAuctionRepo;

    @Mock
    AuctionDraft draftMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        auctionDraftService = new AuctionDraftServiceImpl(auctionDraftRepo, documentAuctionRepo);
    }

    @Test
    public void given_valid_fileNumber_then_draft_should_return() {

        //Arrange
        when(auctionDraftRepo.findByFileNumber(eq("1K 12"))).thenReturn(draftMock);

        //Act
        AuctionDraft result = auctionDraftService.findByFileNumber("1K 12");

        //Assert
        assertNotNull(result);
        assertEquals(draftMock, result);

    }

    @Test
    public void given_fileNumber_with_no_draft_then_null_should_return() {

        //Arrange
        when(auctionDraftRepo.findByFileNumber(eq("1K 12"))).thenReturn(null);

        //Act
        AuctionDraft result = auctionDraftService.findByFileNumber("1K 12");

        //Assert
        assertNull(result);

    }

    @Test(expected = IllegalArgumentException.class)
    public void given_null_fileNumber_to_find_then_throw_IAE() {

        //Arrange
        when(auctionDraftRepo.findByFileNumber(anyString())).thenThrow(new IllegalArgumentException());

        //Act
        auctionDraftService.findByFileNumber("1K 12");

        //Assert

    }

}
