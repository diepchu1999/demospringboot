package com.diepchu.demo.controller;

import com.diepchu.demo.domain.Company;
import com.diepchu.demo.domain.response.ResultPaginationDTO;
import com.diepchu.demo.service.CompanyService;
import com.diepchu.demo.util.anotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class CompanyController {

    private final CompanyService companyService;
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    @ApiMessage("create Company")
    public ResponseEntity<Object> createCompany(@Valid  @RequestBody Company company){
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.handleCreateCompany(company));
    }

    @PutMapping("/companies")
    @ApiMessage("update Company")
    public ResponseEntity<Object> updateCompany(@Valid  @RequestBody Company updatedCompany){
        return ResponseEntity.status(HttpStatus.OK).body(companyService.handleUpdateCompany(updatedCompany));
    }

    @GetMapping("/companies/{id}")
    @ApiMessage("fetch all Company")
    public ResponseEntity<Company> getCompany(@PathVariable long id){
        Company fetchCompanyById = companyService.fetchCompanyById(id);
        return ResponseEntity.status(HttpStatus.OK).body(fetchCompanyById);
    }

    @GetMapping("/companies")
    @ApiMessage("Get all Company")
    public ResponseEntity<ResultPaginationDTO> getAllCompany(@Filter Specification<Company> specification, Pageable pageable){

        return ResponseEntity.status(HttpStatus.OK).body(companyService.fetchAllCompanies(specification, pageable));
    }

    @DeleteMapping("/companies/{id}")
    @ApiMessage("Delete Company")
    public ResponseEntity<Void> deleteCompany(@PathVariable long id){
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.ok(null);
    }

}
