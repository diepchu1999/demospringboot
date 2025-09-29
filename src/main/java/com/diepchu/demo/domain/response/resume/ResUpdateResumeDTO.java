package com.diepchu.demo.domain.response.resume;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Instant;

@Getter
@Setter
public class ResUpdateResumeDTO {
    private Instant updatedAt;
    private String updatedBy;
}
