package com.ecs.sgws.poc.romsdocuments.model;

import com.redis.om.spring.annotations.Aggregation;
import com.redis.om.spring.annotations.Query;
import com.redis.om.spring.repository.RedisDocumentRepository;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.query.Param;


public interface CompanyRepository extends RedisDocumentRepository<Company, String> {
  // find one by property
  Optional<Company> findOneByName(String name);

  // geospatial query
  Iterable<Company> findByLocationNear(Point point, Distance distance);

  Page<Company> findByLocationNear(Point point, Distance distance, Pageable pageable);

  // find by numeric property
  Iterable<Company> findByNumberOfEmployees(int noe);

  // find by numeric property range
  Iterable<Company> findByNumberOfEmployeesBetween(int noeGT, int noeLT);

  // starting with/ending with
  Iterable<Company> findByNameStartingWith(String prefix);

  // find by tag field, using JRediSearch "native" annotation
  @Query("@tags:{$tags}")
  Page<Company> findByTags(@Param("tags") Set<String> tags, Pageable pageable);

  @Query("@tags:{$tags} @id:{$ids}")
  Page<Company> findByTagsAndIds(@Param("tags") Set<String> tags, @Param("ids") Set<String> ids, Pageable pageable);

  @Aggregation("@tags:{$tags}")
  Page<Company> findNearByTags(Point point, Distance distance, @Param("tags") Set<String> tags, Pageable pageable);
}
