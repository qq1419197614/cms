package com.lalala.cms.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Result {
    @Id
    private String id;
    private String fenlei;
    private String title;
    private String content;
}
