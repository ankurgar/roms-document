package com.ecs.sgws.poc.romsdocuments.model;


import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.IndexCreationMode;
import com.redis.om.spring.annotations.Indexed;
import com.redis.om.spring.annotations.IndexingOptions;
import com.redis.om.spring.annotations.SchemaFieldType;
import com.redis.om.spring.annotations.Searchable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;

@Data
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Document(value = "cmpny:")
@IndexingOptions(indexName = "idx:company", creationMode = IndexCreationMode.DROP_AND_RECREATE)
public class Company {

  @Id
  private String id;

  @NonNull
  @Searchable
  private String name;

  @Indexed(schemaFieldType = SchemaFieldType.TAG, sortable = true)
  private Set<String> tags = new HashSet<String>();

  @NonNull
  private String url;

  @NonNull
  @Indexed(schemaFieldType = SchemaFieldType.GEO, sortable = true)
  private Point location;

  @NonNull
  @Indexed(schemaFieldType = SchemaFieldType.NUMERIC, sortable = true)
  private Integer numberOfEmployees;

  @NonNull
  @Indexed(schemaFieldType = SchemaFieldType.NUMERIC, sortable = true)
  private Integer yearFounded;

  private boolean publiclyListed;

  /**
   * It does not creates the index on nested fields within Phones class.
   */
  @Indexed(schemaFieldType = SchemaFieldType.NESTED, sortable = false)
  private List<Phones> phonesList;

  @Indexed(schemaFieldType = SchemaFieldType.NESTED, sortable = false)
  private Address address;
}