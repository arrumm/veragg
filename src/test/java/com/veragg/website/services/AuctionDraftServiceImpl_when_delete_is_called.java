package com.veragg.website.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.veragg.website.domain.AuctionDraft;
import com.veragg.website.repository.AuctionDraftRepo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

public class AuctionDraftServiceImpl_when_delete_is_called {

    AuctionDraftServiceImpl auctionDraftService;

    @Mock
    AuctionDraftRepo auctionDraftRepo;

    @Mock
    AuctionDraft draftMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        auctionDraftService = new AuctionDraftServiceImpl(auctionDraftRepo);
    }

    @Test
    public void given_draft_to_delete_then_delete_should_called() {

        //Arrange
        doNothing().when(auctionDraftRepo).delete(eq(draftMock));

        //Act
        auctionDraftService.delete(draftMock);

        //Assert
        verify(auctionDraftRepo).delete(eq(draftMock));

    }

    @Test(expected = IllegalArgumentException.class)
    public void given_null_draft_to_delete_then_throw_IAE() {

        //Arrange
        doThrow(new IllegalArgumentException()).when(auctionDraftRepo).delete(any());

        //Act
        auctionDraftService.delete(null);

        //Assert

    }
}
