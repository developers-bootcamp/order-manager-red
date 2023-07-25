package com.sapred.ordermanagerred.resolver;

import com.sapred.ordermanagerred.model.Company;
import com.sapred.ordermanagerred.model.DiscountType;
import com.sapred.ordermanagerred.model.Product;
import com.sapred.ordermanagerred.model.User;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class ProductResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Product.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return Product.builder().id("1")
                .companyId(Company.builder().id("1").build())
                .price(70)
                .discountType(DiscountType.PERCENTAGE)
                .discount(20).build();
    }
}
