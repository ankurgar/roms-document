package com.ecs.sgws.poc.romsdocuments.model;

import com.redis.om.spring.annotations.Indexed;
import com.redis.om.spring.annotations.SchemaFieldType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Phones {

  @Indexed(schemaFieldType = SchemaFieldType.TAG, sortable = true)
  private String number;
}
