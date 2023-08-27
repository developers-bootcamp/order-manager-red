package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.model.Company;
import com.sapred.ordermanagerred.repository.CompanyRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Optional;

@Service
@Slf4j
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @SneakyThrows
    public Company getCompany(String id) {
        log.info("Retrieving company with ID: {}", id);

        Optional<Company> companyOptional = companyRepository.findById(id);
        if (companyOptional.isEmpty()) {
            log.error("Company with ID '{}' not found", id);
            throw new NotFoundException("Company not found");
        }

        Company company = companyOptional.get();
        log.info("Company retrieved: {}", company);

        return company;
    }
}
