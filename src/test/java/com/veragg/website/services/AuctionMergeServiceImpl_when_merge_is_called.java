package com.veragg.website.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.veragg.website.domain.Auction;
import com.veragg.website.domain.AuctionDraft;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Auction.class)
public class AuctionMergeServiceImpl_when_merge_is_called {

    @Mock
    AuctionServiceImpl auctionService;

    @Mock
    AuctionMapper auctionMapper;

    @Mock
    AuctionDraft draft;

    @Mock
    Auction auction;

    AuctionMergeService sut;

    @Before
    public void setUp() {
        sut = new AuctionMergeServiceImpl(auctionService, auctionMapper);
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
        when(auctionMapper.getAuction(draft)).thenReturn(auction);
        when(auctionService.save(auction)).thenThrow(NullPointerException.class);

        //Act
        //Assert
        assertThrows(NullPointerException.class, () -> sut.merge(draft));

    }

    @Test
    public void given_auction_mapped_then_return_mapped_auction_and_save_is_called() {

        //Arrange
        when(auctionMapper.getAuction(draft)).thenReturn(auction);
        when(auctionService.save(auction)).thenReturn(auction);

        //Act
        Auction resultAuction = sut.merge(draft);

        //Assert
        assertEquals(auction, resultAuction);
        verify(auctionService).save(eq(auction));
    }

}