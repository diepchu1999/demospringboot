package com.diepchu.demo.domain.response;

import com.diepchu.demo.util.constant.GenderEnum;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant createdAt;
    private CompanyUser company;

    @Getter
    @Setter
    public static class CompanyUser{
        private long id;
        private String name;
    }
}
