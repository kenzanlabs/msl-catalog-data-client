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
@PrepareForTest({
    CassandraCatalogService.class,
    AlbumsQuery.class,
    SongsQuery.class,
    ArtistsQuery.class,
    PaginationQuery.class,
    Session.class,
    Cluster.class,
    MappingManager.class })
public class CassandraCatalogServiceTest {

    private TestConstants tc = TestConstants.getInstance();
    private CassandraCatalogService cassandraCatalogService;
    private ResultSet resultSet;
    private Observable<ResultSet> observableResultSet;
    private MappingManager manager;

    @Before
    public void init()
        throws Exception {
        resultSet = createMock(ResultSet.class);
        observableResultSet = Observable.just(resultSet);

        Session session = PowerMock.createMock(Session.class);
        Cluster cluster = PowerMock.createMock(Cluster.class);
        Cluster.Builder builder = PowerMock.createMock(Cluster.Builder.class);

        PowerMock.mockStatic(Cluster.class);
        EasyMock.expect(Cluster.builder()).andReturn(builder);
        EasyMock.expect(builder.addContactPoint(EasyMock.anyString())).andReturn(builder);
        EasyMock.expect(builder.build()).andReturn(cluster);
        EasyMock.expect(cluster.connect(EasyMock.anyString())).andReturn(session);

        manager = PowerMockito.mock(MappingManager.class);
        PowerMockito.whenNew(MappingManager.class).withAnyArguments().thenReturn(manager);

        Mapper<FeaturedAlbumsDto> myFeaturedAlbumsMapper = PowerMockito.mock(Mapper.class);
        PowerMockito.when(manager.mapper(FeaturedAlbumsDto.class)).thenReturn(myFeaturedAlbumsMapper);
        PowerMockito.when(myFeaturedAlbumsMapper.map(resultSet)).thenReturn(null);

        Mapper<AlbumsByFacetDto> myAlbumsByFacetMapper = PowerMockito.mock(Mapper.class);
        PowerMockito.when(manager.mapper(AlbumsByFacetDto.class)).thenReturn(myAlbumsByFacetMapper);
        PowerMockito.when(myAlbumsByFacetMapper.map(resultSet)).thenReturn(null);

        Mapper<AlbumArtistBySongDto> myAlbumArtistBySongMapper = PowerMockito.mock(Mapper.class);
        PowerMockito.when(manager.mapper(AlbumArtistBySongDto.class)).thenReturn(myAlbumArtistBySongMapper);
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
        PowerMockito.when(manager.mapper(SongsAlbumsByArtistDto.class)).thenReturn(mySongsAlbumsByArtistMapper);
        PowerMockito.when(mySongsAlbumsByArtistMapper.map(resultSet)).thenReturn(null);

        Mapper<SongsArtistByAlbumDto> mySongsArtistByAlbumMapper = PowerMockito.mock(Mapper.class);
        PowerMockito.when(manager.mapper(SongsArtistByAlbumDto.class)).thenReturn(mySongsArtistByAlbumMapper);
        PowerMockito.when(mySongsArtistByAlbumMapper.map(resultSet)).thenReturn(null);

        PowerMockito.mockStatic(PaginationQuery.class);

        PowerMock.mockStatic(AlbumsQuery.class);
        PowerMock.mockStatic(ArtistsQuery.class);
        PowerMock.mockStatic(SongsQuery.class);
    }

    // ==========================================================================================================
    // PAGINATION
    // ==========================================================================================================

    @Test
    public void testGetPagingState() {
        PowerMockito.when(PaginationQuery.get(Mockito.anyObject(), Mockito.anyObject()))
            .thenReturn(Optional.of(tc.PAGING_STATE));

        PowerMock.replayAll();

        cassandraCatalogService = CassandraCatalogService.getInstance();
        Observable<PagingStateDto> results = cassandraCatalogService.getPagingState(tc.PAGING_ID);
        assertEquals(results.toBlocking().first(), tc.PAGING_STATE);
    }

    @Test
    public void testGetNullPagingState() {
        PowerMockito.when(PaginationQuery.get(Mockito.anyObject(), Mockito.anyObject())).thenReturn(Optional.absent());

        PowerMock.replayAll();

        cassandraCatalogService = CassandraCatalogService.getInstance();
        Observable<PagingStateDto> results = cassandraCatalogService.getPagingState(tc.PAGING_ID);
        assertTrue(results.isEmpty().toBlocking().first());
    }

    @Test
    public void testAddOrUpdatePagingState() {
        PowerMock.replayAll();

        cassandraCatalogService = CassandraCatalogService.getInstance();
        Observable<Void> results = cassandraCatalogService.addOrUpdatePagingState(tc.PAGING_STATE);
        assertTrue(results.isEmpty().toBlocking().first());
    }

