package com.kenzan.msl.catalog.client.config;
import com.google.inject.AbstractModule;
import com.kenzan.msl.catalog.client.services.CatalogDataClientService;
import com.kenzan.msl.catalog.client.services.CatalogDataClientServiceStub;

/**
 * @author Kenzan
 */
public class LocalCatalogDataCientModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(CatalogDataClientService.class).to(CatalogDataClientServiceStub.class).asEagerSingleton();
  }
}
