package com.sapred.ordermanagerred.mapper;

import com.sapred.ordermanagerred.dto.ProductCategoryDto;
import com.sapred.ordermanagerred.model.ProductCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")

public interface ProductCategoryMapper {

    ProductCategoryMapper INSTANCE = Mappers.getMapper(ProductCategoryMapper.class);

    ProductCategoryDto productCategoryToProductCategoryDto(ProductCategory productCategory);

    ProductCategory productCategoryDtoToProductCategory(ProductCategoryDto productCategoryDto);

    List<ProductCategoryDto> productCategoryDtoToProductCategory(List<ProductCategory> productCategories);

    List<ProductCategory> productCategoryToProductCategoryDto(List<ProductCategoryDto> productCategoryDtos);


}
