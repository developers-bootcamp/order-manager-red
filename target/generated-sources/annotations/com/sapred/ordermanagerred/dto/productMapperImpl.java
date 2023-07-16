package com.sapred.ordermanagerred.dto;

import com.sapred.ordermanagerred.model.AuditData;
import com.sapred.ordermanagerred.model.Company;
import com.sapred.ordermanagerred.model.DiscountType;
import com.sapred.ordermanagerred.model.Product;
import com.sapred.ordermanagerred.model.ProductCategory;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-16T11:43:32+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class productMapperImpl implements productMapper {

    @Override
    public ProductDTO productToDto(Product p) {
        if ( p == null ) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();

        productDTO.setProductCategoryId( pProductCategoryIdId( p ) );
        productDTO.setName( p.getName() );
        productDTO.setDesc( p.getDesc() );
        productDTO.setPrice( p.getPrice() );
        productDTO.setInventory( p.getInventory() );

        return productDTO;
    }

    @Override
    public Product dtoToProduct(ProductDTO p) {
        if ( p == null ) {
            return null;
        }

        ProductCategory productCategoryId = null;
        String name = null;
        String desc = null;
        double price = 0.0d;
        int inventory = 0;

        productCategoryId = productDTOToProductCategory( p );
        name = p.getName();
        desc = p.getDesc();
        price = p.getPrice();
        inventory = p.getInventory();

        String id = null;
        double discount = 0.0d;
        DiscountType discountType = null;
        Company companyId = null;
        AuditData auditData = null;

        Product product = new Product( id, name, desc, price, discount, discountType, productCategoryId, inventory, companyId, auditData );

        return product;
    }

    @Override
    public List<ProductDTO> productToDto(List<Product> list) {
        if ( list == null ) {
            return null;
        }

        List<ProductDTO> list1 = new ArrayList<ProductDTO>( list.size() );
        for ( Product product : list ) {
            list1.add( productToDto( product ) );
        }

        return list1;
    }

    @Override
    public List<Product> dtoToProduct(List<ProductDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Product> list1 = new ArrayList<Product>( list.size() );
        for ( ProductDTO productDTO : list ) {
            list1.add( dtoToProduct( productDTO ) );
        }

        return list1;
    }

    @Override
    public ProductNameDTO productToProductNameDto(Product p) {
        if ( p == null ) {
            return null;
        }

        ProductNameDTO productNameDTO = new ProductNameDTO();

        productNameDTO.setId( p.getId() );
        productNameDTO.setName( p.getName() );

        return productNameDTO;
    }

    @Override
    public List<ProductNameDTO> productToProductNameDto(List<Product> list) {
        if ( list == null ) {
            return null;
        }

        List<ProductNameDTO> list1 = new ArrayList<ProductNameDTO>( list.size() );
        for ( Product product : list ) {
            list1.add( productToProductNameDto( product ) );
        }

        return list1;
    }

    private String pProductCategoryIdId(Product product) {
        if ( product == null ) {
            return null;
        }
        ProductCategory productCategoryId = product.getProductCategoryId();
        if ( productCategoryId == null ) {
            return null;
        }
        String id = productCategoryId.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected ProductCategory productDTOToProductCategory(ProductDTO productDTO) {
        if ( productDTO == null ) {
            return null;
        }

        ProductCategory productCategory = new ProductCategory();

        productCategory.setId( productDTO.getProductCategoryId() );

        return productCategory;
    }
}
