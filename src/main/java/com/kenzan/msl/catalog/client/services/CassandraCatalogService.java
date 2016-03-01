/*
 * Copyright 2015, Kenzan, All rights reserved.
 */
package com.kenzan.msl.catalog.client.services;

import com.kenzan.msl.catalog.client.archaius.ArchaiusHelper;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.google.common.base.Optional;
import com.kenzan.msl.catalog.client.cassandra.QueryAccessor;

import java.util.UUID;

import com.kenzan.msl.catalog.client.cassandra.query.AlbumsQuery;
import com.kenzan.msl.catalog.client.cassandra.query.ArtistsQuery;
import com.kenzan.msl.catalog.client.cassandra.query.PaginationQuery;
import com.kenzan.msl.catalog.client.cassandra.query.SongsQuery;
import com.kenzan.msl.catalog.client.dto.PagingStateDto;
import com.kenzan.msl.catalog.client.dto.FeaturedAlbumsDto;
import com.kenzan.msl.catalog.client.dto.AlbumsByFacetDto;
import com.kenzan.msl.catalog.client.dto.AlbumArtistBySongDto;
import com.kenzan.msl.catalog.client.dto.FeaturedArtistsDto;
import com.kenzan.msl.catalog.client.dto.ArtistsByFacetDto;
import com.kenzan.msl.catalog.client.dto.FeaturedSongsDto;
import com.kenzan.msl.catalog.client.dto.SongsByFacetDto;
import com.kenzan.msl.catalog.client.dto.SongsAlbumsByArtistDto;
import com.kenzan.msl.catalog.client.dto.SongsArtistByAlbumDto;
import rx.Observable;

/**
 * Implementation of the CatalogService interface that retrieves its data from a Cassandra cluster.
 */
