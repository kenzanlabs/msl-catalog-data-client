package com.kenzan.msl.catalog.client.services;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.google.common.base.Optional;
import com.kenzan.msl.catalog.client.TestConstants;
import com.kenzan.msl.catalog.client.cassandra.QueryAccessor;
import com.kenzan.msl.catalog.client.cassandra.query.AlbumsQuery;
import com.kenzan.msl.catalog.client.cassandra.query.ArtistsQuery;
import com.kenzan.msl.catalog.client.cassandra.query.PaginationQuery;
import com.kenzan.msl.catalog.client.cassandra.query.SongsQuery;
import com.kenzan.msl.catalog.client.dto.FeaturedAlbumsDto;
import com.kenzan.msl.catalog.client.dto.AlbumsByFacetDto;
import com.kenzan.msl.catalog.client.dto.AlbumArtistBySongDto;
import com.kenzan.msl.catalog.client.dto.FeaturedArtistsDto;
import com.kenzan.msl.catalog.client.dto.FeaturedSongsDto;
import com.kenzan.msl.catalog.client.dto.SongsByFacetDto;
import com.kenzan.msl.catalog.client.dto.ArtistsByFacetDto;
import com.kenzan.msl.catalog.client.dto.SongsAlbumsByArtistDto;
import com.kenzan.msl.catalog.client.dto.SongsArtistByAlbumDto;
import com.kenzan.msl.catalog.client.dto.PagingStateDto;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import rx.Observable;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CatalogDataClientServiceImpl.class, AlbumsQuery.class, SongsQuery.class,
    ArtistsQuery.class, PaginationQuery.class, Session.class, Cluster.class, MappingManager.class})
public class CassandraCatalogDataClientServiceTest {

  private TestConstants tc = TestConstants.getInstance();
  private CatalogDataClientServiceImpl cassandraCatalogServiceImpl;
  private ResultSet resultSet;
  private Observable<ResultSet> observableResultSet;
  private MappingManager manager;

  @Before
  public void init() throws Exception {
    resultSet = createMock(ResultSet.class);
    observableResultSet = Observable.just(resultSet);
    manager = PowerMockito.mock(MappingManager.class);

    Mapper<FeaturedAlbumsDto> myFeaturedAlbumsMapper = PowerMockito.mock(Mapper.class);
    PowerMockito.when(manager.mapper(FeaturedAlbumsDto.class)).thenReturn(myFeaturedAlbumsMapper);
    PowerMockito.when(myFeaturedAlbumsMapper.map(resultSet)).thenReturn(null);

    Mapper<AlbumsByFacetDto> myAlbumsByFacetMapper = PowerMockito.mock(Mapper.class);
    PowerMockito.when(manager.mapper(AlbumsByFacetDto.class)).thenReturn(myAlbumsByFacetMapper);
    PowerMockito.when(myAlbumsByFacetMapper.map(resultSet)).thenReturn(null);

    Mapper<AlbumArtistBySongDto> myAlbumArtistBySongMapper = PowerMockito.mock(Mapper.class);
    PowerMockito.when(manager.mapper(AlbumArtistBySongDto.class)).thenReturn(
        myAlbumArtistBySongMapper);
    PowerMockito.when(myAlbumArtistBySongMapper.map(resultSet)).thenReturn(null);

    Mapper<FeaturedArtistsDto> myFeaturedArtistsMapper = PowerMockito.mock(Mapper.class);
    PowerMockito.when(manager.mapper(FeaturedArtistsDto.class)).thenReturn(myFeaturedArtistsMapper);
    PowerMockito.when(myFeaturedArtistsMapper.map(resultSet)).thenReturn(null);

    Mapper<FeaturedSongsDto> myFeaturedSongsMapper = PowerMockito.mock(Mapper.class);
    PowerMockito.when(manager.mapper(FeaturedSongsDto.class)).thenReturn(myFeaturedSongsMapper);
    PowerMockito.when(myFeaturedSongsMapper.map(resultSet)).thenReturn(null);

    Mapper<SongsByFacetDto> mySongsByFacetMapper = PowerMockito.mock(Mapper.class);
    PowerMockito.when(manager.mapper(SongsByFacetDto.class)).thenReturn(mySongsByFacetMapper);
    PowerMockito.when(mySongsByFacetMapper.map(resultSet)).thenReturn(null);

    Mapper<ArtistsByFacetDto> myArtistsByFacetMapper = PowerMockito.mock(Mapper.class);
    PowerMockito.when(manager.mapper(ArtistsByFacetDto.class)).thenReturn(myArtistsByFacetMapper);
    PowerMockito.when(myArtistsByFacetMapper.map(resultSet)).thenReturn(null);

    Mapper<SongsAlbumsByArtistDto> mySongsAlbumsByArtistMapper = PowerMockito.mock(Mapper.class);
    PowerMockito.when(manager.mapper(SongsAlbumsByArtistDto.class)).thenReturn(
        mySongsAlbumsByArtistMapper);
    PowerMockito.when(mySongsAlbumsByArtistMapper.map(resultSet)).thenReturn(null);

    Mapper<SongsArtistByAlbumDto> mySongsArtistByAlbumMapper = PowerMockito.mock(Mapper.class);
    PowerMockito.when(manager.mapper(SongsArtistByAlbumDto.class)).thenReturn(
        mySongsArtistByAlbumMapper);
    PowerMockito.when(mySongsArtistByAlbumMapper.map(resultSet)).thenReturn(null);

    PowerMockito.mockStatic(PaginationQuery.class);

    PowerMock.mockStatic(AlbumsQuery.class);
    PowerMock.mockStatic(ArtistsQuery.class);
    PowerMock.mockStatic(SongsQuery.class);
    cassandraCatalogServiceImpl = new CatalogDataClientServiceImpl(manager);
  }

