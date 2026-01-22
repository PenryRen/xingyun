package com.mindskip.wdd.service.impl;

import cn.hutool.core.bean.DynaBean;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.mindskip.wdd.configuration.utility.BeanValidator;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.enums.QuestionTypeEnum;
import com.mindskip.wdd.listener.*;
import com.mindskip.wdd.mapping.QuestionMapping;
import com.mindskip.wdd.service.FileUploadService;
import com.mindskip.wdd.service.QuestionArchiveService;
import com.mindskip.wdd.service.QuestionImportService;
import com.mindskip.wdd.service.QuestionService;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.WordUtil;
import com.mindskip.wdd.viewmodel.excel.*;
import com.mindskip.wdd.viewmodel.word.QuestionWordElement;
import com.mindskip.wdd.viewmodel.word.WordImage;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 题目导入
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Service
@AllArgsConstructor
public class QuestionImportServiceImpl implements QuestionImportService {

    private static final Logger logger = LoggerFactory.getLogger(com.mindskip.wdd.service.impl.QuestionImportServiceImpl.class);
    private static final int EMU_PER_PX = 9525;
    private final QuestionMapping questionMapping;
    private final FileUploadService fileUploadService;
    private final QuestionService questionService;
    private final BeanValidator beanValidator;
    private final QuestionArchiveService questionArchiveService;


