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

public class AlbumsQueryTest {

  private TestConstants tc = TestConstants.getInstance();

  @Mock
  private QueryAccessor queryAccessor;

  @Before
  public void init() {
    queryAccessor = mock(QueryAccessor.class);
  }

  @Test
  public void testGetAlbumsWithLimitAndFacet() {
    AlbumsQuery.getAlbums(queryAccessor, Optional.of(tc.FACET), Optional.of(tc.LIMIT));
    verify(queryAccessor, atLeastOnce()).getAlbumsByFacetsWithLimit(tc.FACET, tc.LIMIT);
  }

  @Test
  public void testGetAlbumsWithFacet() {
    AlbumsQuery.getAlbums(queryAccessor, Optional.of(tc.FACET), Optional.absent());
    verify(queryAccessor, atLeastOnce()).getAlbumsByFacets(tc.FACET);
  }

  @Test
  public void testGetAlbumsWithLimit() {
    AlbumsQuery.getAlbums(queryAccessor, Optional.absent(), Optional.of(tc.LIMIT));
    verify(queryAccessor, atLeastOnce()).getAlbumsWithLimit(tc.LIMIT);
  }

  @Test
  public void testGetAlbums() {
    AlbumsQuery.getAlbums(queryAccessor, Optional.absent(), Optional.absent());
    verify(queryAccessor, atLeastOnce()).getAlbums();
  }

  @Test
  public void testGetAlbumArtistBySongWithLimit() {
    AlbumsQuery.getAlbumArtistBySong(queryAccessor, tc.SONG_ID, Optional.of(tc.LIMIT));
    verify(queryAccessor, atLeastOnce()).albumArtistBySongWithLimit(tc.SONG_ID, tc.LIMIT);
  }

  @Test
  public void testGetAlbumArtistBySong() {
    AlbumsQuery.getAlbumArtistBySong(queryAccessor, tc.SONG_ID, Optional.absent());
    verify(queryAccessor, atLeastOnce()).albumArtistBySong(tc.SONG_ID);
  }

}
