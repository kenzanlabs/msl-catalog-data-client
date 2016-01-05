# MSL catalog data client

## Overview
Data Client Layer
Simplification to a traditional edge/middle architecture, this project uses a edge/data client architecture instead.
The data clients are jars, each containing all the methods and DAOs for accessing all the tables within a Cassandra cluster.
To enhance scalability and configuration flexibility, the Cassandra tables are split into three independent clusters: account, catalog, and rating.
Each of these clusters has a data client jar dedicated to accessing it: account-data-client, catalog-data-client, and rating-data-client, respectively.

So a microservice that needs to access Cassandra data will include one or more of the data client jars.

| Table           | Method  |
|:-------------:| -----:|
| **featured_songs** |Observable<ResultSet> getFeaturedSongs(Optional<Integer> limit) |
| | Observable<Result<FeaturedSongsDao> map(Observable<ResultSet>) |
| **songs_by_facet** |Observable<ResultSet> getSongsByFacet(String facetName, Optional<Integer> limit) |
| |Observable<Result<SongsByFacetDao> map(Observable<ResultSet>) |
| **featured_albums** |Observable<ResultSet> getFeaturedAlbums(Optional<Integer> limit)|
| |Observable<Result<FeaturedAlbumsDao> map(Observable<ResultSet>) |
| **albums_by_facet** | Observable<ResultSet> getAlbumsByFacet(String facetName, Optional<Integer> limit) |
| | Observable<Result<AlbumsByFacetDao> map(Observable<ResultSet>) |
| **featured_artists** | Observable<ResultSet> getFeaturedArtists(Optional<Integer> limit) |
| | Observable<Result<FeaturedArtistsDao> map(Observable<ResultSet>) |
| **artists_by_facet** | Observable<ResultSet> getArtistsByFacet(String facetName, Optional<Integer> limit) |
| | Observable<Result<ArtistsByFacetDao> map(Observable<ResultSet>) |
| **songs_albums_by_artist** | Observable<ResultSet> getSongsAlbumsByArtist(UUID artistUuid, Optional<Integer> limit) |
| | Observable<Result<SongsAlbumsByArtistDao> map(Observable<ResultSet>) |
| **songs_artist_by_album** | Observable<ResultSet> getSongsArtistByAlbum(UUID albumUuid, Optional<Integer> limit) |
| | Observable<Result<SongsArtistByAlbumDao> map(Observable<ResultSet>) |
| **album_artist_by_song** | Observable<ResultSet> getAlbumArtistBySong(UUID songUuid, Optional<Integer> limit) |
| | Observable<Result<AlbumArtistBySongDao> map(Observable<ResultSet>) |
| **paging_state** | addOrUpdatePagingState(PagingStateDao) |
| | Observable<PagingStateDao> getPagingState(UUID pagingStateUuid) |
| | deletePagingState(UUID pagingStateUuid) |

## Packaging & Installation

```bash 
mvn clean package && mvn -P install compile
```