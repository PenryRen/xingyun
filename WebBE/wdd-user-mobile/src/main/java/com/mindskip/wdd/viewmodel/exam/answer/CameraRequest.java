package com.mindskip.wdd.viewmodel.exam.answer;

import lombok.Data;

@Data
public class CameraRequest {
    private Long paperId;
    private Integer paperType;
    private String imageBase64;
}