    @Override
    public String fromExcel(User createUser, InputStream inputStream) throws IOException {
        ListenerParameter listenerParameter = new ListenerParameter(createUser, beanValidator, questionService, questionArchiveService, questionMapping);

        List<SingleChoiceVM> singleChoiceVMS = new ArrayList<>();
        List<MultipleVM> multipleVMS = new ArrayList<>();
        List<TrueFalseVM> trueFalseVMS = new ArrayList<>();
        List<GapFillingVM> gapFillingVMS = new ArrayList<>();
        List<ShortAnswerVM> shortAnswerVMS = new ArrayList<>();

        try {
            //读Excel
            ExcelReader excelReader = EasyExcel.read(inputStream).build();
            ReadSheet readSheet1 =
                    EasyExcel.readSheet("单选题").head(SingleChoiceVM.class).registerReadListener(new SingleChoiceVMListener(singleChoiceVMS, listenerParameter, true)).build();
            ReadSheet readSheet2 =
                    EasyExcel.readSheet("多选题").head(MultipleVM.class).registerReadListener(new MultipleVMListener(multipleVMS, listenerParameter, true)).build();
            ReadSheet readSheet3 =
                    EasyExcel.readSheet("判断题").head(TrueFalseVM.class).registerReadListener(new TrueFalseVMListener(trueFalseVMS, listenerParameter, true)).build();
            ReadSheet readSheet4 =
                    EasyExcel.readSheet("填空题").head(GapFillingVM.class).registerReadListener(new GapFillingVMListener(gapFillingVMS, listenerParameter, true)).build();
            ReadSheet readSheet5 =
                    EasyExcel.readSheet("简答题").head(ShortAnswerVM.class).registerReadListener(new ShortAnswerVMListener(shortAnswerVMS, listenerParameter, true)).build();
            excelReader.read(readSheet1, readSheet2, readSheet3, readSheet4, readSheet5);
            excelReader.finish();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        //回写Excel
        File excelTemp = File.createTempFile(UUID.randomUUID().toString(), ".xlsx");
        ExcelWriter excelWriter = EasyExcel.write(excelTemp).build();
        WriteSheet singleWriteSheet = new WriteSheet();
        singleWriteSheet.setSheetNo(0);
        singleWriteSheet.setSheetName("单选题");
        excelWriter.writeContext().writeWorkbookHolder().setClazz(SingleChoiceVM.class);
        excelWriter.write(singleChoiceVMS, singleWriteSheet);


        WriteSheet multipleWriter = new WriteSheet();
        multipleWriter.setSheetNo(1);
        multipleWriter.setSheetName("多选题");
        excelWriter.writeContext().writeWorkbookHolder().setClazz(MultipleVM.class);
        excelWriter.write(multipleVMS, multipleWriter);

        WriteSheet trueFileWriter = new WriteSheet();
        trueFileWriter.setSheetNo(2);
        trueFileWriter.setSheetName("判断题");
        excelWriter.writeContext().writeWorkbookHolder().setClazz(MultipleVM.class);
        excelWriter.write(trueFalseVMS, trueFileWriter);

        WriteSheet gapFileWriter = new WriteSheet();
        gapFileWriter.setSheetNo(3);
        gapFileWriter.setSheetName("填空题");
        excelWriter.writeContext().writeWorkbookHolder().setClazz(GapFillingVM.class);
        excelWriter.write(gapFillingVMS, gapFileWriter);

        WriteSheet shortFileWriter = new WriteSheet();
        shortFileWriter.setSheetNo(4);
        shortFileWriter.setSheetName("简答题");
        excelWriter.writeContext().writeWorkbookHolder().setClazz(ShortAnswerVM.class);
        excelWriter.write(shortAnswerVMS, shortFileWriter);


        excelWriter.finish();

        return fileUploadService.fileUpload(excelTemp, String.format("题目导入结果 - %s.xlsx", DateTimeUtil.dateTimeFullNumberFormat(new Date())), "export/excel", true, true);
    }

    @Override
    public String fromWord(User createUser, InputStream inputStream) throws IOException {
        String resultPath = null;
        //word 处理
        try (XWPFDocument xwpfDocument = new XWPFDocument(inputStream)) {
            List<QuestionWordElement> questionWordElementList = wordToExcelVM(xwpfDocument);
            //excel 导入处理
            List<QuestionWordElement> errorQuestionWordElementList = excelSingleImport(questionWordElementList, createUser);

            //清理正确题目
            List<IBodyElement> allElementList = xwpfDocument.getBodyElements();
            List<IBodyElement> errorElementList = errorQuestionWordElementList.stream().flatMap(pEl -> pEl.getBodyElements().stream()).collect(Collectors.toList());
            List<IBodyElement> rightElementList = allElementList.stream().filter(el -> !errorElementList.contains(el)).collect(Collectors.toList());
            rightElementList.forEach(elItem -> {
                Integer pos = WordUtil.getPosition(xwpfDocument, elItem);
                if (null != pos) {
                    xwpfDocument.removeBodyElement(pos);
                }
            });

            //插入错误提示
            errorQuestionWordElementList.forEach(errorQuestion -> {
                XWPFParagraph searchXWPFParagraph = searchXWPFParagraph(errorQuestion.getBodyElements(), "【难度】");
                XmlCursor xmlCursor = searchXWPFParagraph.getCTP().newCursor();  //从段落中获取光标
                xmlCursor.toNextSibling();
                XWPFParagraph newParagraph = xwpfDocument.insertNewParagraph(xmlCursor);
                XWPFRun run = newParagraph.createRun();
                run.setColor("EE0000");
                run.setText(String.format("【导入结果】 %s", errorQuestion.getResult()));
            });

            File wordTemp = File.createTempFile(UUID.randomUUID().toString(), ".docx");
            try (FileOutputStream fileOutputStream = new FileOutputStream(wordTemp.getPath())) {
                xwpfDocument.write(fileOutputStream);
            }
            resultPath = fileUploadService.fileUpload(wordTemp, String.format("题目导入结果 - %s.docx", DateTimeUtil.dateTimeFullNumberFormat(new Date())), "export/word", true, true);
        }
        return resultPath;


    }

    /**
     * Word 转成 Excel 导入对象
     *
     * @param xwpfDocument
     * @return
     */
    private List<QuestionWordElement> wordToExcelVM(XWPFDocument xwpfDocument) {
        List<IBodyElement> bodyElements = xwpfDocument.getBodyElements();
        List<QuestionWordElement> questionWordElementList = new ArrayList<>();
        for (int i = 0, bodyElementsSize = bodyElements.size(); i < bodyElementsSize; i++) {
            IBodyElement bodyElement = bodyElements.get(i);
            if (bodyElement instanceof XWPFParagraph) {
                XWPFParagraph xwpfParagraph = (XWPFParagraph) bodyElement;
                String paragraphText = xwpfParagraph.getParagraphText();
                if (paragraphText.startsWith("【题型】")) {
                    QuestionWordElement questionWordElement = new QuestionWordElement();
                    String questionTypeStr = paragraphText.substring(4).trim();
                    questionWordElement.setQuestionTypeEnum(QuestionTypeEnum.fromName(questionTypeStr));
                    questionWordElement.setBodyElements(new ArrayList<>());
                    questionWordElementList.add(questionWordElement);
                }
            }
            if (questionWordElementList.size() > 0) {
                QuestionWordElement lastEl = questionWordElementList.get(questionWordElementList.size() - 1);
                lastEl.getBodyElements().add(bodyElement);
            }
        }
        return questionWordElementList;
    }

    /**
     * Word -> Excel 导入
     *
     * @param questionWordElementList
     * @param createUser
     * @return 导入失败数据
     */
    private List<QuestionWordElement> excelSingleImport(List<QuestionWordElement> questionWordElementList, User createUser) {
        List<QuestionWordElement> errorQuestionWordElementList = new ArrayList<>();
        ListenerParameter listenerParameter = new ListenerParameter(createUser, beanValidator, questionService, questionArchiveService, questionMapping);
        List<SingleChoiceVM> singleChoiceVMS = new ArrayList<>();
        List<MultipleVM> multipleVMS = new ArrayList<>();
        List<TrueFalseVM> trueFalseVMS = new ArrayList<>();
        List<GapFillingVM> gapFillingVMS = new ArrayList<>();
        List<ShortAnswerVM> shortAnswerVMS = new ArrayList<>();
        SingleChoiceVMListener singleChoiceVMListener = new SingleChoiceVMListener(singleChoiceVMS, listenerParameter, false);
        MultipleVMListener multipleVMListener = new MultipleVMListener(multipleVMS, listenerParameter, false);
        TrueFalseVMListener trueFalseVMListener = new TrueFalseVMListener(trueFalseVMS, listenerParameter, false);
        GapFillingVMListener gapFillingVMListener = new GapFillingVMListener(gapFillingVMS, listenerParameter, false);
        ShortAnswerVMListener shortAnswerVMListener = new ShortAnswerVMListener(shortAnswerVMS, listenerParameter, false);
        questionWordElementList.forEach(el -> {
            switch (el.getQuestionTypeEnum()) {
                case SingleChoice:
                    SingleChoiceVM singleChoice = singleChoice(el.getBodyElements());
                    singleChoiceVMListener.invoke(singleChoice, null);
                    if (StringUtils.isNotBlank(singleChoice.getResult())) {
                        el.setResult(singleChoice.getResult());
                        errorQuestionWordElementList.add(el);
                    }
                    break;
                case MultipleChoice:
                    MultipleVM multipleChoice = multiple(el.getBodyElements());
                    multipleVMListener.invoke(multipleChoice, null);
                    if (StringUtils.isNotBlank(multipleChoice.getResult())) {
                        el.setResult(multipleChoice.getResult());
                        errorQuestionWordElementList.add(el);
                    }
                    break;
                case TrueFalse:
                    TrueFalseVM trueFalse = trueFalse(el.getBodyElements());
                    trueFalseVMListener.invoke(trueFalse, null);
                    if (StringUtils.isNotBlank(trueFalse.getResult())) {
                        el.setResult(trueFalse.getResult());
                        errorQuestionWordElementList.add(el);
                    }
                    break;
                case GapFilling:
                    GapFillingVM gapFilling = gapFilling(el.getBodyElements());
                    gapFillingVMListener.invoke(gapFilling, null);
                    if (StringUtils.isNotBlank(gapFilling.getResult())) {
                        el.setResult(gapFilling.getResult());
                        errorQuestionWordElementList.add(el);
                    }
                    break;
                case ShortAnswer:
                    ShortAnswerVM shortAnswer = shortAnswer(el.getBodyElements());
                    shortAnswerVMListener.invoke(shortAnswer, null);
                    if (StringUtils.isNotBlank(shortAnswer.getResult())) {
                        el.setResult(shortAnswer.getResult());
                        errorQuestionWordElementList.add(el);
                    }
                    break;
                default:
                    break;
            }
        });
        return errorQuestionWordElementList;
    }

    /**
     * word单选题分解
     *
     * @param questionElement
     * @return {@link SingleChoiceVM}
     */
    private SingleChoiceVM singleChoice(List<IBodyElement> questionElement) {
        QuestionCommonVM questionCommon = questionCommonVM(questionElement);
        SingleChoiceVM singleChoiceVM = questionMapping.toSingleChoiceVM(questionCommon);
        List<IBodyElement> titleElement = selectElement(questionElement, "【题干】", "【选项】");
        String titleStr = convertHtml(titleElement); //题干
        singleChoiceVM.setTitle(titleStr);
        List<IBodyElement> questionItemElement = selectElement(questionElement, "【选项】", "【标答】"); //选项

        //首选项处理
        List<IBodyElement> choiceA = selectElement(questionItemElement, "【选项】", "B");
        if (choiceA.size() > 0) {
            String choiceAHtml = convertHtml(choiceA);
            singleChoiceVM.setA(choiceAHtml.substring(2));
        }

        //其他选项处理
        DynaBean singleChoiceVMBean = DynaBean.create(singleChoiceVM);
        char property = 'b';
        for (int i = 0; i < 8; i++) {
            List<IBodyElement> choiceSelect = selectElement(questionItemElement, String.valueOf((char) ((int) property - 32)), String.valueOf((char) ((int) property - 31)));
            if (choiceSelect.size() > 0) {
                String choiceHtml = convertHtml(choiceSelect);
                singleChoiceVMBean.set(String.valueOf(property), choiceHtml.substring(2));
            }
            property = (char) ((int) property + 1);
        }
        return singleChoiceVM;
    }

    /**
     * word多选题分解
     *
     * @param questionElement
     * @return {@link MultipleVM}
     */
    private MultipleVM multiple(List<IBodyElement> questionElement) {
        QuestionCommonVM questionCommon = questionCommonVM(questionElement);
        MultipleVM multipleVM = questionMapping.toMultipleVM(questionCommon);
        List<IBodyElement> titleElement = selectElement(questionElement, "【题干】", "【选项】");
        String titleStr = convertHtml(titleElement); //题干
        multipleVM.setTitle(titleStr);
        List<IBodyElement> questionItemElement = selectElement(questionElement, "【选项】", "【标答】"); //选项

        //首选项处理
        List<IBodyElement> choiceA = selectElement(questionItemElement, "【选项】", "B");
        if (choiceA.size() > 0) {
            String choiceAHtml = convertHtml(choiceA);
            multipleVM.setA(choiceAHtml.substring(2));
        }

        //其他选项处理
        DynaBean multipleVMBean = DynaBean.create(multipleVM);
        char property = 'b';
        for (int i = 0; i < 8; i++) {
            List<IBodyElement> choiceSelect = selectElement(questionItemElement, String.valueOf((char) ((int) property - 32)), String.valueOf((char) ((int) property - 31)));
            if (choiceSelect.size() > 0) {
                String choiceHtml = convertHtml(choiceSelect);
                multipleVMBean.set(String.valueOf(property), choiceHtml.substring(2));
            }
            property = (char) ((int) property + 1);
        }
        return multipleVM;
    }

    /**
     * word判断题分解
     *
     * @param questionElement
     * @return {@link TrueFalseVM}
     */
    private TrueFalseVM trueFalse(List<IBodyElement> questionElement) {
        QuestionCommonVM questionCommon = questionCommonVM(questionElement);
        TrueFalseVM trueFalseVM = questionMapping.toTrueFalseVM(questionCommon);
        List<IBodyElement> titleElement = selectElement(questionElement, "【题干】", "【选项】");
        String titleStr = convertHtml(titleElement); //题干
        trueFalseVM.setTitle(titleStr);
        List<IBodyElement> questionItemElement = selectElement(questionElement, "【选项】", "【标答】"); //选项

        //首选项处理
        List<IBodyElement> choiceA = selectElement(questionItemElement, "【选项】", "B");
        if (choiceA.size() > 0) {
            String choiceAHtml = convertHtml(choiceA);
            trueFalseVM.setA(choiceAHtml.substring(2));
        }

        //其他选项处理
        DynaBean trueFalseVMBean = DynaBean.create(trueFalseVM);
        char property = 'b';
        for (int i = 0; i < 1; i++) {
            List<IBodyElement> choiceSelect = selectElement(questionItemElement, String.valueOf((char) ((int) property - 32)), String.valueOf((char) ((int) property - 31)));
            if (choiceSelect.size() > 0) {
                String choiceHtml = convertHtml(choiceSelect);
                trueFalseVMBean.set(String.valueOf(property), choiceHtml.substring(2));
            }
            property = (char) ((int) property + 1);
        }
        return trueFalseVM;
    }

    /**
     * word填空题分解
     *
     * @param questionElement
     * @return {@link GapFillingVM}
     */
    private GapFillingVM gapFilling(List<IBodyElement> questionElement) {
        QuestionCommonVM questionCommon = questionCommonVM(questionElement);
        GapFillingVM gapFillingVM = questionMapping.toGapFillingVM(questionCommon);

        List<IBodyElement> titleElement = selectElement(questionElement, "【题干】", "【标答】");
        String titleStr = convertHtml(titleElement); //题干
        gapFillingVM.setTitle(titleStr);
        List<IBodyElement> questionItemElement = selectElement(questionElement, "【标答】", "【解析】"); //选项

        //首空处理
        List<IBodyElement> fillA = selectElement(questionItemElement, "【标答】", "2");
        if (fillA.size() > 0) {
            String fillAHtml = convertHtml(fillA);
            gapFillingVM.setA(fillAHtml.substring(2));
        }

        //其他空处理
        DynaBean gapFillingVMBean = DynaBean.create(gapFillingVM);
        char property = 'b';
        for (int i = 2; i < 9; i++) {
            List<IBodyElement> fillSelect = selectElement(questionItemElement, String.valueOf(i), String.valueOf(i + 1));
            if (fillSelect.size() > 0) {
                String fillHtml = convertHtml(fillSelect);
                gapFillingVMBean.set(String.valueOf(property), fillHtml.substring(2));
            }
            property = (char) ((int) property + 1);
        }
        return gapFillingVM;
    }

    /**
     * word简答题分解
     *
     * @param questionElement
     * @return {@link ShortAnswerVM}
     */
    private ShortAnswerVM shortAnswer(List<IBodyElement> questionElement) {
        QuestionCommonVM questionCommon = questionCommonVM(questionElement);
        ShortAnswerVM shortAnswerVM = questionMapping.toShortAnswerVM(questionCommon);
        List<IBodyElement> titleElement = selectElement(questionElement, "【题干】", "【标答】");
        String titleStr = convertHtml(titleElement); //题干
        shortAnswerVM.setTitle(titleStr);

        //标答
        List<IBodyElement> correctEl = selectElement(questionElement, "【标答】", "【解析】");
        String correctStr = convertHtml(correctEl);
        shortAnswerVM.setCorrect(correctStr);

        return shortAnswerVM;
    }

    /**
     * 题目基本信息分解
     *
     * @param questionElement
     * @return {@link QuestionCommonVM}
     */
    private QuestionCommonVM questionCommonVM(List<IBodyElement> questionElement) {
        QuestionCommonVM questionCommonVM = new QuestionCommonVM();
        questionElement.forEach(element -> {
            if (element instanceof XWPFParagraph) {
                XWPFParagraph xwpfParagraph = (XWPFParagraph) element;
                String text = xwpfParagraph.getParagraphText();
                if (text.startsWith("【分类】")) {
                    String levelStr = text.substring(4).trim();
                    questionCommonVM.setLevel(levelStr);
                } else if (text.startsWith("【标答】")) {
                    String correctStr = text.substring(4).trim();
                    questionCommonVM.setCorrect(correctStr);
                } else if (text.startsWith("【解析】")) {
                    List<IBodyElement> analyzeEl = selectElement(questionElement, "【解析】", "【分数】");
                    String analyzeStr = convertHtml(analyzeEl);
                    questionCommonVM.setAnalyze(analyzeStr);
                } else if (text.startsWith("【分数】")) {
                    String score = text.substring(4).trim();
                    questionCommonVM.setScore(score);
                } else if (text.startsWith("【难度】")) {
                    Integer difficult = Integer.parseInt(text.substring(4).trim());
                    questionCommonVM.setDifficult(difficult);
                }
            }
        });
        return questionCommonVM;
    }

    /**
     * 选择指定word节点
     *
     * @param questionElement
     * @param start
     * @param end
     * @return {@link List}<{@link IBodyElement}>
     */
    private List<IBodyElement> selectElement(List<IBodyElement> questionElement, String start, String end) {
        Integer startIndex = null;
        Integer endIndex = null;
        for (int i = 0; i < questionElement.size(); i++) {
            IBodyElement bodyElement = questionElement.get(i);
            if (bodyElement instanceof XWPFParagraph) {
                XWPFParagraph xwpfParagraph = (XWPFParagraph) bodyElement;
                String text = xwpfParagraph.getParagraphText();
                if (StringUtils.isNotBlank(text)) {
                    if (text.startsWith(start)) {
                        startIndex = i;
                    } else if (text.startsWith(end)) {
                        endIndex = i;
                        break;
                    }
                }
            }
        }
        if (null == endIndex) {
            endIndex = questionElement.size();
        }
        if (null == startIndex) {
            return new ArrayList<>(0);
        }
        return questionElement.subList(startIndex, endIndex);
    }

    /**
     * 搜索word段落
     *
     * @param questionElement
     * @param start
     * @return {@link XWPFParagraph}
     */
    private XWPFParagraph searchXWPFParagraph(List<IBodyElement> questionElement, String start) {
        for (int i = 0; i < questionElement.size(); i++) {
            IBodyElement bodyElement = questionElement.get(i);
            if (bodyElement instanceof XWPFParagraph) {
                XWPFParagraph xwpfParagraph = (XWPFParagraph) bodyElement;
                String text = xwpfParagraph.getParagraphText();
                if (StringUtils.isNotBlank(text)) {
                    if (text.startsWith(start)) {
                        return xwpfParagraph;
                    }
                }
            }
        }
        return null;
    }

    /**
     * word节点转化为html标签
     *
     * @param elementList
     * @return {@link String}
     */
    private String convertHtml(List<IBodyElement> elementList) {
        String content = "";
        int elementSize = elementList.size();
        for (IBodyElement bodyElement : elementList) {
            if (bodyElement instanceof XWPFParagraph) {
                for (XWPFRun run : ((XWPFParagraph) bodyElement).getRuns()) {
                    String text = run.getText(0);
                    if (null != text) {
                        content += text;
                    } else {
                        XWPFDocument document = ((XWPFParagraph) bodyElement).getDocument();
                        WordImage wordImage = wordImage(run);  //普通word图片
                        if (null != wordImage) {
                            XWPFPictureData xwpfPictureData = document.getPictureDataByID(wordImage.getImageId());
                            String imageName = xwpfPictureData.getFileName();
                            byte[] imageBytes = xwpfPictureData.getData();
                            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes)) {
                                String ossImagePath = fileUploadService.fileUpload(inputStream, imageBytes.length, imageName, "editor/image", true, true);
                                content += String.format("<img class=\"wdd-image\" src=\"%s\"  width=\"%d\" height=\"%d\" />", ossImagePath, wordImage.getWidth(), wordImage.getHeight());
                            } catch (IOException e) {
                                logger.error(e.getMessage(), e);
                            }
                        }
                    }
                }
            } else if (bodyElement instanceof XWPFTable) {
                XWPFTable xwpfTable = (XWPFTable) bodyElement;
                String rowStr = "";
                for (XWPFTableRow xwpfTableRow : xwpfTable.getRows()) {
                    String cellStr = "";
                    for (XWPFTableCell xwpfTableCell : xwpfTableRow.getTableCells()) {
                        cellStr += String.format("<td  class=\"wdd-table-td\"  valign=\"top\" style=\"word-break: break-all;\">%s</td>", xwpfTableCell.getText());
                    }
                    rowStr += String.format("<tr>%s</tr>", cellStr);
                }
                content += String.format("<table class=\"wdd-table\"><tbody>%s</tbody></table>", rowStr);
            }
            if (StringUtils.isNotBlank(content) && elementSize > 1 && elementList.lastIndexOf(bodyElement) < (elementSize - 1)) { //word换行、首尾不加换行
                content += "<br>";
            }
        }

