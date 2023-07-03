package com.sapred.ordermanagerred.repository;

import com.sapred.ordermanagerred.dto.ProductCategoryDto;
import com.sapred.ordermanagerred.model.ProductCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AAAMapper {
    AAAMapper INSTANCE = Mappers.getMapper(AAAMapper.class);
    ProductCategoryDto productCategoryToProductCategoryDto(ProductCategory productCategory);
    ProductCategory productCategoryDtoToProductCategory(ProductCategoryDto productCategoryDto);
}
