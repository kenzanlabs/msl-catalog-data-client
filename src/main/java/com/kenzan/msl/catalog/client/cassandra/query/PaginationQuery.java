package com.kenzan.msl.catalog.client.cassandra.query;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.google.common.base.Optional;
import com.kenzan.msl.catalog.client.archaius.ArchaiusHelper;
import com.kenzan.msl.catalog.client.dto.PagingStateDto;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicIntProperty;

import java.util.UUID;

public class PaginationQuery {

    private static final int DEFAULT_TTL_SECS = 60 * 60; // 1 hour;

    /**
     * Adds a paging state to the mappingManager object and paging_state table
     *
     * @param manager com.datastax.driver.mapping.MappingManager
     * @param pagingState com.kenzan.msl.catalog.client.dto.PagingStateDto
     */
    public static void add(final MappingManager manager, final PagingStateDto pagingState) {
        ArchaiusHelper.setupArchaius();
        DynamicPropertyFactory propertyFactory = DynamicPropertyFactory.getInstance();
        DynamicIntProperty ttlSecs = propertyFactory.getIntProperty("paging_state_ttl_secs", DEFAULT_TTL_SECS);
        pagingState.getPagingState().setPageState(pagingState.getPagingState().getPageState());
        manager.mapper(PagingStateDto.class).save(pagingState, Mapper.Option.ttl(ttlSecs.getValue()));
    }

    /**
     * Retrieves the paging state from the MappingManager object and paging_state table
     *
     * @param manager com.datastax.driver.mapping.MappingManager
     * @param pagingId java.util.UUID
     * @return Optional&lt;PagingStateDto&gt;
     */
    public static Optional<PagingStateDto> get(final MappingManager manager, final UUID pagingId) {
        Mapper<PagingStateDto> mapper = manager.mapper(PagingStateDto.class);
        PagingStateDto pagingStateDto = mapper.get(pagingId);
        if ( null == pagingStateDto ) {
            return Optional.absent();
        }

        return Optional.of(pagingStateDto);
    }

    /**
     * Removes the paging state from the MappingManager object and paging_state table
     *
     * @param manager com.datastax.driver.mapping.MappingManager
     * @param pagingId java.util.UUID
     */
    public static void remove(final MappingManager manager, UUID pagingId) {
        manager.mapper(PagingStateDto.class).delete(pagingId);
    }

}
