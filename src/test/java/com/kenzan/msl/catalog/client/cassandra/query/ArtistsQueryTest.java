package com.kenzan.msl.catalog.client.cassandra.query;

import com.google.common.base.Optional;
import com.kenzan.msl.catalog.client.TestConstants;
import com.kenzan.msl.catalog.client.cassandra.QueryAccessor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.atLeastOnce;

public class ArtistsQueryTest {

  private TestConstants tc = TestConstants.getInstance();

  @Mock
  private QueryAccessor queryAccessor;

  @Before
  public void init() {
    queryAccessor = mock(QueryAccessor.class);
  }

  @Test
  public void testGetArtistsWithLimitAndFacet() {
    ArtistsQuery.getArtists(queryAccessor, Optional.of(tc.FACET), Optional.of(tc.LIMIT));
    verify(queryAccessor, atLeastOnce()).getArtistsByFacetsWithLimit(tc.FACET, tc.LIMIT);
  }

  @Test
  public void testGetArtistsWithFacet() {
    ArtistsQuery.getArtists(queryAccessor, Optional.of(tc.FACET), Optional.absent());
    verify(queryAccessor, atLeastOnce()).getArtistsByFacets(tc.FACET);
  }

  @Test
  public void testGetArtistsWithLimit() {
    ArtistsQuery.getArtists(queryAccessor, Optional.absent(), Optional.of(tc.LIMIT));
    verify(queryAccessor, atLeastOnce()).getArtistsWithLimit(tc.LIMIT);
  }

  @Test
  public void testGetArtists() {
    ArtistsQuery.getArtists(queryAccessor, Optional.absent(), Optional.absent());
    verify(queryAccessor, atLeastOnce()).getArtists();
  }
}
