package com.diepchu.demo.controller;

import com.diepchu.demo.domain.Resume;
import com.diepchu.demo.domain.response.ResultPaginationDTO;
import com.diepchu.demo.domain.response.resume.ResCreateResumeDTO;
import com.diepchu.demo.domain.response.resume.ResFetchResumeDTO;
import com.diepchu.demo.domain.response.resume.ResUpdateResumeDTO;
import com.diepchu.demo.service.ResumeService;
import com.diepchu.demo.util.anotation.ApiMessage;
import com.diepchu.demo.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {

    private final ResumeService resumeService;
    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    @ApiMessage("Create resume")
    public ResponseEntity<ResCreateResumeDTO> create(@Valid @RequestBody Resume resume) throws IdInvalidException{
        boolean isIdExist = this.resumeService.checkResumeExistByUserAndJob(resume);
        if(!isIdExist){
            throw new IdInvalidException("User does not exist");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.create(resume));
    }

    @PutMapping("/resumes")
    @ApiMessage("Update a resume")
    public ResponseEntity<ResUpdateResumeDTO> update(@RequestBody Resume resume) throws IdInvalidException{
        Optional<Resume> reqResumeOptional = this.resumeService.fetchById(resume.getId());
        if(reqResumeOptional.isEmpty()){
            throw new IdInvalidException("Resume does not exist");
        }

        Resume reqResume = reqResumeOptional.get();
        reqResume.setStatus(resume.getStatus());

        return ResponseEntity.ok().body(this.resumeService.update(reqResume));
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete a resume by id")
    public ResponseEntity<Void> delete(@PathVariable long id) throws IdInvalidException{
        Optional<Resume> reqResumeOptional = this.resumeService.fetchById(id);
        if(reqResumeOptional.isEmpty()){
            throw new IdInvalidException("Resume does not exist");
        }
        this.resumeService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Fetch a resume by id")
    public ResponseEntity<ResFetchResumeDTO> fetchById(@PathVariable long id) throws IdInvalidException{
        Optional<Resume> reqResumeOptional = this.resumeService.fetchById(id);
        if(reqResumeOptional.isEmpty()){
            throw new IdInvalidException("Resume does not exist");
        }
        return ResponseEntity.ok().body(this.resumeService.getResume(reqResumeOptional.get()));
    }

    @GetMapping("/resumes")
    @ApiMessage("Fetch all resume with paginate")
    public ResponseEntity<ResultPaginationDTO> fetchAll(
            @Filter Specification<Resume> spec,
            Pageable pageable
            ){
        return ResponseEntity.ok().body(this.resumeService.fetchAllResume(spec, pageable));
    }

}
