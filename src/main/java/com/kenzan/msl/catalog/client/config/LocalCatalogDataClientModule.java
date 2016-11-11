package com.kenzan.msl.catalog.client.config;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.kenzan.msl.catalog.client.cassandra.query.PaginationQuery;
import com.kenzan.msl.catalog.client.services.CatalogDataClientService;
import com.kenzan.msl.catalog.client.services.CatalogDataClientServiceImpl;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * @author Kenzan
 */
public class LocalCatalogDataClientModule extends AbstractModule {

  private static final String DEFAULT_MSL_KEYSPACE = "msl";
  private static final String DEFAULT_MSL_REGION = "us-west-2";
  private static final String DEFAULT_CLUSTER = "127.0.0.1";
  private static final int DEFAULT_TTL_SECS = 60 * 60; // 1 hour;

  private DynamicStringProperty keyspace = DynamicPropertyFactory.getInstance().getStringProperty("keyspace", DEFAULT_MSL_KEYSPACE);
  private DynamicStringProperty domain = DynamicPropertyFactory.getInstance().getStringProperty("domain", DEFAULT_CLUSTER);
  private DynamicStringProperty region = DynamicPropertyFactory.getInstance().getStringProperty("region", DEFAULT_MSL_REGION);
  private static DynamicIntProperty ttlSecs = DynamicPropertyFactory.getInstance().getIntProperty("paging_state_ttl_secs", DEFAULT_TTL_SECS);

  private final static Logger LOGGER = LoggerFactory.getLogger(CatalogDataClientModule.class);

  @Override
  protected void configure() {
    bindConstant().annotatedWith(Names.named("ttlSecs")).to(ttlSecs.getValue());
    requestStaticInjection(PaginationQuery.class);
    bind(CatalogDataClientService.class).to(CatalogDataClientServiceImpl.class).asEagerSingleton();
  }

  @Provides
  @Singleton
  public MappingManager getMappingManager () {
    configureArchaius();
    Cluster.Builder clusterBuilder = Cluster.builder();
    String domainValue = domain.getValue();
    if (StringUtils.isNotEmpty(domainValue)) {
      String[] clusterNodes = StringUtils.split(domainValue, ",");
      for (String node : clusterNodes) {
        clusterBuilder.addContactPoint(node);
      }
    }

    Cluster cluster = clusterBuilder.build();
    Session session = cluster.connect(keyspace.getValue());

    LOGGER.debug(String.format("Keyspace: {%s}, domain: {%s}, region: {%s}", keyspace.getValue(), domainValue, region.getValue()));

    return new MappingManager(session);
  }

  @PostConstruct
  private void configureArchaius() {
    Properties props = System.getProperties();
    String ENV = props.getProperty("env");
    if (StringUtils.isEmpty(ENV) || ENV.toLowerCase().contains("local")) {
      String configUrl = "file://" + System.getProperty("user.dir") + "/../msl-catalog-data-client-config/data-client-config.properties";
      System.setProperty("archaius.configurationSource.additionalUrls", configUrl);
    }
  }
}
