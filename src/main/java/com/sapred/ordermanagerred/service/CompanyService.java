package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.model.Company;
import com.sapred.ordermanagerred.repository.CompanyRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    @SneakyThrows
    public Company getCompany(String id) {
        Optional<Company> companyOptional = companyRepository.findById(id);
        Company company = companyOptional.orElseThrow(() -> new Exception("Company not found"));
        return company;
    }
}
