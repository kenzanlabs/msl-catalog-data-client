package com.kenzan.msl.catalog.client.cassandra.query;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.kenzan.msl.catalog.client.dto.PagingStateDto;

import java.util.UUID;

public class PaginationQuery {

  @Inject
  @Named("ttlSecs")
  private static int TTL_SECS;

  /**
   * Adds a paging state to the mappingManager object and paging_state table
   *
   * @param manager com.datastax.driver.mapping.MappingManager
   * @param pagingState com.kenzan.msl.catalog.client.dto.PagingStateDto
   */
  public static void add(final MappingManager manager, final PagingStateDto pagingState) {

    pagingState.getPagingState().setPageState(pagingState.getPagingState().getPageState());
    manager.mapper(PagingStateDto.class).save(pagingState, Mapper.Option.ttl(TTL_SECS));
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
    if (null == pagingStateDto) {
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