    @Test
    public void testDeletePagingState() {
        PowerMock.replayAll();

        cassandraCatalogService = CassandraCatalogService.getInstance();
        Observable<Void> results = cassandraCatalogService.deletePagingState(tc.PAGING_ID);
        assertTrue(results.isEmpty().toBlocking().first());
    }

    // ==========================================================================================================
    // ALBUMS
    // ==========================================================================================================

    @Test
    public void testGetFeaturedAlbums() {
        expect(
               AlbumsQuery.getAlbums(EasyMock.anyObject(QueryAccessor.class), EasyMock.eq(Optional.absent()),
                                     EasyMock.eq(Optional.of(tc.LIMIT)))).andReturn(resultSet);

        PowerMock.replayAll();

        cassandraCatalogService = CassandraCatalogService.getInstance();

        Observable<ResultSet> result = cassandraCatalogService.getFeaturedAlbums(Optional.of(tc.LIMIT));
        assertEquals(result.toBlocking().first(), resultSet);
    }

    @Test
    public void testMapFeaturedAlbums() {
        PowerMock.replayAll();

        cassandraCatalogService = CassandraCatalogService.getInstance();
        Observable<Result<FeaturedAlbumsDto>> results = cassandraCatalogService.mapFeaturedAlbums(observableResultSet);
        assertNull(results.toBlocking().first());
    }

    @Test
    public void testGetAlbumsByFacet() {
        expect(
               AlbumsQuery.getAlbums(EasyMock.anyObject(QueryAccessor.class), EasyMock.eq(Optional.of(tc.FACET)),
                                     EasyMock.eq(Optional.of(tc.LIMIT)))).andReturn(resultSet);

        PowerMock.replayAll();

        cassandraCatalogService = CassandraCatalogService.getInstance();
        Observable<ResultSet> result = cassandraCatalogService.getAlbumsByFacet(tc.FACET, Optional.of(tc.LIMIT));
        assertEquals(result.toBlocking().first(), resultSet);
    }

    @Test
    public void testMapAlbumsByFacet() {
        PowerMock.replayAll();

        cassandraCatalogService = CassandraCatalogService.getInstance();
        Observable<Result<AlbumsByFacetDto>> results = cassandraCatalogService.mapAlbumsByFacet(observableResultSet);
        assertNull(results.toBlocking().first());
    }

    @Test
    public void testGetAlbumArtistBySong() {
        expect(
               AlbumsQuery.getAlbumArtistBySong(EasyMock.anyObject(QueryAccessor.class), EasyMock.eq(tc.SONG_ID),
                                                EasyMock.eq(Optional.of(tc.LIMIT)))).andReturn(resultSet);

        PowerMock.replayAll();

        cassandraCatalogService = CassandraCatalogService.getInstance();

        Observable<ResultSet> result = cassandraCatalogService.getAlbumArtistBySong(tc.SONG_ID, Optional.of(tc.LIMIT));
        assertEquals(result.toBlocking().first(), resultSet);
    }

    @Test
    public void testMapAlbumArtistBySong() {
        PowerMock.replayAll();

        cassandraCatalogService = CassandraCatalogService.getInstance();
        Observable<Result<AlbumArtistBySongDto>> results = cassandraCatalogService
            .mapAlbumArtistBySong(observableResultSet);
        assertNull(results.toBlocking().first());
    }

    // =========================================================================================================
    // ARTISTS
    // =========================================================================================================

    @Test
    public void getFeaturedArtists() {
        expect(
               ArtistsQuery.getArtists(EasyMock.anyObject(QueryAccessor.class), EasyMock.eq(Optional.absent()),
                                       EasyMock.eq(Optional.of(tc.LIMIT)))).andReturn(resultSet);

        PowerMock.replayAll();

        cassandraCatalogService = CassandraCatalogService.getInstance();

        Observable<ResultSet> result = cassandraCatalogService.getFeaturedArtists(Optional.of(tc.LIMIT));
        assertEquals(result.toBlocking().first(), resultSet);
    }

    @Test
    public void testMapFeaturedArtists() {
        PowerMock.replayAll();

        cassandraCatalogService = CassandraCatalogService.getInstance();
        Observable<Result<FeaturedArtistsDto>> results = cassandraCatalogService
            .mapFeaturedArtists(observableResultSet);
        assertNull(results.toBlocking().first());
    }

