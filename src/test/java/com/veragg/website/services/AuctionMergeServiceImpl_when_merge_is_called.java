package com.veragg.website.services;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.veragg.website.domain.Auction;
import com.veragg.website.domain.AuctionStatus;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Auction.class)
public class AuctionMergeServiceImpl_when_merge_is_called {

    @Mock
    AuctionServiceImpl auctionService;

    @Mock
    Auction auction;

    AuctionMergeService sut;

    @Before
    public void setUp() {
        sut = new AuctionMergeServiceImpl(auctionService);
    }

    @Test
    public void given_draft_list_is_null_then_NPE_expected() {

        //Arrange
        //Act
        //Assert
        assertThrows(NullPointerException.class, () -> sut.merge(null));

    }

    @Test
    public void given_draft_list_is_empty_then_no_calls_expected_and_empty_list_returned() {

        //Arrange
        //Act
        List<Auction> resultList = sut.merge(Collections.emptyList());

        //Assert
        assertTrue(resultList.isEmpty());
        verifyNoInteractions(auctionService);

    }

    @Test
    public void given_draft_list_is_not_empty_then_saveAll_with_list_active_auctions_called_and_list_returned() {

        //Arrange
        ArgumentCaptor<List<Auction>> auctionListArgumentCaptor = ArgumentCaptor.forClass(List.class);
        List<Auction> auctionDrafts = Arrays.asList(auction, auction);
        when(auctionService.saveAll(eq(auctionDrafts))).thenReturn(auctionDrafts);
        doCallRealMethod().when(auction).setAuctionStatus(any(AuctionStatus.class));
        doCallRealMethod().when(auction).getAuctionStatus();

        //Act
        List<Auction> resultList = sut.merge(auctionDrafts);

        //Assert
        assertFalse(resultList.isEmpty());
        assertEquals(2, resultList.size());

        verify(auctionService).saveAll(auctionListArgumentCaptor.capture());
        List<Auction> auctionListArgument = auctionListArgumentCaptor.getValue();
        assertEquals(2, auctionListArgument.size());
        assertEquals(AuctionStatus.ACTIVE, auctionListArgument.get(0).getAuctionStatus());
        assertEquals(AuctionStatus.ACTIVE, auctionListArgument.get(1).getAuctionStatus());

    }

    @Test
    public void given_saveAll_throw_NPE_then_NPE_expected() {

        //Arrange
        when(auctionService.saveAll(anyList())).thenThrow(NullPointerException.class);

        //Act
        //Assert
        assertThrows(NullPointerException.class, () -> sut.merge(Collections.singletonList(auction)));

    }

    @Test
    public void given_auction_list_then_return_auction_list_and_saveAll_is_called() {

        //Arrange
        List<Auction> auctionList = Arrays.asList(auction, auction);
        when(auctionService.saveAll(eq(auctionList))).thenReturn(auctionList);

        //Act
        List<Auction> resultList = sut.merge(auctionList);

        //Assert
        assertThat(resultList, hasItem(auction));
        verify(auctionService).saveAll(eq(auctionList));
    }

}
