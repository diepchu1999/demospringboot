package com.diepchu.demo.controller;

import com.diepchu.demo.domain.Skill;
import com.diepchu.demo.domain.response.ResultPaginationDTO;
import com.diepchu.demo.service.SkillService;
import com.diepchu.demo.util.anotation.ApiMessage;
import com.diepchu.demo.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @ApiMessage("Create a skill")
    public ResponseEntity<Skill> createSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        if (skill.getName() != null && this.skillService.isNameExist(skill.getName())) {
            throw new IdInvalidException("Skill name = " + skill.getName() + " đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.createSkill(skill));
    }

    @PutMapping("/skills")
    @ApiMessage("Update a skill")
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {

        Skill currentSkill = this.skillService.fetchSkillById(skill.getId());
        if (currentSkill == null) {
            throw new IdInvalidException("Skill id = " + currentSkill.getId() + " không tồn tại");
        }

        if (skill.getName() != null && this.skillService.isNameExist(skill.getName())) {
            throw new IdInvalidException("Skill name = " + skill.getName() + " đã tồn tại");
        }

        currentSkill.setName(skill.getName());
        return ResponseEntity.ok().body(this.skillService.updateSkill(currentSkill));
    }

    @GetMapping("/skills")
    @ApiMessage("fetch all  skills")
    public ResponseEntity<ResultPaginationDTO> getAll(@Filter Specification<Skill> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.fetchAllSkills(spec, pageable));
    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("delete a skill")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) throws IdInvalidException {
        Skill currentSkill = this.skillService.fetchSkillById(id);
        if (currentSkill == null) {
            throw new IdInvalidException("Skill name = " + currentSkill.getName() + " đã tồn tại");
        }
        this.skillService.deleteSkill(id);
        return ResponseEntity.ok().body(null);
    }
}
