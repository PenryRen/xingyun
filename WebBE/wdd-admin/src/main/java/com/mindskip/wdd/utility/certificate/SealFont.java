package com.mindskip.wdd.utility.certificate;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
public class SealFont {
    private String text;
    private String family = "思源黑體舊字形 Normal";
    private Integer size;
    private Boolean bold = false;
    private Double space;
    private Integer margin;

    public SealFont append(String text) {
        this.text += text;
        return this;
    }

    public SealFont(String text, Integer size, Boolean bold, Double space, Integer margin) {
        this.text = text;
        this.size = size;
        this.bold = bold;
        this.space = space;
        this.margin = margin;
    }

    public SealFont(String text, Integer size) {
        this.text = text;
        this.size = size;
    }

    public SealFont(String text, Integer size, Double space, Integer margin) {
        this.text = text;
        this.size = size;
        this.space = space;
        this.margin = margin;
    }
}
