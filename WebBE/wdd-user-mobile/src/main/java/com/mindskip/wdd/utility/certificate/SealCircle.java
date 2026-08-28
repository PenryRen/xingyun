package com.mindskip.wdd.utility.certificate;

import lombok.Data;

@Data
public class SealCircle {
    private Integer line;
    private Integer width;
    private Integer height;

    public SealCircle(Integer line, Integer width, Integer height) {
        this.line = line;
        this.width = width;
        this.height = height;
    }
}
