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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuctionDraftServiceImpl_when_save_is_called {

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
    public void given_draft_to_save_then_save_should_called() {

        //Arrange
        AuctionDraft draft = new AuctionDraft();
        when(auctionDraftRepo.save(any(AuctionDraft.class))).thenReturn(draftMock);

        //Act
        AuctionDraft result = auctionDraftService.save(draft);

        //Assert
        assertNotNull(result);
        assertEquals(draftMock, result);
        verify(auctionDraftRepo).save(eq(draft));

    }

    @Test(expected = IllegalArgumentException.class)
    public void given_null_draft_to_save_then_throw_IAE() {

        //Arrange
        when(auctionDraftRepo.save(any())).thenThrow(new IllegalArgumentException());

        //Act
        auctionDraftService.save(null);

        //Assert

    }

}
