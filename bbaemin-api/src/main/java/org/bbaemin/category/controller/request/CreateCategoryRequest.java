package org.bbaemin.category.controller.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateCategoryRequest {

    private Integer code;
    private String name;
    private String description;

    private Long parentId;    // 상위 카테고리
}
