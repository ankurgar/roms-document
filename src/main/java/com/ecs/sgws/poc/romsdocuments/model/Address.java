package com.ecs.sgws.poc.romsdocuments.model;

import com.redis.om.spring.annotations.Indexed;
import com.redis.om.spring.annotations.SchemaFieldType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

  @Indexed(schemaFieldType = SchemaFieldType.TAG, sortable = true)
  private String city;

}
