package com.diepchu.demo.service;

import com.diepchu.demo.domain.Company;
import com.diepchu.demo.domain.dto.Meta;
import com.diepchu.demo.domain.dto.ResultPaginationDTO;
import com.diepchu.demo.repository.CompanyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(Company company) {
        return companyRepository.save(company);
    }

    public Company fetchCompanyById(long id) {
        Optional<Company> company = companyRepository.findById(id);
        return company.orElse(null);
    }

    public Company handleUpdateCompany(Company updatedCompany) {
        return companyRepository.findById(updatedCompany.getId())
                .map(company -> {
                    Company newCompany = updatedCompany.toBuilder()
                            .address(updatedCompany.getAddress())
                            .logo(updatedCompany.getLogo())
                            .description(updatedCompany.getDescription())
                            .name(updatedCompany.getName())
                            .build();
                    return companyRepository.save(newCompany);

                }).orElse(null);

    }

    public ResultPaginationDTO fetchAllCompanies(Specification<Company> specification,Pageable pageable) {
        Page<Company> companies = this.companyRepository.findAll(specification, pageable);
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber()+1)
                .pageSize(pageable.getPageSize())
                .total(companies.getTotalElements())
                .pages(companies.getTotalPages())
                .build();

        ResultPaginationDTO result = ResultPaginationDTO.builder()
                .meta(meta)
                .data(companies.getContent())
                .build();
        return result;
    }

    public void handleDeleteCompany(long id) {
        this.companyRepository.deleteById(id);
    }
}
