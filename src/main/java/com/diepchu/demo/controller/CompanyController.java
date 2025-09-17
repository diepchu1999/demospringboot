package com.diepchu.demo.controller;

import com.diepchu.demo.domain.Company;
import com.diepchu.demo.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyController {

    private final CompanyService companyService;
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Object> createCompany(@Valid  @RequestBody Company company){
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.handleCreateCompany(company));
    }
}
