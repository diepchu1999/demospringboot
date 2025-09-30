package com.diepchu.demo.service;

import com.diepchu.demo.domain.Company;
import com.diepchu.demo.domain.Job;
import com.diepchu.demo.domain.Skill;
import com.diepchu.demo.domain.response.Meta;
import com.diepchu.demo.domain.response.ResultPaginationDTO;
import com.diepchu.demo.domain.response.job.ResCreateJobDTO;
import com.diepchu.demo.domain.response.job.ResUpdateJobDTO;
import com.diepchu.demo.repository.CompanyRepository;
import com.diepchu.demo.repository.JobRepository;
import com.diepchu.demo.repository.SkillRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final CompanyRepository companyRepository;
    public  JobService(JobRepository jobRepository, SkillRepository skillRepository,  CompanyRepository companyRepository)
    {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.companyRepository = companyRepository;
    }

    public ResCreateJobDTO create(Job job){
        if(job.getSkills() != null){
            List<Long> reqSkills = job.getSkills()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Skill> skills = this.skillRepository.findByIdIn(reqSkills);
            job.setSkills(skills);
        }
        if(job.getCompany() != null){
            Optional<Company> company = this.companyRepository.findById(job.getCompany().getId());
            if(company.isPresent()){
                job.setCompany(company.get());
            }
        }

        Job newJob = jobRepository.save(job);
        ResCreateJobDTO resCreateJobDTO = new ResCreateJobDTO();
        resCreateJobDTO.setId(newJob.getId());
        resCreateJobDTO.setName(newJob.getName());
        resCreateJobDTO.setSalary(newJob.getSalary());
        resCreateJobDTO.setQuantity(newJob.getQuantity());
        resCreateJobDTO.setLocation(newJob.getLocation());
        resCreateJobDTO.setLevel(newJob.getLevel());
        resCreateJobDTO.setStartDate(newJob.getStartDate());
        resCreateJobDTO.setEndDate(newJob.getEndDate());
        resCreateJobDTO.setActive(newJob.isActive());
        resCreateJobDTO.setCreatedBy(newJob.getCreatedBy());
        resCreateJobDTO.setCreatedBy(newJob.getCreatedBy());

        if(newJob.getSkills() != null){
            List<String> skills = newJob.getSkills()
                    .stream()
                    .map(item -> item.getName())
                    .collect(Collectors.toList());
            resCreateJobDTO.setSkills(skills);
        }
        return resCreateJobDTO;
    }

    public ResUpdateJobDTO update(Job job, Job jobInDb){
        if(job.getSkills() != null){
            List<Long> reqSkills = job.getSkills()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Skill> skills = this.skillRepository.findByIdIn(reqSkills);
            jobInDb.setSkills(skills);
        }
        if(job.getCompany() != null){
            Optional<Company> companyOptional = this.companyRepository.findById(job.getCompany().getId());
            if(companyOptional.isPresent()){
                jobInDb.setCompany(companyOptional.get());
            }
        }
        jobInDb.setName(job.getName());
        jobInDb.setSalary(job.getSalary());
        jobInDb.setQuantity(job.getQuantity());
        jobInDb.setLocation(job.getLocation());
        jobInDb.setLevel(job.getLevel());
        jobInDb.setStartDate(job.getStartDate());
        jobInDb.setEndDate(job.getEndDate());
        jobInDb.setActive(job.isActive());

        Job newJob = jobRepository.save(jobInDb);
        ResUpdateJobDTO resUpdateJobDTO = new ResUpdateJobDTO();
        resUpdateJobDTO.setId(newJob.getId());
        resUpdateJobDTO.setName(newJob.getName());
        resUpdateJobDTO.setSalary(newJob.getSalary());
        resUpdateJobDTO.setQuantity(newJob.getQuantity());
        resUpdateJobDTO.setLocation(newJob.getLocation());
        resUpdateJobDTO.setLevel(newJob.getLevel());
        resUpdateJobDTO.setStartDate(newJob.getStartDate());
        resUpdateJobDTO.setEndDate(newJob.getEndDate());
        resUpdateJobDTO.setActive(newJob.isActive());
        resUpdateJobDTO.setUpdatedAt(newJob.getUpdatedAt());
        resUpdateJobDTO.setUpdatedBy(newJob.getUpdatedBy());

        if(newJob.getSkills() != null){
            List<String> skills = newJob.getSkills()
                    .stream()
                    .map(item -> item.getName())
                    .collect(Collectors.toList());
            resUpdateJobDTO.setSkills(skills);
        }
        return resUpdateJobDTO;
    }

    public void delete(long id){
        this.jobRepository.deleteById(id);
    }

    public Optional<Job> fetchJobById(long id) {
        return this.jobRepository.findById(id);
    }

    public ResultPaginationDTO fetchAll(Specification<Job> specification, Pageable pageable) {
        Page<Job> jobPage = this.jobRepository.findAll(specification, pageable);

        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber());
        meta.setPageSize(pageable.getPageSize());

        meta.setPage(jobPage.getNumber());
        meta.setTotal(jobPage.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(jobPage.getContent());
        return resultPaginationDTO;

    }
}
