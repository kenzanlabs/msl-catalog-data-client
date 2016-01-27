package com.kenzan.msl.catalog.client.cassandra.query;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.google.common.base.Optional;
import com.kenzan.msl.catalog.client.TestConstants;
import com.kenzan.msl.catalog.client.dao.PagingStateDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ MappingManager.class, Mapper.class })
public class PaginationQueryTest {

    TestConstants tc = TestConstants.getInstance();

    private MappingManager manager;
    private Mapper<PagingStateDao> mapper;

    @Before
    public void init() {
        manager = PowerMockito.mock(MappingManager.class);
        mapper = PowerMockito.mock(Mapper.class);
        PowerMockito.when(manager.mapper(PagingStateDao.class)).thenReturn(mapper);
    }

    @Test
    public void testAdd() {
        PaginationQuery.add(manager, tc.PAGING_STATE);
        verify(mapper, atLeastOnce()).save(eq(tc.PAGING_STATE), anyObject());
    }

    @Test(expected = RuntimeException.class)
    public void testAddException() {
        PaginationQuery.add(null, tc.PAGING_STATE);
    }

    @Test
    public void testGet() {
        PowerMockito.when(mapper.get(any(UUID.class))).thenReturn(tc.PAGING_STATE);
        Optional<PagingStateDao> result = PaginationQuery.get(manager, tc.PAGING_ID);
        verify(mapper, atLeastOnce()).get(tc.PAGING_ID);
        assertEquals(result.get(), tc.PAGING_STATE);
    }

    @Test
    public void testGetNullPagingState() {
        Optional<PagingStateDao> result = PaginationQuery.get(manager, tc.PAGING_ID);
        PowerMockito.when(mapper.get(tc.PAGING_ID)).thenReturn(null);
        verify(mapper, atLeastOnce()).get(tc.PAGING_ID);
        assertFalse(result.isPresent());
    }

    @Test
    public void testRemove() {
        PaginationQuery.remove(manager, tc.PAGING_ID);
        verify(mapper, atLeastOnce()).delete(tc.PAGING_ID);
    }

    @Test(expected = RuntimeException.class)
    public void testRemoveException() {
        PaginationQuery.remove(null, tc.PAGING_ID);
    }

}
