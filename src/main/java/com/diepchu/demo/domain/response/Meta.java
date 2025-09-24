package com.diepchu.demo.domain.response;

import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Meta {
    private int page;
    private int pageSize;
    private int pages;
    private long total;
}
