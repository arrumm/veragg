package com.veragg.website.services;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.veragg.website.domain.AuctionDraft;
import com.veragg.website.repository.AuctionDraftRepo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class AuctionDraftServiceImpl_when_findById_is_called {

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
    public void given_valid_id_then_draft_should_return() {

        //Arrange
        Optional<AuctionDraft> auctionDraftOptional = Optional.of(draftMock);
        when(auctionDraftRepo.findById(eq(1L))).thenReturn(auctionDraftOptional);

        //Act
        AuctionDraft result = auctionDraftService.findById(1L);

        //Assert
        assertNotNull(result);
        assertEquals(draftMock, result);

    }

    @Test
    public void given_id_with_no_draft_then_null_should_return() {

        //Arrange
        when(auctionDraftRepo.findById(eq(1L))).thenReturn(Optional.empty());

        //Act
        AuctionDraft result = auctionDraftService.findById(1L);

        //Assert
        assertNull(result);

    }

    @Test(expected = IllegalArgumentException.class)
    public void given_null_draft_id_to_find_then_throw_IAE() {

        //Arrange
        when(auctionDraftRepo.findById(anyLong())).thenThrow(new IllegalArgumentException());

        //Act
        auctionDraftService.findById(1L);

        //Assert

    }

}