        content = content.replaceFirst("^【.*?】", ""); //去掉标识字符
        content = content.replaceFirst("^<br>", ""); //去掉首部换行
        content = content.replaceFirst("<br>$", ""); //去掉尾部换行
        return content;
    }


    /**
     * 读取word图片
     *
     * @param run
     * @return {@link WordImage}
     */
    private WordImage wordImage(XWPFRun run) {
        Node node = run.getCTR().getDomNode();
        Node drawingNode = getChildNode(node, "w:drawing");
        if (drawingNode == null) {
            return null;
        }
        Node extentNode = getChildNode(drawingNode, "wp:extent");
        NamedNodeMap extentAttrs = extentNode.getAttributes();
        int width = Integer.parseInt(extentAttrs.getNamedItem("cx").getNodeValue());
        int height = Integer.parseInt(extentAttrs.getNamedItem("cy").getNodeValue());
        Double widthPx = NumberUtil.div(width, EMU_PER_PX);
        Double heightPx = NumberUtil.div(height, EMU_PER_PX);
        Node blipNode = getChildNode(drawingNode, "a:blip");
        NamedNodeMap blipAttrs = blipNode.getAttributes();
        String rid = blipAttrs.getNamedItem("r:embed").getNodeValue();
        return new WordImage(rid, widthPx.intValue(), heightPx.intValue());
    }

    /**
     * 获取子节点
     *
     * @param node
     * @param nodeName
     * @return {@link Node}
     */
    private Node getChildNode(Node node, String nodeName) {
        if (!node.hasChildNodes()) {
            return null;
        }
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (nodeName.equals(childNode.getNodeName())) {
                return childNode;
            }
            childNode = getChildNode(childNode, nodeName);
            if (childNode != null) {
                return childNode;
            }
        }
        return null;
    }
}
