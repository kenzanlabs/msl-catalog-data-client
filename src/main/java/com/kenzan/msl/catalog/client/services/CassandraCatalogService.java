/*
 * Copyright 2015, Charter Communications, All rights reserved.
 */
package com.kenzan.msl.catalog.client.services;

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
import com.kenzan.msl.catalog.client.dao.*;
import rx.Observable;

/**
 * Implementation of the CatalogService interface that retrieves its data from a Cassandra cluster.
 */
public class CassandraCatalogService
    implements CatalogService {

    public QueryAccessor queryAccessor;
    public MappingManager mappingManager;

    private static CassandraCatalogService instance = null;

    private CassandraCatalogService() {
        // TODO: Get the contact point from config param
        Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build();

        // TODO: Get the keyspace from config param
        Session session = cluster.connect("msl");

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
     * @param pagingState com.kenzan.msl.catalog.client.dao.PagingStateDao
     * @return Observable<Void>
     */
    public Observable<Void> addOrUpdatePagingState(PagingStateDao pagingState) {
        PaginationQuery.add(mappingManager, pagingState);
        return Observable.empty();
    }

    /**
     * Retrieves a paging state
     *
     * @param pagingStateUuid java.util.UUID
     * @return Observable<PagingStateDao>
     */
    public Observable<PagingStateDao> getPagingState(UUID pagingStateUuid) {
        Optional<PagingStateDao> result = PaginationQuery.get(mappingManager, pagingStateUuid);
        if ( result.isPresent() ) {
            return Observable.just(result.get());
        }
        else {
            return Observable.empty();
        }
    }

    /**
     * Deletes a paging state by its id
     *
     * @param pagingStateUuid java.util.UUID
     * @return Observable<Void>
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
     * @param limit Optional<Integer>
     * @return Observable<ResultSet>
     */
    public Observable<ResultSet> getFeaturedAlbums(Optional<Integer> limit) {
        return Observable.just(AlbumsQuery.getAlbums(queryAccessor, Optional.absent(), limit));
    }

    /**
     * Maps a resultSet object into a FeaturedAlbumsDao result array
     *
     * @param object Observable<ResultSet>
     * @return Observable<Result<FeaturedAlbumsDao>>
     */
    public Observable<Result<FeaturedAlbumsDao>> mapFeaturedAlbums(Observable<ResultSet> object) {
        return Observable.just(mappingManager.mapper(FeaturedAlbumsDao.class).map(object.toBlocking().first()));
    }

    /**
     * Retrieves a list of albums filtered by a specific facet, optionally limited
     *
     * @param facetName String
     * @param limit Optional<Integer>
     * @return Observable<ResultSet>
     */
    public Observable<ResultSet> getAlbumsByFacet(String facetName, Optional<Integer> limit) {
        return Observable.just(AlbumsQuery.getAlbums(queryAccessor, Optional.of(facetName), limit));
    }

    /**
     * Maps a resultSet object into a AlbumsByFacetDao result array
     *
     * @param object Observable<ResultSet>
     * @return Observable<Result<AlbumsByFacetDao>>
     */
    public Observable<Result<AlbumsByFacetDao>> mapAlbumsByFacet(Observable<ResultSet> object) {
        return Observable.just(mappingManager.mapper(AlbumsByFacetDao.class).map(object.toBlocking().first()));
    }

    /**
     * Retrieves results from the album_artist_by_song cassandra table
     *
     * @param songUuid java.util.UUID
     * @param limit Optional<Integer>
     * @return Observable<ResultSet>
     */
    public Observable<ResultSet> getAlbumArtistBySong(UUID songUuid, Optional<Integer> limit) {
        return Observable.just(AlbumsQuery.getAlbumArtistBySong(queryAccessor, songUuid, limit));
    }

    /**
     * Maps a resultSet object into a AlbumArtistBySongDao result array
     *
     * @param object Observable<ResultSet>
     * @return Observable<Result<AlbumArtistBySongDao>>
     */
    public Observable<Result<AlbumArtistBySongDao>> mapAlbumArtistBySong(Observable<ResultSet> object) {
        return Observable.just(mappingManager.mapper(AlbumArtistBySongDao.class).map(object.toBlocking().first()));
    }

    // =========================================================================================================
    // ARTISTS
    // =========================================================================================================

    /**
     * Retrieves a list of artist optionally limited
     *
     * @param limit Optional<Integer>
     * @return Observable<ResultSet>
     */
    public Observable<ResultSet> getFeaturedArtists(Optional<Integer> limit) {
        return Observable.just(ArtistsQuery.getArtists(queryAccessor, Optional.absent(), limit));
    }

    /**
     * Maps a resultSet object into a FeaturedArtistsDao result array
     *
     * @param object Observable<ResultSet>
     * @return Observable<Result<FeaturedArtistsDao>>
     */
    public Observable<Result<FeaturedArtistsDao>> mapFeaturedArtists(Observable<ResultSet> object) {
        return Observable.just(mappingManager.mapper(FeaturedArtistsDao.class).map(object.toBlocking().first()));
    }

    /**
     * Retrieves a list of artist filtered by facet, optionally limited
     *
     * @param facetName String
     * @param limit Optional<Integer>
     * @return Observable<ResultSet>
     */
    public Observable<ResultSet> getArtistsByFacet(String facetName, Optional<Integer> limit) {
        return Observable.just(ArtistsQuery.getArtists(queryAccessor, Optional.of(facetName), limit));
    }

    /**
     * Maps a resultSet object into a ArtistsByFacetDao result array
     *
     * @param object Observable<ResultSet>
     * @return Observable<Result<ArtistsByFacetDao>>
     */
    public Observable<Result<ArtistsByFacetDao>> mapArtistByFacet(Observable<ResultSet> object) {
        return Observable.just(mappingManager.mapper(ArtistsByFacetDao.class).map(object.toBlocking().first()));
    }

    // ===========================================================================================================
    // SONGS
    // ===========================================================================================================

    /**
     * Retrieves a list of songs optionally limited
     *
     * @param limit Optional<Integer>
     * @return Observable<ResultSet>
     */
    public Observable<ResultSet> getFeaturedSongs(Optional<Integer> limit) {
        return Observable.just(SongsQuery.getSongs(queryAccessor, Optional.absent(), limit));
    }

    /**
     * Maps a resultSet object into a FeaturedSongsDao result array
     *
     * @param object Observable<ResultSet>
     * @return Observable<Result<FeaturedSongsDao>>
     */
    public Observable<Result<FeaturedSongsDao>> mapFeaturedSongs(Observable<ResultSet> object) {
        return Observable.just(mappingManager.mapper(FeaturedSongsDao.class).map(object.toBlocking().first()));
    }

    /**
     * Retrieves a list fo songs filtered by a specific facet. Optionally limited
     *
     * @param facetName String
     * @param limit Optional<Integer>
     * @return Observable<ResultSet>
     */
    public Observable<ResultSet> getSongsByFacets(String facetName, Optional<Integer> limit) {
        return Observable.just(SongsQuery.getSongs(queryAccessor, Optional.of(facetName), limit));
    }

    /**
     * Maps a resultSet object into a SongsByFacetDao result array
     *
     * @param object Observable<ResultSet>
     * @return Observable<Result<SongsByFacetDao>>
     */
    public Observable<Result<SongsByFacetDao>> mapSongsByFacet(Observable<ResultSet> object) {
        return Observable.just(mappingManager.mapper(SongsByFacetDao.class).map(object.toBlocking().first()));
    }

    /**
     * Retrieves results from the songs_albums_by_artist cassandra table
     *
     * @param artistUuid java.util.UUID
     * @param limit Optional<Integer>
     * @return Observable<ResultSet>
     */
    public Observable<ResultSet> getSongsAlbumsByArtist(UUID artistUuid, Optional<Integer> limit) {
        return Observable.just(SongsQuery.getSongsAlbumsByArtist(queryAccessor, artistUuid, limit));
    }

    /**
     * Maps a resultSet object into a SongsAlbumsByArtistDao result array
     *
     * @param object Observable<ResultSet>
     * @return Observable<Result<SongsAlbumsByArtistDao>>
     */
    public Observable<Result<SongsAlbumsByArtistDao>> mapSongsAlbumsByArtist(Observable<ResultSet> object) {
        return Observable.just(mappingManager.mapper(SongsAlbumsByArtistDao.class).map(object.toBlocking().first()));
    }

    /**
     * Retrieves results from the songs_artist_by_album cassandra table
     *
     * @param albumUuid java.util.UUID
     * @param limit Optional<Integer>
     * @return Observable<ResultSet>
     */
    public Observable<ResultSet> getSongsArtistByAlbum(UUID albumUuid, Optional<Integer> limit) {
        return Observable.just(SongsQuery.getSongsArtistByAlbum(queryAccessor, albumUuid, limit));
    }

    /**
     * Maps a resultSet object into a SongsArtistByAlbumDao result array
     *
     * @param object Observable<ResultSet>
     * @return Observable<Result<SongsArtistByAlbumDao>>
     */
    public Observable<Result<SongsArtistByAlbumDao>> mapSongsArtistByAlbum(Observable<ResultSet> object) {
        return Observable.just(mappingManager.mapper(SongsArtistByAlbumDao.class).map(object.toBlocking().first()));
    }

}
