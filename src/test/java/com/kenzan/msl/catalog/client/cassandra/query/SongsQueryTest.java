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

public class SongsQueryTest {

  private TestConstants tc = TestConstants.getInstance();

  @Mock
  private QueryAccessor queryAccessor;

  @Before
  public void init() {
    queryAccessor = mock(QueryAccessor.class);
  }

  @Test
  public void testGetSongsWithLimitAndFacet() {
    SongsQuery.getSongs(queryAccessor, Optional.of(tc.FACET), Optional.of(tc.LIMIT));
    verify(queryAccessor, atLeastOnce()).getSongsByFacetsWithLimit(tc.FACET, tc.LIMIT);
  }

  @Test
  public void testGetSongsWithFacet() {
    SongsQuery.getSongs(queryAccessor, Optional.of(tc.FACET), Optional.absent());
    verify(queryAccessor, atLeastOnce()).getSongsByFacets(tc.FACET);
  }

  @Test
  public void testGetSongsWithLimit() {
    SongsQuery.getSongs(queryAccessor, Optional.absent(), Optional.of(tc.LIMIT));
    verify(queryAccessor, atLeastOnce()).getSongsWithLimit(tc.LIMIT);
  }

  @Test
  public void testGetSongs() {
    SongsQuery.getSongs(queryAccessor, Optional.absent(), Optional.absent());
    verify(queryAccessor, atLeastOnce()).getSongs();
  }

  @Test
  public void testGetSongsAlbumsByArtistWithLimit() {
    SongsQuery.getSongsAlbumsByArtist(queryAccessor, tc.ARTIST_ID, Optional.of(tc.LIMIT));
    verify(queryAccessor, atLeastOnce()).songsAlbumsByArtistWithLimit(tc.ARTIST_ID, tc.LIMIT);
  }

  @Test
  public void testGetSongsAlbumsByArtist() {
    SongsQuery.getSongsAlbumsByArtist(queryAccessor, tc.ARTIST_ID, Optional.absent());
    verify(queryAccessor, atLeastOnce()).songsAlbumsByArtist(tc.ARTIST_ID);
  }

  @Test
  public void testGetSongsArtistByAlbumWithLimit() {
    SongsQuery.getSongsArtistByAlbum(queryAccessor, tc.ALBUM_ID, Optional.of(tc.LIMIT));
    verify(queryAccessor, atLeastOnce()).songsArtistByAlbumWithLimit(tc.ALBUM_ID, tc.LIMIT);
  }

  @Test
  public void testGetSongsArtistByAlbum() {
    SongsQuery.getSongsArtistByAlbum(queryAccessor, tc.ALBUM_ID, Optional.absent());
    verify(queryAccessor, atLeastOnce()).songsArtistByAlbum(tc.ALBUM_ID);
  }
}
