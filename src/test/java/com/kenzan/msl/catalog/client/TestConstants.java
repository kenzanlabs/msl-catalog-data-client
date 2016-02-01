package com.kenzan.msl.catalog.client;

import com.kenzan.msl.catalog.client.dto.PagingStateDto;

import java.nio.ByteBuffer;
import java.util.UUID;

public class TestConstants {

    private static TestConstants instance = null;

    public PagingStateDto PAGING_STATE;
    public String FACET = "2";
    public UUID SONG_ID = UUID.fromString("ec217f7e-01f4-415b-b8ff-0b3b020e485f");
    public UUID ARTIST_ID = UUID.fromString("ec217f7e-01f4-415b-b8ff-0b3b020e485f");
    public UUID USER_ID = UUID.fromString("ec217f7e-01f4-415b-b8ff-0b3b020e485f");
    public UUID ALBUM_ID = UUID.fromString("389f9181-99f9-4377-9114-c63b53245355");
    public UUID PAGING_ID = UUID.fromString("389f9181-99f9-4377-9114-c63b53245355");
    public int LIMIT = 5;

    private TestConstants() {
        PAGING_STATE = new PagingStateDto();
        PAGING_STATE.setUserId(USER_ID);
        PagingStateDto.PagingStateUdt lala = new PagingStateDto.PagingStateUdt();
        lala.setPageState(ByteBuffer.allocate(10));
        PAGING_STATE.setPagingState(lala);
    }

    public static TestConstants getInstance() {
        if ( instance == null ) {
            instance = new TestConstants();
        }
        return instance;
    }
}
