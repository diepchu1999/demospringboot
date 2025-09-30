package com.diepchu.demo.service;

import com.diepchu.demo.domain.Job;
import com.diepchu.demo.domain.Resume;
import com.diepchu.demo.domain.User;
import com.diepchu.demo.domain.response.Meta;
import com.diepchu.demo.domain.response.ResultPaginationDTO;
import com.diepchu.demo.domain.response.resume.ResCreateResumeDTO;
import com.diepchu.demo.domain.response.resume.ResFetchResumeDTO;
import com.diepchu.demo.domain.response.resume.ResUpdateResumeDTO;
import com.diepchu.demo.repository.JobRepository;
import com.diepchu.demo.repository.ResumeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final JobRepository jobRepository;
    public ResumeService(ResumeRepository resumeRepository,  JobRepository jobRepository) {
        this.resumeRepository = resumeRepository;
        this.jobRepository = jobRepository;
    }

    public boolean checkResumeExistByUserAndJob(Resume resume){
        if(resume.getUser() ==  null) {
            return false;
        }

        Optional<Resume> resumeOptional = this.resumeRepository.findById(resume.getUser().getId());
        if(resumeOptional.isEmpty()){
            return false;
        }
        if(resume.getJob() == null) {
            return false;
        }

        Optional<Job> jobOptional = this.jobRepository.findById(resume.getJob().getId());
        if(jobOptional.isEmpty()){
            return false;
        }
        return true;
    }
    public ResCreateResumeDTO create(Resume resume){
        resume = this.resumeRepository.save(resume);

        ResCreateResumeDTO res = new ResCreateResumeDTO();
        res.setId(resume.getId());
        res.setCreatedBy(resume.getCreatedBy());
        res.setCreatedAt(resume.getCreatedAt());

        return res;
    }

    public Optional<Resume> fetchById(long id){
        return this.resumeRepository.findById(id);
    }

    public ResUpdateResumeDTO update(Resume resume){
        resume = this.resumeRepository.save(resume);
        ResUpdateResumeDTO res = new ResUpdateResumeDTO();
        res.setUpdatedAt(resume.getUpdatedAt());
        res.setUpdatedBy(resume.getUpdatedBy());
        return res;
    }

    public void delete (long id){
        this.resumeRepository.deleteById(id);
    }

    public ResFetchResumeDTO getResume (Resume resume){
        ResFetchResumeDTO res = new ResFetchResumeDTO();
        res.setId(resume.getId());
        res.setEmail(resume.getEmail());
        res.setUrl(resume.getUrl());
        res.setStatus(resume.getStatus());
        res.setCreatedAt(resume.getCreatedAt());
        res.setCreatedBy(resume.getCreatedBy());
        res.setUpdatedAt(resume.getUpdatedAt());
        res.setUpdatedBy(resume.getUpdatedBy());
        if(resume.getJob() != null) {
            res.setCompanyName(resume.getJob().getCompany().getName());
        }
        res.setUser(new ResFetchResumeDTO.UserResume(resume.getUser().getId(), resume.getUser().getName()));
        res.setJob(new ResFetchResumeDTO.JobResume(resume.getJob().getId(), resume.getJob().getName()));
        return res;
    }

    public ResultPaginationDTO fetchAllResume(Specification<Resume> specification, Pageable pageable){
        Page<Resume> pageResume = this.resumeRepository.findAll(specification, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageResume.getTotalPages());
        meta.setTotal(pageResume.getTotalElements());

        res.setMeta(meta);
        List<ResFetchResumeDTO> listResume = pageResume.getContent().stream().map(item -> this.getResume(item))
                .collect(Collectors.toList());

        res.setResult(listResume);
        return res;
    }


}
