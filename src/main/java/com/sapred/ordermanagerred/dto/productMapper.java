package com.sapred.ordermanagerred.dto;

import com.sapred.ordermanagerred.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring")
public interface productMapper {
    productMapper INSTANCE = Mappers.getMapper(productMapper.class);

    @Mapping(source = "productCategoryId.id",target = "productCategoryId")
    ProductDTO productToDto(Product p);

    @Mapping(source = "productCategoryId",target = "productCategoryId.id")
    Product dtoToProduct(ProductDTO p);

    List<ProductDTO> productToDto(List<Product> list);
    List<Product> dtoToProduct(List<ProductDTO> list);



    @Mapping(source = "id",target = "id")

    ProductNameDTO productToProductNameDto(Product p);

    //Product dtoToProductName(ProductNameDTO p);

    List<ProductNameDTO> productToProductNameDto(List<Product> list);
   // List<Product> dtoToProductName(List<ProductNameDTO> list);

}
