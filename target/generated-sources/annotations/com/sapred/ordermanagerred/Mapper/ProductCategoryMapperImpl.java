package com.sapred.ordermanagerred.Mapper;

import com.sapred.ordermanagerred.dto.ProductCategoryDto;
import com.sapred.ordermanagerred.model.ProductCategory;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-16T11:18:05+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class ProductCategoryMapperImpl implements ProductCategoryMapper {

    @Override
    public ProductCategoryDto productCategoryToProductCategoryDto(ProductCategory productCategory) {
        if ( productCategory == null ) {
            return null;
        }

        ProductCategoryDto productCategoryDto = new ProductCategoryDto();

        productCategoryDto.setId( productCategory.getId() );
        productCategoryDto.setName( productCategory.getName() );
        productCategoryDto.setDesc( productCategory.getDesc() );

        return productCategoryDto;
    }

    @Override
    public ProductCategory productCategoryDtoToProductCategory(ProductCategoryDto productCategoryDto) {
        if ( productCategoryDto == null ) {
            return null;
        }

        ProductCategory productCategory = new ProductCategory();

        productCategory.setId( productCategoryDto.getId() );
        productCategory.setName( productCategoryDto.getName() );
        productCategory.setDesc( productCategoryDto.getDesc() );

        return productCategory;
    }

    @Override
    public List<ProductCategoryDto> productCategoryDtoToProductCategory(List<ProductCategory> productCategories) {
        if ( productCategories == null ) {
            return null;
        }

        List<ProductCategoryDto> list = new ArrayList<ProductCategoryDto>( productCategories.size() );
        for ( ProductCategory productCategory : productCategories ) {
            list.add( productCategoryToProductCategoryDto( productCategory ) );
        }

        return list;
    }

    @Override
    public List<ProductCategory> productCategoryToProductCategoryDto(List<ProductCategoryDto> productCategoryDtos) {
        if ( productCategoryDtos == null ) {
            return null;
        }

        List<ProductCategory> list = new ArrayList<ProductCategory>( productCategoryDtos.size() );
        for ( ProductCategoryDto productCategoryDto : productCategoryDtos ) {
            list.add( productCategoryDtoToProductCategory( productCategoryDto ) );
        }

        return list;
    }
}
