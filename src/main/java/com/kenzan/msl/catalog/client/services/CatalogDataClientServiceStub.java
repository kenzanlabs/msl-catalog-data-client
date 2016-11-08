package com.kenzan.msl.catalog.client.services;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.google.common.base.Optional;
import com.kenzan.msl.catalog.client.cassandra.QueryAccessor;
import com.kenzan.msl.catalog.client.dto.*;
import rx.Observable;

import java.util.UUID;

/**
 * @author Kenzan
 */
public class CatalogDataClientServiceStub implements CatalogDataClientService {

  // ==========================================================================================================
  // PAGINATION
  // ==========================================================================================================

  public Observable<Void> addOrUpdatePagingState(PagingStateDto pagingState) {
    return Observable.empty();
  }

  public Observable<PagingStateDto> getPagingState(UUID pagingStateUuid) {
    return Observable.empty();
  }

  public Observable<Void> deletePagingState(UUID pagingStateUuid) {
    return Observable.empty();
  }

  // ==========================================================================================================
  // ALBUMS
  // =================================================================================================================

  public Observable<ResultSet> getFeaturedAlbums(Optional<Integer> limit) {
    return Observable.empty();
  }

  public Observable<Result<FeaturedAlbumsDto>> mapFeaturedAlbums(Observable<ResultSet> object) {
    return Observable.empty();
  }

  public Observable<ResultSet> getAlbumsByFacet(String facetName, Optional<Integer> limit) {
    return Observable.empty();
  }

  public Observable<Result<AlbumsByFacetDto>> mapAlbumsByFacet(Observable<ResultSet> object) {
    return Observable.empty();
  }

  public Observable<ResultSet> getAlbumArtistBySong(UUID songUuid, Optional<Integer> limit) {
    return Observable.empty();
  }

  public Observable<Result<AlbumArtistBySongDto>> mapAlbumArtistBySong(Observable<ResultSet> object) {
    return Observable.empty();
  }

  // =========================================================================================================
  // ARTISTS
  // =================================================================================================================

  public Observable<ResultSet> getFeaturedArtists(Optional<Integer> limit) {
    return Observable.empty();
  }

  public Observable<Result<FeaturedArtistsDto>> mapFeaturedArtists(Observable<ResultSet> object) {
    return Observable.empty();
  }

  public Observable<ResultSet> getArtistsByFacet(String facetName, Optional<Integer> limit) {
    return Observable.empty();
  }

  public Observable<Result<ArtistsByFacetDto>> mapArtistByFacet(Observable<ResultSet> object) {
    return Observable.empty();
  }

  // ===========================================================================================================
  // SONGS
  // =================================================================================================================

  public Observable<ResultSet> getFeaturedSongs(Optional<Integer> limit) {
    return Observable.empty();
  }

  public Observable<Result<FeaturedSongsDto>> mapFeaturedSongs(Observable<ResultSet> object) {
    return Observable.empty();
  }

  public Observable<ResultSet> getSongsByFacets(String facets, Optional<Integer> limit) {
    return Observable.empty();
  }

  public Observable<Result<SongsByFacetDto>> mapSongsByFacet(Observable<ResultSet> object) {
    return Observable.empty();
  }

  public Observable<ResultSet> getSongsAlbumsByArtist(UUID artistUuid, Optional<Integer> limit) {
    return Observable.empty();
  }

  public Observable<Result<SongsAlbumsByArtistDto>> mapSongsAlbumsByArtist(Observable<ResultSet> object) {
    return Observable.empty();
  }

  public Observable<ResultSet> getSongsArtistByAlbum(UUID albumUuid, Optional<Integer> limit) {
    return Observable.empty();
  }

  public Observable<Result<SongsArtistByAlbumDto>> mapSongsArtistByAlbum(Observable<ResultSet> object) {
    return Observable.empty();
  }

  public QueryAccessor getQueryAccessor () {
    return null;
  }

  public MappingManager getMappingManager () {
    return null;
  }
}
