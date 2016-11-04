package com.kenzan.msl.catalog.client.config;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.kenzan.msl.catalog.client.archaius.ArchaiusHelper;
import com.kenzan.msl.catalog.client.services.CatalogDataClientService;
import com.kenzan.msl.catalog.client.services.CatalogDataClientServiceImpl;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kenzan
 */
public class CatalogDataClientModule extends AbstractModule {

  private static final String DEFAULT_MSL_KEYSPACE = "msl";
  private static final String DEFAULT_MSL_REGION = "us-west-2";
  private static final String DEFAULT_CLUSTER = "127.0.0.1";

  private final static Logger LOGGER = LoggerFactory.getLogger(CatalogDataClientModule.class);

  @Override
  protected void configure() {
    bind(CatalogDataClientService.class).to(CatalogDataClientServiceImpl.class).asEagerSingleton();
  }

  @Provides
  @Singleton
  public MappingManager getMappingManager () {

    ArchaiusHelper.setupArchaius();
    DynamicPropertyFactory propertyFactory = DynamicPropertyFactory.getInstance();

    DynamicStringProperty keyspace = propertyFactory.getStringProperty("keyspace", DEFAULT_MSL_KEYSPACE);
    DynamicStringProperty domain = propertyFactory.getStringProperty("domain", DEFAULT_CLUSTER);
    DynamicStringProperty region = propertyFactory.getStringProperty("region", DEFAULT_MSL_REGION);

    Cluster.Builder builder = Cluster.builder();
    String domainValue = domain.getValue();
    if (StringUtils.isNotEmpty(domainValue)) {
      String[] clusterNodes = StringUtils.split(domainValue, ",");
      for (String node : clusterNodes) {
        builder.addContactPoint(node);
      }
    }

    Cluster cluster = builder.build();
    Session session = cluster.connect(keyspace.getValue());

    LOGGER.debug(String.format("Keyspace: {%s}, domain: {%s}, region: {%s}", keyspace.getValue(), domainValue, region.getValue()));

    return new MappingManager(session);
  }
}
