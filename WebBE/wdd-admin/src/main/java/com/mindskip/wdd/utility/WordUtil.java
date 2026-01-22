package com.mindskip.wdd.utility;

import com.mindskip.wdd.viewmodel.word.WordImage;
import org.apache.poi.hwmf.usermodel.HwmfPicture;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * @version 1.7.0
 * @description: word工具类
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public class WordUtil {
    private static final Logger logger = LoggerFactory.getLogger(WordUtil.class);

    /**
     * word图片读取
     *
     * @param wmfByteList the wmf byte list
     * @return the word image
     */
    public static WordImage wmfToPng(byte[] wmfByteList) {
        try (ByteArrayInputStream xwpInputStream = new ByteArrayInputStream(wmfByteList)) {
            HwmfPicture hwmfPicture = new HwmfPicture(xwpInputStream);
            Dimension2D dim = hwmfPicture.getSize();
            int width = Units.pointsToPixel(dim.getWidth());
            int height = Units.pointsToPixel(dim.getHeight());
            double max = Math.max(width, height);
            if (max > 1500) {
                width *= 1500 / max;
                height *= 1500 / max;
            }
            BufferedImage bufImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = bufImg.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
            hwmfPicture.draw(graphics, new Rectangle2D.Double(0, 0, width, height));
            graphics.dispose();

            File pngTempFile = File.createTempFile("math", ".PNG");
            ImageIO.write(bufImg, "PNG", pngTempFile);
            WordImage wordImage = new WordImage();
            wordImage.setPath(pngTempFile.getPath());
            wordImage.setFileName(pngTempFile.getName());
            wordImage.setHeight(height);
            wordImage.setWidth(width);
            wordImage.setLength(pngTempFile.length());
            return wordImage;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取位置
     *
     * @param xwpfDocument the xwpf document
     * @param element      the element
     * @return the position
     */
    public static Integer getPosition(XWPFDocument xwpfDocument, IBodyElement element) {
        Integer pos = null;
        if (element instanceof XWPFParagraph) {
            pos = xwpfDocument.getPosOfParagraph((XWPFParagraph) element);
        } else if (element instanceof XWPFTable) {
            pos = xwpfDocument.getPosOfTable((XWPFTable) element);
        }
        return pos;
    }
}
