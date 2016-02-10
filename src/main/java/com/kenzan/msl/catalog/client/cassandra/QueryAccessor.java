/*
 * Copyright 2015, Kenzan, All rights reserved.
 */
package com.kenzan.msl.catalog.client.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;

import java.util.UUID;

@Accessor
public interface QueryAccessor {

    public static String FACETED_ARTISTS_QUERY = "SELECT * FROM artists_by_facet WHERE facet_name = :facet_name AND content_type = 'Artist'";
    public static String FACETED_SONGS_QUERY = "SELECT * FROM songs_by_facet WHERE facet_name = :facet_name AND content_type = 'Song'";
    public static String FACETED_ALBUMS_QUERY = "SELECT * FROM albums_by_facet WHERE facet_name = :facet_name AND content_type = 'Album'";

    public static String FEATURED_ARTISTS_QUERY = "SELECT * FROM featured_artists WHERE hotness_bucket = 'Hotness01' AND content_type = 'Artist'";
    public static String FEATURED_SONGS_QUERY = "SELECT * FROM featured_songs WHERE hotness_bucket = 'Hotness01' AND content_type = 'Song'";
    public static String FEATURED_ALBUMS_QUERY = "SELECT * FROM featured_albums WHERE hotness_bucket = 'Hotness01' AND content_type = 'Album'";

    // =======================================================================================================
    // ARTISTS
    // =======================================================================================================

    @Query("SELECT * FROM artists_by_facet WHERE content_type = 'Artist' AND facet_name = :facet LIMIT :max")
    public ResultSet getArtistsByFacetsWithLimit(@Param("facet") String facet, @Param("max") int limit);

    @Query(FACETED_ARTISTS_QUERY)
    public ResultSet getArtistsByFacets(@Param("facet_name") String facet_name);

    @Query("SELECT * FROM featured_artists WHERE hotness_bucket = 'Hotness01' AND content_type = 'Artist' LIMIT :max")
    public ResultSet getArtistsWithLimit(@Param("max") int limit);

    @Query(FEATURED_ARTISTS_QUERY)
    public ResultSet getArtists();

    // =======================================================================================================
    // SONGS
    // =======================================================================================================

    @Query("SELECT * FROM songs_artist_by_album WHERE album_id = :album_id LIMIT :max")
    public ResultSet songsArtistByAlbumWithLimit(@Param("album_id") UUID album_id, @Param("max") int limit);

    @Query("SELECT * FROM songs_artist_by_album WHERE album_id = :album_id")
    public ResultSet songsArtistByAlbum(@Param("album_id") UUID album_id);

    @Query("SELECT * FROM songs_albums_by_artist WHERE artist_id = :artist_id LIMIT :max")
    public ResultSet songsAlbumsByArtistWithLimit(@Param("artist_id") UUID artist_id, @Param("max") int limit);

    @Query("SELECT * FROM songs_albums_by_artist WHERE artist_id = :artist_id")
    public ResultSet songsAlbumsByArtist(@Param("artist_id") UUID artist_id);

    @Query("SELECT * FROM songs_by_facet WHERE content_type = 'Song' AND facet_name = :facet LIMIT :max")
    public ResultSet getSongsByFacetsWithLimit(@Param("facet") String facet, @Param("max") int limit);

    @Query(FACETED_SONGS_QUERY)
    public ResultSet getSongsByFacets(@Param("facet_name") String facet_name);

    @Query("SELECT * FROM featured_artists WHERE hotness_bucket = 'Hotness01' AND content_type = 'Song' LIMIT :max")
    public ResultSet getSongsWithLimit(@Param("max") int limit);

    @Query(FEATURED_SONGS_QUERY)
    public ResultSet getSongs();

    // =======================================================================================================
    // ALBUMS
    // =======================================================================================================

    @Query("SELECT * FROM albums_by_facet WHERE content_type = 'Album' AND facet_name = :facet LIMIT :max")
    public ResultSet getAlbumsByFacetsWithLimit(@Param("facet") String facet, @Param("max") int limit);

    @Query(FACETED_ALBUMS_QUERY)
    public ResultSet getAlbumsByFacets(@Param("facet_name") String facet_name);

    @Query("SELECT * FROM featured_albums WHERE hotness_bucket = 'Hotness01' AND content_type = 'Album' LIMIT :max")
    public ResultSet getAlbumsWithLimit(@Param("max") int limit);

    @Query(FEATURED_ALBUMS_QUERY)
    public ResultSet getAlbums();

    @Query("SELECT * FROM album_artist_by_song WHERE song_id = :song_id")
    public ResultSet albumArtistBySong(@Param("song_id") UUID song_id);

    @Query("SELECT * FROM album_artist_by_song WHERE song_id = :song_id LIMIT :max")
    public ResultSet albumArtistBySongWithLimit(@Param("song_id") UUID song_id, @Param("max") int limit);

    // =======================================================================================================
    // PAGINATION
    // =======================================================================================================

    @Query(FACETED_ALBUMS_QUERY)
    public Statement albumsByFacet(@Param("facet_name") String facet_name);

    @Query(FACETED_ARTISTS_QUERY)
    public Statement artistsByFacet(@Param("facet_name") String facet_name);

    @Query(FACETED_SONGS_QUERY)
    public Statement songsByFacet(@Param("facet_name") String facet_name);

    @Query(FEATURED_ALBUMS_QUERY)
    public Statement featuredAlbums();

    @Query(FEATURED_ARTISTS_QUERY)
    public Statement featuredArtists();

    @Query(FEATURED_SONGS_QUERY)
    public Statement featuredSongs();
}