    @Test
    public void testGetArtistsByFacet() {
        expect(
               ArtistsQuery.getArtists(EasyMock.anyObject(QueryAccessor.class), EasyMock.eq(Optional.of(tc.FACET)),
                                       EasyMock.eq(Optional.of(tc.LIMIT)))).andReturn(resultSet);

        PowerMock.replayAll();

        cassandraCatalogService = CassandraCatalogService.getInstance();

        Observable<ResultSet> result = cassandraCatalogService.getArtistsByFacet(tc.FACET, Optional.of(tc.LIMIT));
        assertEquals(result.toBlocking().first(), resultSet);
    }

    @Test
    public void testMapArtistsByFacet() {
        PowerMock.replayAll();

        cassandraCatalogService = CassandraCatalogService.getInstance();
        Observable<Result<ArtistsByFacetDto>> results = cassandraCatalogService.mapArtistByFacet(observableResultSet);
        assertNull(results.toBlocking().first());
    }

    // ===========================================================================================================
    // SONGS
    // ===========================================================================================================

    @Test
    public void testGetFeaturedSongs() {
        expect(
               SongsQuery.getSongs(EasyMock.anyObject(QueryAccessor.class), EasyMock.eq(Optional.absent()),
                                   EasyMock.eq(Optional.of(tc.LIMIT)))).andReturn(resultSet);

        PowerMock.replayAll();

        cassandraCatalogService = CassandraCatalogService.getInstance();

        Observable<ResultSet> result = cassandraCatalogService.getFeaturedSongs(Optional.of(tc.LIMIT));
        assertEquals(result.toBlocking().first(), resultSet);
    }

    @Test
    public void testMapFeaturedSongs() {
        PowerMock.replayAll();

        cassandraCatalogService = CassandraCatalogService.getInstance();
        Observable<Result<FeaturedSongsDto>> results = cassandraCatalogService.mapFeaturedSongs(observableResultSet);
        assertNull(results.toBlocking().first());
    }

    @Test
    public void testGetSongsByFacets() {
        expect(
               SongsQuery.getSongs(EasyMock.anyObject(QueryAccessor.class), EasyMock.eq(Optional.of(tc.FACET)),
                                   EasyMock.eq(Optional.of(tc.LIMIT)))).andReturn(resultSet);

        PowerMock.replayAll();

        cassandraCatalogService = CassandraCatalogService.getInstance();

        Observable<ResultSet> result = cassandraCatalogService.getSongsByFacets(tc.FACET, Optional.of(tc.LIMIT));
        assertEquals(result.toBlocking().first(), resultSet);
    }

    @Test
    public void testMapSongsByFacet() {
        PowerMock.replayAll();

        cassandraCatalogService = CassandraCatalogService.getInstance();
        Observable<Result<SongsByFacetDto>> results = cassandraCatalogService.mapSongsByFacet(observableResultSet);
        assertNull(results.toBlocking().first());
    }

    @Test
    public void testGetSongsAlbumsByArtist() {
        expect(
               SongsQuery.getSongsAlbumsByArtist(EasyMock.anyObject(QueryAccessor.class), EasyMock.eq(tc.ARTIST_ID),
                                                 EasyMock.eq(Optional.of(tc.LIMIT)))).andReturn(resultSet);

        PowerMock.replayAll();

        cassandraCatalogService = CassandraCatalogService.getInstance();

        Observable<ResultSet> result = cassandraCatalogService.getSongsAlbumsByArtist(tc.ARTIST_ID,
                                                                                      Optional.of(tc.LIMIT));
        assertEquals(result.toBlocking().first(), resultSet);
    }

    @Test
    public void testMapSongsAlbumsByArtist() {
        PowerMock.replayAll();

        cassandraCatalogService = CassandraCatalogService.getInstance();
        Observable<Result<SongsAlbumsByArtistDto>> results = cassandraCatalogService
            .mapSongsAlbumsByArtist(observableResultSet);
        assertNull(results.toBlocking().first());
    }

    @Test
    public void testGetSongsArtistByAlbum() {
        expect(
               SongsQuery.getSongsArtistByAlbum(EasyMock.anyObject(QueryAccessor.class), EasyMock.eq(tc.ALBUM_ID),
                                                EasyMock.eq(Optional.of(tc.LIMIT)))).andReturn(resultSet);

        PowerMock.replayAll();

        cassandraCatalogService = CassandraCatalogService.getInstance();

        Observable<ResultSet> result = cassandraCatalogService
            .getSongsArtistByAlbum(tc.ALBUM_ID, Optional.of(tc.LIMIT));
        assertEquals(result.toBlocking().first(), resultSet);
    }

    @Test
    public void testMapSongsArtistByAlbum() {
        PowerMock.replayAll();

        cassandraCatalogService = CassandraCatalogService.getInstance();
        Observable<Result<SongsArtistByAlbumDto>> results = cassandraCatalogService
            .mapSongsArtistByAlbum(observableResultSet);
        assertNull(results.toBlocking().first());
    }

}
