package com.kenzan.msl.catalog.client.cassandra.query;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.google.common.base.Optional;
import com.kenzan.msl.catalog.client.dao.PagingStateDao;

import java.util.UUID;

public class PaginationQuery {

    private static final int PAGING_STATE_TTL_SECS = 60 * 60; // 1 hour;

    /**
     * Adds a paging state to the mappingManager object and paging_state table
     *
     * @param manager com.datastax.driver.mapping.MappingManager
     * @param pagingState com.kenzan.msl.catalog.client.dao.PagingStateDao
     */
    public static void add(final MappingManager manager, final PagingStateDao pagingState) {
        pagingState.getPagingState().setPageState(null);
        manager.mapper(PagingStateDao.class).save(pagingState, Mapper.Option.ttl(PAGING_STATE_TTL_SECS));
    }

    /**
     * Retrieves the paging state from the MappingManager object and paging_state table
     *
     * @param manager com.datastax.driver.mapping.MappingManager
     * @param pagingId java.util.UUID
     * @return Optional<PagingStateDao>
     */
    public static Optional<PagingStateDao> get(final MappingManager manager, final UUID pagingId) {
        Mapper<PagingStateDao> mapper = manager.mapper(PagingStateDao.class);
        PagingStateDao pagingStateDao = mapper.get(pagingId);
        if ( null == pagingStateDao ) {
            return Optional.absent();
        }

        return Optional.of(pagingStateDao);
    }

    /**
     * Removes the paging state from the MappingManager object and paging_state table
     *
     * @param manager com.datastax.driver.mapping.MappingManager
     * @param pagingId java.util.UUID
     */
    public static void remove(final MappingManager manager, UUID pagingId) {
        manager.mapper(PagingStateDao.class).delete(pagingId);
    }

}
