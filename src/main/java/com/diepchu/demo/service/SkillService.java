package com.diepchu.demo.service;

import com.diepchu.demo.domain.Skill;
import com.diepchu.demo.domain.response.Meta;
import com.diepchu.demo.domain.response.ResultPaginationDTO;
import com.diepchu.demo.repository.SkillRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SkillService {
    private final SkillRepository skillRepository;
    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public boolean isNameExist(String name) {
        return skillRepository.existsByName(name);
    }

    public Skill fetchSkillById(long id) {
        Optional<Skill> skill = skillRepository.findById(id);
        if (skill.isPresent()) {
            return skill.get();
        }
        return null;
    }

    public Skill createSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    public Skill updateSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    public ResultPaginationDTO fetchAllSkills(Specification<Skill> specification, Pageable pageable) {
        Page<Skill> page = skillRepository.findAll(specification, pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        Meta meta = new Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPage(page.getTotalPages());
        meta.setTotal(page.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(page.getContent());
        return resultPaginationDTO;
    }

    public void deleteSkill(long id) {
        Optional<Skill> skill = skillRepository.findById(id);
        Skill currentSkill = skill.get();
        skillRepository.delete(currentSkill);
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));
        this.skillRepository.delete(skill.get());
    }

}