  // ==========================================================================================================
  // PAGINATION
  // ==========================================================================================================

  @Test
  public void testGetPagingState() {
    PowerMockito.when(PaginationQuery.get(Mockito.anyObject(), Mockito.anyObject())).thenReturn(
        Optional.of(tc.PAGING_STATE));

    PowerMock.replayAll();

    Observable<PagingStateDto> results = cassandraCatalogServiceImpl.getPagingState(tc.PAGING_ID);
    assertEquals(results.toBlocking().first(), tc.PAGING_STATE);
  }

  @Test
  public void testGetNullPagingState() {
    PowerMockito.when(PaginationQuery.get(Mockito.anyObject(), Mockito.anyObject())).thenReturn(
        Optional.absent());

    PowerMock.replayAll();

    Observable<PagingStateDto> results = cassandraCatalogServiceImpl.getPagingState(tc.PAGING_ID);
    assertTrue(results.isEmpty().toBlocking().first());
  }

  @Test
  public void testAddOrUpdatePagingState() {
    PowerMock.replayAll();

    Observable<Void> results = cassandraCatalogServiceImpl.addOrUpdatePagingState(tc.PAGING_STATE);
    assertTrue(results.isEmpty().toBlocking().first());
  }

  @Test
  public void testDeletePagingState() {
    PowerMock.replayAll();

    Observable<Void> results = cassandraCatalogServiceImpl.deletePagingState(tc.PAGING_ID);
    assertTrue(results.isEmpty().toBlocking().first());
  }

  // ==========================================================================================================
  // ALBUMS
  // ==========================================================================================================

  @Test
  public void testGetFeaturedAlbums() {
    expect(
        AlbumsQuery.getAlbums(EasyMock.anyObject(QueryAccessor.class),
            EasyMock.eq(Optional.absent()), EasyMock.eq(Optional.of(tc.LIMIT)))).andReturn(
        resultSet);

    PowerMock.replayAll();

    Observable<ResultSet> result = cassandraCatalogServiceImpl.getFeaturedAlbums(Optional.of(tc.LIMIT));
    assertEquals(result.toBlocking().first(), resultSet);
  }

  @Test
  public void testMapFeaturedAlbums() {
    PowerMock.replayAll();

    Observable<Result<FeaturedAlbumsDto>> results =
        cassandraCatalogServiceImpl.mapFeaturedAlbums(observableResultSet);
    assertNull(results.toBlocking().first());
  }

  @Test
  public void testGetAlbumsByFacet() {
    expect(
        AlbumsQuery.getAlbums(EasyMock.anyObject(QueryAccessor.class),
            EasyMock.eq(Optional.of(tc.FACET)), EasyMock.eq(Optional.of(tc.LIMIT)))).andReturn(
        resultSet);

    PowerMock.replayAll();

    Observable<ResultSet> result =
        cassandraCatalogServiceImpl.getAlbumsByFacet(tc.FACET, Optional.of(tc.LIMIT));
    assertEquals(result.toBlocking().first(), resultSet);
  }

  @Test
  public void testMapAlbumsByFacet() {
    PowerMock.replayAll();

    Observable<Result<AlbumsByFacetDto>> results =
        cassandraCatalogServiceImpl.mapAlbumsByFacet(observableResultSet);
    assertNull(results.toBlocking().first());
  }

  @Test
  public void testGetAlbumArtistBySong() {
    expect(
        AlbumsQuery.getAlbumArtistBySong(EasyMock.anyObject(QueryAccessor.class),
            EasyMock.eq(tc.SONG_ID), EasyMock.eq(Optional.of(tc.LIMIT)))).andReturn(resultSet);

    PowerMock.replayAll();

    Observable<ResultSet> result =
        cassandraCatalogServiceImpl.getAlbumArtistBySong(tc.SONG_ID, Optional.of(tc.LIMIT));
    assertEquals(result.toBlocking().first(), resultSet);
  }

  @Test
  public void testMapAlbumArtistBySong() {
    PowerMock.replayAll();

    Observable<Result<AlbumArtistBySongDto>> results =
        cassandraCatalogServiceImpl.mapAlbumArtistBySong(observableResultSet);
    assertNull(results.toBlocking().first());
  }

  // =========================================================================================================
  // ARTISTS
  // =========================================================================================================

