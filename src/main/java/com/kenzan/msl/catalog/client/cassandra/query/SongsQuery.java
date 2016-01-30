/*
 * Copyright 2015, Kenzan, All rights reserved.
 */
package com.kenzan.msl.catalog.client.cassandra.query;

import com.datastax.driver.core.ResultSet;
import com.google.common.base.Optional;
import com.kenzan.msl.catalog.client.cassandra.QueryAccessor;

import java.util.UUID;

public class SongsQuery {

    /**
     * Retrieves a list of songs from the cassandra db
     *
     * @param queryAccessor com.kenzan.msl.catalog.client.cassandra.QueryAccessor
     * @param facet facet filter
     * @param limit optional limit for results
     * @return ResultSet
     */
    public static ResultSet getSongs(final QueryAccessor queryAccessor, final Optional<String> facet,
                                     final Optional<Integer> limit) {
        if ( facet.isPresent() && limit.isPresent() ) {
            return queryAccessor.getSongsByFacetsWithLimit(facet.get(), limit.get());
        }
        else if ( facet.isPresent() ) {
            return queryAccessor.getSongsByFacets(facet.get());
        }
        else if ( limit.isPresent() ) {
            return queryAccessor.getSongsWithLimit(limit.get());
        }
        else {
            return queryAccessor.getSongs();
        }
    }

    /**
     * Retrieves results from the songs_albums_by_artist cassandra table
     *
     * @param queryAccessor com.kenzan.msl.catalog.client.cassandra.QueryAccessor
     * @param artistId artist UUID
     * @param limit optional limit
     * @return ResultSet
     */
    public static ResultSet getSongsAlbumsByArtist(final QueryAccessor queryAccessor, final UUID artistId,
                                                   final Optional<Integer> limit) {
        if ( limit.isPresent() ) {
            return queryAccessor.songsAlbumsByArtistWithLimit(artistId, limit.get());
        }

        return queryAccessor.songsAlbumsByArtist(artistId);
    }

    /**
     * Retrieves results from the songs_artist_by_album cassandra table
     *
     * @param queryAccessor com.kenzan.msl.catalog.client.cassandra.QueryAccessor
     * @param albumId album UUID
     * @param limit optional limit
     * @return ResultSet
     */
    public static ResultSet getSongsArtistByAlbum(final QueryAccessor queryAccessor, final UUID albumId,
                                                  final Optional<Integer> limit) {
        if ( limit.isPresent() ) {
            return queryAccessor.songsArtistByAlbumWithLimit(albumId, limit.get());
        }

        return queryAccessor.songsArtistByAlbum(albumId);
    }

}
