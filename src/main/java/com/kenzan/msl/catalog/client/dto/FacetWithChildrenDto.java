/*
 * Copyright 2015, Kenzan, All rights reserved.
 */
package com.kenzan.msl.catalog.client.dto;

import java.util.List;

/**
 * @author kenzan
 */
public class FacetWithChildrenDto extends FacetDto {
  private List<FacetDto> children;

  /**
   * @return the children
   */
  public List<FacetDto> getChildren() {
    return children;
  }

  /**
   * @param children the children to set
   */
  public void setChildren(List<FacetDto> children) {
    this.children = children;
  }
}
