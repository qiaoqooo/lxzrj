package org.example.userservice.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * PDF 预览图工具类：将 PDF 第 1 页渲染为 PNG 图片
 */
public class PdfPreviewUtil {

    /**
     * 生成 PDF 预览图（仅第 1 页）
     *
     * @param pdfFile   源 PDF 文件
     * @param outputDir 输出目录（必须可写）
     * @param outputName 不带后缀的输出文件名
     * @return 生成的图片文件对象，失败返回 null
     */
    public static File generateFirstPagePreview(File pdfFile, File outputDir, String outputName) {
        if (pdfFile == null || !pdfFile.exists()) {
            return null;
        }
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            return null;
        }
        File outFile = new File(outputDir, outputName + ".png");
        try (PDDocument document = PDDocument.load(pdfFile)) {
            if (document.getNumberOfPages() <= 0) {
                return null;
            }
            PDFRenderer renderer = new PDFRenderer(document);
            // 150~200 DPI 足够做预览
            BufferedImage image = renderer.renderImageWithDPI(0, 160, ImageType.RGB);
            ImageIO.write(image, "PNG", outFile);
            return outFile;
        } catch (IOException e) {
            // 生成失败直接返回 null，上层可选择忽略预览图
            return null;
        }
    }
}





















