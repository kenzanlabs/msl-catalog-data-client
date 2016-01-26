/*
 * Copyright 2015, Kenzan, All rights reserved.
 */
package com.kenzan.msl.catalog.client.services;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.Result;
import com.google.common.base.Optional;
import com.kenzan.msl.catalog.client.dao.*;
import rx.Observable;

import java.util.UUID;

public interface CatalogService {

    // ==========================================================================================================
    // PAGINATION
    // ==========================================================================================================

    Observable<Void> addOrUpdatePagingState(PagingStateDao pagingState);

    Observable<PagingStateDao> getPagingState(UUID pagingStateUuid);

    Observable<Void> deletePagingState(UUID pagingStateUuid);

    // ==========================================================================================================
    // ALBUMS
    // =================================================================================================================

    Observable<ResultSet> getFeaturedAlbums(Optional<Integer> limit);

    Observable<Result<FeaturedAlbumsDao>> mapFeaturedAlbums(Observable<ResultSet> object);

    Observable<ResultSet> getAlbumsByFacet(String facetName, Optional<Integer> limit);

    Observable<Result<AlbumsByFacetDao>> mapAlbumsByFacet(Observable<ResultSet> object);

    Observable<ResultSet> getAlbumArtistBySong(UUID songUuid, Optional<Integer> limit);

    Observable<Result<AlbumArtistBySongDao>> mapAlbumArtistBySong(Observable<ResultSet> object);

    // =========================================================================================================
    // ARTISTS
    // =================================================================================================================

    Observable<ResultSet> getFeaturedArtists(Optional<Integer> limit);

    Observable<Result<FeaturedArtistsDao>> mapFeaturedArtists(Observable<ResultSet> object);

    Observable<ResultSet> getArtistsByFacet(String facetName, Optional<Integer> limit);

    Observable<Result<ArtistsByFacetDao>> mapArtistByFacet(Observable<ResultSet> object);

    // ===========================================================================================================
    // SONGS
    // =================================================================================================================

    Observable<ResultSet> getFeaturedSongs(Optional<Integer> limit);

    Observable<Result<FeaturedSongsDao>> mapFeaturedSongs(Observable<ResultSet> object);

    Observable<ResultSet> getSongsByFacets(String facets, Optional<Integer> limit);

    Observable<Result<SongsByFacetDao>> mapSongsByFacet(Observable<ResultSet> object);

    Observable<ResultSet> getSongsAlbumsByArtist(UUID artistUuid, Optional<Integer> limit);

    Observable<Result<SongsAlbumsByArtistDao>> mapSongsAlbumsByArtist(Observable<ResultSet> object);

    Observable<ResultSet> getSongsArtistByAlbum(UUID albumUuid, Optional<Integer> limit);

    Observable<Result<SongsArtistByAlbumDao>> mapSongsArtistByAlbum(Observable<ResultSet> object);

}
