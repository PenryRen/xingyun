package com.mindskip.wdd.utility.certificate;

import cn.hutool.core.img.ImgUtil;
import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.stream.Collectors;

/**
 * @version 7.1.0
 * @description: 证书工具类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public class ImageUtil {

    /**
     * 图片写入文字
     *
     * @param templateFile     the template file
     * @param credentialFile   the credential file
     * @param fontLocationList the font location list
     */
    public static void pressText(File templateFile, File credentialFile, java.util.List<FontLocation> fontLocationList) {
        Image srcImage = ImgUtil.read(templateFile);
        final BufferedImage targetImage = ImgUtil.toBufferedImage(srcImage);
        final Graphics2D graphics = targetImage.createGraphics();
        java.util.List<FontLocation> notSeaList = fontLocationList.stream().filter(item -> !item.getType().equals(6)).collect(Collectors.toList());
        java.util.List<FontLocation> seaList = fontLocationList.stream().filter(item -> item.getType().equals(6)).collect(Collectors.toList());
        notSeaList.addAll(seaList);
        notSeaList.forEach(item -> {
            switch (item.getType()) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 7:
                case 8:
                    if (null != item.getText()) {
                        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1f));
                        graphics.setColor(Color.decode(item.color));
                        Font font = new Font("思源黑體舊字形 Normal", item.getWeight() == 1 ? Font.PLAIN : Font.BOLD, item.getSize());
                        graphics.setFont(font);
                        FontMetrics fontMetrics = graphics.getFontMetrics();
                        graphics.drawString(item.getText(), item.getX(), item.getY() + fontMetrics.getAscent() - fontMetrics.getDescent());
                    }
                    break;
                case 6:
                    int width = 69;
                    int height = 69;
                    Seal seal = new Seal();
                    seal.setInitBeginX(item.getX());
                    seal.setInitBeginY(item.getY());
                    seal.setSize(130);
                    seal.setBorderCircle(new SealCircle(4, width, height));
                    seal.setMainFont(new SealFont(item.text, 16, item.getWeight() != 1, 16.0, 6));
                    seal.setCenterFont(new SealFont("★", 60));
                    seal.setTitleFont(new SealFont("电子签章", 14, 8.0, 44));
                    seal.draw(graphics);
                    break;
            }
        });
        graphics.dispose();
        ImgUtil.write(srcImage, credentialFile);
    }


    /**
     * 图片文字位置
     */
    @Data
    public static class FontLocation {
        private Integer type;
        private Integer x;
        private Integer y;
        private String text;
        private Integer weight;
        private String color;
        private Integer size;
    }
}
