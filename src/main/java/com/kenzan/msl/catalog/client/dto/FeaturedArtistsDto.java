/*
 * Copyright 2015, Kenzan, All rights reserved.
 */
package com.kenzan.msl.catalog.client.dto;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.kenzan.msl.common.dto.AbstractArtistDto;

import java.util.UUID;

/**
 *
 *
 * @author kenzan
 */
@Table(name = "featured_artists")
public class FeaturedArtistsDto extends AbstractArtistDto {
  @PartitionKey(value = 0)
  @Column(name = "hotness_bucket")
  private String hotnessBucket;
  @PartitionKey(value = 1)
  @Column(name = "content_type")
  private String contentType;
  @Column(name = "hotness_value")
  private Float hotnessValue;
  @Column(name = "artist_id")
  private UUID artistId;
  @Column(name = "artist_name")
  private String artistName;
  @Column(name = "artist_mbid")
  private UUID artistMbid;
  @Column(name = "image_link")
  private String imageLink;

  /**
   * @return the image url
   */
  public String getImageLink() {
    return imageLink;
  }

  /**
   * @param imageLink url of the image
   */
  public void setImageLink(String imageLink) {
    this.imageLink = imageLink;
  }

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
