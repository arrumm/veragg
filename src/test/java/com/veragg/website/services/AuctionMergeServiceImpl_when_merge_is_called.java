package com.veragg.website.services;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.veragg.website.domain.Auction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Auction.class)
public class AuctionMergeServiceImpl_when_merge_is_called {

    @Mock
    AuctionServiceImpl auctionService;

    @Mock
    AuctionServiceImpl auctionDraftService;

    @Mock
    Auction draft;

    @Mock
    Auction auction;

    AuctionMergeService sut;

    @Before
    public void setUp() {
        sut = new AuctionMergeServiceImpl(auctionService);
    }

    @Test
    public void given_draft_is_null_then_NPE_expected() {

        //Arrange
        //Act
        //Assert
        assertThrows(NullPointerException.class, () -> sut.merge(null));

    }

    @Test
    public void given_save_throw_NPE_then_NPE_expected() {

        //Arrange
        when(auctionService.saveDraft(auction)).thenThrow(NullPointerException.class);

        //Act
        //Assert
        assertThrows(NullPointerException.class, () -> sut.merge(Collections.singletonList(draft)));

    }

    @Test
    public void given_delete_throw_NPE_then_NPE_expected() {

        //Arrange
        doThrow(NullPointerException.class).when(auctionDraftService).delete(draft);

        //Act
        //Assert
        assertThrows(NullPointerException.class, () -> sut.merge(Collections.singletonList(draft)));

    }

    @Test
    public void given_auction_mapped_then_return_mapped_auction_and_save_and_delete_is_called() {

        //Arrange
        when(auctionService.saveDraft(auction)).thenReturn(auction);

        //Act
        List<Auction> resultAuction = sut.merge(Collections.singletonList(draft));

        //Assert
        assertEquals(auction, resultAuction);
        verify(auctionService).saveDraft(eq(auction));
    }

}
