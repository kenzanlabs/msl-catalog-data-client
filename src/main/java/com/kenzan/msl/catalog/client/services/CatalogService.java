/*
 * Copyright 2015, Kenzan, All rights reserved.
 */
package com.kenzan.msl.catalog.client.services;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.Result;
import com.google.common.base.Optional;
import com.kenzan.msl.catalog.client.dto.PagingStateDto;
import com.kenzan.msl.catalog.client.dto.FeaturedAlbumsDto;
import com.kenzan.msl.catalog.client.dto.AlbumsByFacetDto;
import com.kenzan.msl.catalog.client.dto.AlbumArtistBySongDto;
import com.kenzan.msl.catalog.client.dto.FeaturedSongsDto;
import com.kenzan.msl.catalog.client.dto.FeaturedArtistsDto;
import com.kenzan.msl.catalog.client.dto.ArtistsByFacetDto;
import com.kenzan.msl.catalog.client.dto.SongsByFacetDto;
import com.kenzan.msl.catalog.client.dto.SongsAlbumsByArtistDto;
import com.kenzan.msl.catalog.client.dto.SongsArtistByAlbumDto;
import rx.Observable;

import java.util.UUID;

public interface CatalogService {

  // ==========================================================================================================
  // PAGINATION
  // ==========================================================================================================

  Observable<Void> addOrUpdatePagingState(PagingStateDto pagingState);

  Observable<PagingStateDto> getPagingState(UUID pagingStateUuid);

  Observable<Void> deletePagingState(UUID pagingStateUuid);

  // ==========================================================================================================
  // ALBUMS
  // =================================================================================================================

  Observable<ResultSet> getFeaturedAlbums(Optional<Integer> limit);

  Observable<Result<FeaturedAlbumsDto>> mapFeaturedAlbums(Observable<ResultSet> object);

  Observable<ResultSet> getAlbumsByFacet(String facetName, Optional<Integer> limit);

  Observable<Result<AlbumsByFacetDto>> mapAlbumsByFacet(Observable<ResultSet> object);

  Observable<ResultSet> getAlbumArtistBySong(UUID songUuid, Optional<Integer> limit);

  Observable<Result<AlbumArtistBySongDto>> mapAlbumArtistBySong(Observable<ResultSet> object);

  // =========================================================================================================
  // ARTISTS
  // =================================================================================================================

  Observable<ResultSet> getFeaturedArtists(Optional<Integer> limit);

  Observable<Result<FeaturedArtistsDto>> mapFeaturedArtists(Observable<ResultSet> object);

  Observable<ResultSet> getArtistsByFacet(String facetName, Optional<Integer> limit);

  Observable<Result<ArtistsByFacetDto>> mapArtistByFacet(Observable<ResultSet> object);

  // ===========================================================================================================
  // SONGS
  // =================================================================================================================

  Observable<ResultSet> getFeaturedSongs(Optional<Integer> limit);

  Observable<Result<FeaturedSongsDto>> mapFeaturedSongs(Observable<ResultSet> object);

  Observable<ResultSet> getSongsByFacets(String facets, Optional<Integer> limit);

  Observable<Result<SongsByFacetDto>> mapSongsByFacet(Observable<ResultSet> object);

  Observable<ResultSet> getSongsAlbumsByArtist(UUID artistUuid, Optional<Integer> limit);

  Observable<Result<SongsAlbumsByArtistDto>> mapSongsAlbumsByArtist(Observable<ResultSet> object);

  Observable<ResultSet> getSongsArtistByAlbum(UUID albumUuid, Optional<Integer> limit);

  Observable<Result<SongsArtistByAlbumDto>> mapSongsArtistByAlbum(Observable<ResultSet> object);

}
