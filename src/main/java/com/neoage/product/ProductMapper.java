package com.neoage.product;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

  ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

  Product toProduct(ProductDto dto);

  ProductDto toProductDto(Product product);

  @Mapping(target = "id", ignore = true)
  Product mergeToProduct(ProductDto dto, @MappingTarget Product product);
}
