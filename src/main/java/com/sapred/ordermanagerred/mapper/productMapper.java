package com.sapred.ordermanagerred.mapper;
import com.sapred.ordermanagerred.dto.ProductDTO;
import com.sapred.ordermanagerred.dto.ProductNameDTO;
import com.sapred.ordermanagerred.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;
@Mapper(componentModel = "spring")
public interface productMapper {
    productMapper INSTANCE = Mappers.getMapper(productMapper.class);
    @Mapping(source = "discountType", target = "discountType")
    @Mapping(source = "productCategoryId.name", target = "productCategoryName")
    ProductDTO productToDto(Product p);
    @Mapping(source = "productCategoryName", target = "productCategoryId.name")
    @Mapping(source = "discountType", target = "discountType")
    Product dtoToProduct(ProductDTO p);
    List<ProductDTO> productToDto(List<Product> list);
    List<Product> dtoToProduct(List<ProductDTO> list);
    @Mapping(source = "id", target = "id")
    ProductNameDTO productToProductNameDto(Product p);
    List<ProductNameDTO> productToProductNameDto(List<Product> list);
}