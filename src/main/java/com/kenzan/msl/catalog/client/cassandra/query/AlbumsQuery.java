/*
 * Copyright 2015, Charter Communications, All rights reserved.
 */
package com.kenzan.msl.catalog.client.cassandra.query;

import com.datastax.driver.core.ResultSet;
import com.google.common.base.Optional;
import com.kenzan.msl.catalog.client.cassandra.QueryAccessor;

import java.util.UUID;

public class AlbumsQuery {

    /**
     * Retrieves a list of albums from the cassandra db
     *
     * @param queryAccessor com.kenzan.msl.catalog.client.cassandra.QueryAccessor
     * @param facet facet filter
     * @param limit optional limit
     * @return ResultSet
     */
    public static ResultSet getAlbums(final QueryAccessor queryAccessor, final Optional<String> facet,
                                      final Optional<Integer> limit) {
        if ( facet.isPresent() && limit.isPresent() ) {
            return queryAccessor.getAlbumsByFacetsWithLimit(facet.get(), limit.get());
        }
        else if ( facet.isPresent() ) {
            return queryAccessor.getAlbumsByFacets(facet.get());
        }
        else if ( limit.isPresent() ) {
            return queryAccessor.getAlbumsWithLimit(limit.get());
        }
        else {
            return queryAccessor.getAlbums();
        }
    }

    /**
     * Retrieves results from the album_artist_by_song table
     *
     * @param queryAccessor com.kenzan.msl.catalog.client.cassandra.QueryAccessor
     * @param songId song UUID
     * @param limit optional limit
     * @return ResultSet
     */
    public static ResultSet getAlbumArtistBySong(final QueryAccessor queryAccessor, UUID songId, Optional<Integer> limit) {
        if ( limit.isPresent() ) {
            return queryAccessor.albumArtistBySongWithLimit(songId, limit.get());
        }
        else {
            return queryAccessor.albumArtistBySong(songId);
        }
    }
}