  @Test
  public void getFeaturedArtists() {
    expect(
        ArtistsQuery.getArtists(EasyMock.anyObject(QueryAccessor.class),
            EasyMock.eq(Optional.absent()), EasyMock.eq(Optional.of(tc.LIMIT)))).andReturn(
        resultSet);

    PowerMock.replayAll();

    Observable<ResultSet> result =
        cassandraCatalogServiceImpl.getFeaturedArtists(Optional.of(tc.LIMIT));
    assertEquals(result.toBlocking().first(), resultSet);
  }

  @Test
  public void testMapFeaturedArtists() {
    PowerMock.replayAll();

    Observable<Result<FeaturedArtistsDto>> results =
        cassandraCatalogServiceImpl.mapFeaturedArtists(observableResultSet);
    assertNull(results.toBlocking().first());
  }

  @Test
  public void testGetArtistsByFacet() {
    expect(
        ArtistsQuery.getArtists(EasyMock.anyObject(QueryAccessor.class),
            EasyMock.eq(Optional.of(tc.FACET)), EasyMock.eq(Optional.of(tc.LIMIT)))).andReturn(
        resultSet);

    PowerMock.replayAll();

    Observable<ResultSet> result =
        cassandraCatalogServiceImpl.getArtistsByFacet(tc.FACET, Optional.of(tc.LIMIT));
    assertEquals(result.toBlocking().first(), resultSet);
  }

  @Test
  public void testMapArtistsByFacet() {
    PowerMock.replayAll();

    Observable<Result<ArtistsByFacetDto>> results =
        cassandraCatalogServiceImpl.mapArtistByFacet(observableResultSet);
    assertNull(results.toBlocking().first());
  }

  // ===========================================================================================================
  // SONGS
  // ===========================================================================================================

  @Test
  public void testGetFeaturedSongs() {
    expect(
        SongsQuery.getSongs(EasyMock.anyObject(QueryAccessor.class),
            EasyMock.eq(Optional.absent()), EasyMock.eq(Optional.of(tc.LIMIT)))).andReturn(
        resultSet);

    PowerMock.replayAll();

    Observable<ResultSet> result = cassandraCatalogServiceImpl.getFeaturedSongs(Optional.of(tc.LIMIT));
    assertEquals(result.toBlocking().first(), resultSet);
  }

  @Test
  public void testMapFeaturedSongs() {
    PowerMock.replayAll();

    Observable<Result<FeaturedSongsDto>> results =
        cassandraCatalogServiceImpl.mapFeaturedSongs(observableResultSet);
    assertNull(results.toBlocking().first());
  }

  @Test
  public void testGetSongsByFacets() {
    expect(
        SongsQuery.getSongs(EasyMock.anyObject(QueryAccessor.class),
            EasyMock.eq(Optional.of(tc.FACET)), EasyMock.eq(Optional.of(tc.LIMIT)))).andReturn(
        resultSet);

    PowerMock.replayAll();
    Observable<ResultSet> result =
        cassandraCatalogServiceImpl.getSongsByFacets(tc.FACET, Optional.of(tc.LIMIT));
    assertEquals(result.toBlocking().first(), resultSet);
  }

  @Test
  public void testMapSongsByFacet() {
    PowerMock.replayAll();

    Observable<Result<SongsByFacetDto>> results =
        cassandraCatalogServiceImpl.mapSongsByFacet(observableResultSet);
    assertNull(results.toBlocking().first());
  }

  @Test
  public void testGetSongsAlbumsByArtist() {
    expect(
        SongsQuery.getSongsAlbumsByArtist(EasyMock.anyObject(QueryAccessor.class),
            EasyMock.eq(tc.ARTIST_ID), EasyMock.eq(Optional.of(tc.LIMIT)))).andReturn(resultSet);

    PowerMock.replayAll();

    Observable<ResultSet> result =
        cassandraCatalogServiceImpl.getSongsAlbumsByArtist(tc.ARTIST_ID, Optional.of(tc.LIMIT));
    assertEquals(result.toBlocking().first(), resultSet);
  }

  @Test
  public void testMapSongsAlbumsByArtist() {
    PowerMock.replayAll();

    Observable<Result<SongsAlbumsByArtistDto>> results =
        cassandraCatalogServiceImpl.mapSongsAlbumsByArtist(observableResultSet);
    assertNull(results.toBlocking().first());
  }

  @Test
  public void testGetSongsArtistByAlbum() {
    expect(
        SongsQuery.getSongsArtistByAlbum(EasyMock.anyObject(QueryAccessor.class),
            EasyMock.eq(tc.ALBUM_ID), EasyMock.eq(Optional.of(tc.LIMIT)))).andReturn(resultSet);

    PowerMock.replayAll();

    Observable<ResultSet> result =
        cassandraCatalogServiceImpl.getSongsArtistByAlbum(tc.ALBUM_ID, Optional.of(tc.LIMIT));
    assertEquals(result.toBlocking().first(), resultSet);
  }

  @Test
  public void testMapSongsArtistByAlbum() {
    PowerMock.replayAll();

    Observable<Result<SongsArtistByAlbumDto>> results =
        cassandraCatalogServiceImpl.mapSongsArtistByAlbum(observableResultSet);
    assertNull(results.toBlocking().first());
  }

}
