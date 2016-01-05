/*
 * Copyright 2015, Charter Communications, All rights reserved.
 */
package com.kenzan.msl.catalog.client.cassandra.query;

import com.datastax.driver.core.ResultSet;
import com.google.common.base.Optional;
import com.kenzan.msl.catalog.client.cassandra.QueryAccessor;

public class ArtistsQuery {

    /**
     * Retrieves results from the featured_artists cassandra table
     *
     * @param queryAccessor com.kenzan.msl.catalog.client.cassandra.QueryAccessor
     * @param facet facet filter
     * @param limit optional limit
     * @return ResultSet
     */
    public static ResultSet getArtists(final QueryAccessor queryAccessor, final Optional<String> facet,
                                       final Optional<Integer> limit) {
        if ( facet.isPresent() && limit.isPresent() ) {
            return queryAccessor.getArtistsByFacetsWithLimit(facet.get(), limit.get());
        }
        else if ( facet.isPresent() ) {
            return queryAccessor.getArtistsByFacets(facet.get());
        }
        else if ( limit.isPresent() ) {
            return queryAccessor.getArtistsWithLimit(limit.get());
        }
        else {
            return queryAccessor.getArtists();
        }
    }
}
