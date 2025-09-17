package com.diepchu.demo.service;

import com.diepchu.demo.domain.Company;
import com.diepchu.demo.repository.CompanyRepository;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(Company company) {
        return companyRepository.save(company);
    }
}