public class CassandraCatalogService
    implements CatalogService {

    public QueryAccessor queryAccessor;
    public MappingManager mappingManager;

    private static final String DEFAULT_CONTACT_POINT = "127.0.0.1";
    private static final String DEFAULT_MSL_KEYSPACE = "msl";

    private static CassandraCatalogService instance = null;

    private CassandraCatalogService() {
        ArchaiusHelper.setupArchaius();
        DynamicPropertyFactory propertyFactory = DynamicPropertyFactory.getInstance();
        DynamicStringProperty contactPoint = propertyFactory.getStringProperty("contact_point", DEFAULT_CONTACT_POINT);
        Cluster cluster = Cluster.builder().addContactPoint(contactPoint.getValue()).build();

        DynamicStringProperty keyspace = propertyFactory.getStringProperty("keyspace", DEFAULT_MSL_KEYSPACE);
        Session session = cluster.connect(keyspace.getValue());

        mappingManager = new MappingManager(session);
        queryAccessor = mappingManager.createAccessor(QueryAccessor.class);
    }

    public static CassandraCatalogService getInstance() {
        if ( instance == null ) {
            instance = new CassandraCatalogService();
        }
        return instance;
    }

    // ==========================================================================================================
    // PAGINATION
    // ==========================================================================================================

    /**
     * Adds or update a pagingState
     *
     * @param pagingState com.kenzan.msl.catalog.client.dto.PagingStateDto
     * @return Observable&lt;Void&gt;
     */
    public Observable<Void> addOrUpdatePagingState(PagingStateDto pagingState) {
        PaginationQuery.add(mappingManager, pagingState);
        return Observable.empty();
    }

    /**
     * Retrieves a paging state
     *
     * @param pagingStateUuid java.util.UUID
     * @return Observable&lt;PagingStateDto&gt;
     */
    public Observable<PagingStateDto> getPagingState(UUID pagingStateUuid) {
        Optional<PagingStateDto> result = PaginationQuery.get(mappingManager, pagingStateUuid);
        if ( result.isPresent() ) {
            return Observable.just(result.get());
        }

        return Observable.empty();
    }

    /**
     * Deletes a paging state by its id
     *
     * @param pagingStateUuid java.util.UUID
     * @return Observable&lt;Void&gt;
     */
    public Observable<Void> deletePagingState(UUID pagingStateUuid) {
        PaginationQuery.remove(mappingManager, pagingStateUuid);
        return Observable.empty();
    }

    // ==========================================================================================================
    // ALBUMS
    // ==========================================================================================================

    /**
     * Retrieves a list of albums optionally limited
     *
     * @param limit Optional&lt;Integer&gt;
     * @return Observable&lt;ResultSet&gt;
     */
    public Observable<ResultSet> getFeaturedAlbums(Optional<Integer> limit) {
        return Observable.just(AlbumsQuery.getAlbums(queryAccessor, Optional.absent(), limit));
    }

    /**
     * Maps a resultSet object into a FeaturedAlbumsDto result array
     *
     * @param object Observable&lt;ResultSet&gt;
     * @return Observable&lt;Result&lt;FeaturedAlbumsDto&gt;&gt;
     */
    public Observable<Result<FeaturedAlbumsDto>> mapFeaturedAlbums(Observable<ResultSet> object) {
        return Observable.just(mappingManager.mapper(FeaturedAlbumsDto.class).map(object.toBlocking().first()));
    }

    /**
     * Retrieves a list of albums filtered by a specific facet, optionally limited
     *
     * @param facetName String
     * @param limit Optional&lt;Integer&gt;
     * @return Observable&lt;ResultSet&gt;
     */
    public Observable<ResultSet> getAlbumsByFacet(String facetName, Optional<Integer> limit) {
        return Observable.just(AlbumsQuery.getAlbums(queryAccessor, Optional.of(facetName), limit));
    }

    /**
     * Maps a resultSet object into a AlbumsByFacetDto result array
     *
     * @param object Observable&lt;ResultSet&gt;
     * @return Observable&lt;Result&lt;AlbumsByFacetDto&gt;&gt;
     */
    public Observable<Result<AlbumsByFacetDto>> mapAlbumsByFacet(Observable<ResultSet> object) {
        return Observable.just(mappingManager.mapper(AlbumsByFacetDto.class).map(object.toBlocking().first()));
    }

    /**
     * Retrieves results from the album_artist_by_song cassandra table
     *
     * @param songUuid java.util.UUID
     * @param limit Optional&lt;Integer&gt;
     * @return Observable&lt;ResultSet&gt;
     */
    public Observable<ResultSet> getAlbumArtistBySong(UUID songUuid, Optional<Integer> limit) {
        return Observable.just(AlbumsQuery.getAlbumArtistBySong(queryAccessor, songUuid, limit));
    }

    /**
     * Maps a resultSet object into a AlbumArtistBySongDto result array
     *
     * @param object Observable&lt;ResultSet&gt;
     * @return Observable&lt;Result&lt;AlbumArtistBySongDto&gt;&gt;
     */
    public Observable<Result<AlbumArtistBySongDto>> mapAlbumArtistBySong(Observable<ResultSet> object) {
        return Observable.just(mappingManager.mapper(AlbumArtistBySongDto.class).map(object.toBlocking().first()));
    }

    // =========================================================================================================
    // ARTISTS
    // =========================================================================================================

    /**
     * Retrieves a list of artist optionally limited
     *
     * @param limit Optional&lt;Integer&gt;
     * @return Observable&lt;ResultSet&gt;
     */
    public Observable<ResultSet> getFeaturedArtists(Optional<Integer> limit) {
        return Observable.just(ArtistsQuery.getArtists(queryAccessor, Optional.absent(), limit));
    }

    /**
     * Maps a resultSet object into a FeaturedArtistsDto result array
     *
     * @param object Observable&lt;ResultSet&gt;
     * @return Observable&lt;Result&lt;FeaturedArtistsDto&gt;&gt;
     */
    public Observable<Result<FeaturedArtistsDto>> mapFeaturedArtists(Observable<ResultSet> object) {
        return Observable.just(mappingManager.mapper(FeaturedArtistsDto.class).map(object.toBlocking().first()));
    }

    /**
     * Retrieves a list of artist filtered by facet, optionally limited
     *
     * @param facetName String
     * @param limit Optional&lt;Integer&gt;
     * @return Observable&lt;ResultSet&gt;
     */
    public Observable<ResultSet> getArtistsByFacet(String facetName, Optional<Integer> limit) {
        return Observable.just(ArtistsQuery.getArtists(queryAccessor, Optional.of(facetName), limit));
    }

    /**
     * Maps a resultSet object into a ArtistsByFacetDto result array
     *
     * @param object Observable&lt;ResultSet&gt;
     * @return Observable&lt;Result&lt;ArtistsByFacetDto&gt;&gt;
     */
    public Observable<Result<ArtistsByFacetDto>> mapArtistByFacet(Observable<ResultSet> object) {
        return Observable.just(mappingManager.mapper(ArtistsByFacetDto.class).map(object.toBlocking().first()));
    }

    // ===========================================================================================================
    // SONGS
    // ===========================================================================================================

    /**
     * Retrieves a list of songs optionally limited
     *
     * @param limit Optional&lt;Integer&gt;
     * @return Observable&lt;ResultSet&gt;
     */
    public Observable<ResultSet> getFeaturedSongs(Optional<Integer> limit) {
        return Observable.just(SongsQuery.getSongs(queryAccessor, Optional.absent(), limit));
    }

    /**
     * Maps a resultSet object into a FeaturedSongsDto result array
     *
     * @param object Observable&lt;ResultSet&gt;
     * @return Observable&lt;Result&lt;FeaturedSongsDto&gt;&gt;
     */
    public Observable<Result<FeaturedSongsDto>> mapFeaturedSongs(Observable<ResultSet> object) {
        return Observable.just(mappingManager.mapper(FeaturedSongsDto.class).map(object.toBlocking().first()));
    }

    /**
     * Retrieves a list fo songs filtered by a specific facet. Optionally limited
     *
     * @param facetName String
     * @param limit Optional&lt;Integer&gt;
     * @return Observable&lt;ResultSet&gt;
     */
    public Observable<ResultSet> getSongsByFacets(String facetName, Optional<Integer> limit) {
        return Observable.just(SongsQuery.getSongs(queryAccessor, Optional.of(facetName), limit));
    }

    /**
     * Maps a resultSet object into a SongsByFacetDto result array
     *
     * @param object Observable&lt;ResultSet&gt;
     * @return Observable&lt;Result&lt;SongsByFacetDto&gt;&gt;
     */
    public Observable<Result<SongsByFacetDto>> mapSongsByFacet(Observable<ResultSet> object) {
        return Observable.just(mappingManager.mapper(SongsByFacetDto.class).map(object.toBlocking().first()));
    }

    /**
     * Retrieves results from the songs_albums_by_artist cassandra table
     *
     * @param artistUuid java.util.UUID
     * @param limit Optional&lt;Integer&gt;
     * @return Observable&lt;ResultSet&gt;
     */
    public Observable<ResultSet> getSongsAlbumsByArtist(UUID artistUuid, Optional<Integer> limit) {
        return Observable.just(SongsQuery.getSongsAlbumsByArtist(queryAccessor, artistUuid, limit));
    }

    /**
     * Maps a resultSet object into a SongsAlbumsByArtistDto result array
     *
     * @param object Observable.ResultSet
     * @return Observable&lt;Result&lt;SongsAlbumsByArtistDto&gt;&gt;
     */
    public Observable<Result<SongsAlbumsByArtistDto>> mapSongsAlbumsByArtist(Observable<ResultSet> object) {
        return Observable.just(mappingManager.mapper(SongsAlbumsByArtistDto.class).map(object.toBlocking().first()));
    }

    /**
     * Retrieves results from the songs_artist_by_album cassandra table
     *
     * @param albumUuid java.util.UUID
     * @param limit Optional&lt;Integer&gt;
     * @return Observable&lt;ResultSet&gt;
     */
    public Observable<ResultSet> getSongsArtistByAlbum(UUID albumUuid, Optional<Integer> limit) {
        return Observable.just(SongsQuery.getSongsArtistByAlbum(queryAccessor, albumUuid, limit));
    }

    /**
     * Maps a resultSet object into a SongsArtistByAlbumDto result array
     *
     * @param object Observable&lt;ResultSet&gt;
     * @return Observable&lt;Result&lt;SongsArtistByAlbumDto&gt;&gt;
     */
    public Observable<Result<SongsArtistByAlbumDto>> mapSongsArtistByAlbum(Observable<ResultSet> object) {
        return Observable.just(mappingManager.mapper(SongsArtistByAlbumDto.class).map(object.toBlocking().first()));
    }

}
