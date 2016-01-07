/*
 * Copyright 2015, Kenzan, All rights reserved.
 */
package com.kenzan.msl.catalog.client.dao;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.kenzan.msl.common.dao.AbstractSongDao;

import java.util.UUID;

/**
 *
 *
 * @author billschwanitz
 */
@Table(name = "featured_songs")
public class FeaturedSongsDao extends AbstractSongDao {
    @PartitionKey(value = 0)
    @Column(name = "hotness_bucket")
    private String hotnessBucket;
    @PartitionKey(value = 1)
    @Column(name = "content_type")
    private String contentType;
    @Column(name = "hotness_value")
    private Float hotnessValue;
    @Column(name = "song_id")
    private UUID songId;
    @Column(name = "song_name")
    private String songName;
    @Column(name = "song_duration")
    private Integer songDuration;
    @Column(name = "album_id")
    private UUID albumId;
    @Column(name = "album_name")
    private String albumName;
    @Column(name = "album_year")
    private Integer albumYear;
    @Column(name = "artist_id")
    private UUID artistId;
    @Column(name = "artist_name")
    private String artistName;
    @Column(name = "artist_mbid")
    private UUID artistMbid;

    /**
     * @return the hotnessBucket
     */
    public String getHotnessBucket() {
        return hotnessBucket;
    }

    /**
     * @param hotnessBucket the hotnessBucket to set
     */
    public void setHotnessBucket(String hotnessBucket) {
        this.hotnessBucket = hotnessBucket;
    }

    /**
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @return the hotnessValue
     */
    public Float getHotnessValue() {
        return hotnessValue;
    }

    /**
     * @param hotnessValue the hotnessValue to set
     */
    public void setHotnessValue(Float hotnessValue) {
        this.hotnessValue = hotnessValue;
    }

    /**
     * @return the songId
     */
    @Override
    public UUID getSongId() {
        return songId;
    }

    /**
     * @param songId the songId to set
     */
    @Override
    public void setSongId(UUID songId) {
        this.songId = songId;
    }

    /**
     * @return the songName
     */
    @Override
    public String getSongName() {
        return songName;
    }

    /**
     * @param songName the songName to set
     */
    @Override
    public void setSongName(String songName) {
        this.songName = songName;
    }

    /**
     * @return the songDuration
     */
    @Override
    public Integer getSongDuration() {
        return songDuration;
    }

    /**
     * @param songDuration the songDuration to set
     */
    @Override
    public void setSongDuration(Integer songDuration) {
        this.songDuration = songDuration;
    }

    /**
     * @return the albumId
     */
    @Override
    public UUID getAlbumId() {
        return albumId;
    }

    /**
     * @param albumId the albumId to set
     */
    @Override
    public void setAlbumId(UUID albumId) {
        this.albumId = albumId;
    }

    /**
     * @return the albumName
     */
    @Override
    public String getAlbumName() {
        return albumName;
    }

    /**
     * @param albumName the albumName to set
     */
    @Override
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    /**
     * @return the albumYear
     */
    @Override
    public Integer getAlbumYear() {
        return albumYear;
    }

    /**
     * @param albumYear the albumYear to set
     */
    @Override
    public void setAlbumYear(Integer albumYear) {
        this.albumYear = albumYear;
    }

    /**
     * @return the artistId
     */
    @Override
    public UUID getArtistId() {
        return artistId;
    }

    /**
     * @param artistId the artistId to set
     */
    @Override
    public void setArtistId(UUID artistId) {
        this.artistId = artistId;
    }

    /**
     * @return the artistName
     */
    @Override
    public String getArtistName() {
        return artistName;
    }

    /**
     * @param artistName the artistName to set
     */
    @Override
    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    /**
     * @return the artistMbid
     */
    @Override
    public UUID getArtistMbid() {
        return artistMbid;
    }

    /**
     * @param artistMbid the artistMbid to set
     */
    @Override
    public void setArtistMbid(UUID artistMbid) {
        this.artistMbid = artistMbid;
    }
}