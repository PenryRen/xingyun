package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.CourseWare;
import com.mindskip.wdd.domain.CourseWareQuestion;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.course.ware.CourseWareEditRequestVM;
import com.mindskip.wdd.viewmodel.course.ware.CourseWarePageResponseVM;
import com.mindskip.wdd.viewmodel.course.ware.CourseWareQuestionVM;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-26T21:40:32+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class CourseWareMappingImpl implements CourseWareMapping {

    @Override
    public CourseWarePageResponseVM toCourseWareResponseVM(CourseWare courseWare) {
        if ( courseWare == null ) {
            return null;
        }

        CourseWarePageResponseVM courseWarePageResponseVM = new CourseWarePageResponseVM();

        courseWarePageResponseVM.setCreateUser( courseWare.getCreateUser() );
        courseWarePageResponseVM.setFileName( courseWare.getFileName() );
        courseWarePageResponseVM.setFileType( courseWare.getFileType() );
        courseWarePageResponseVM.setId( courseWare.getId() );
        courseWarePageResponseVM.setMaxLength( courseWare.getMaxLength() );
        courseWarePageResponseVM.setName( courseWare.getName() );
        courseWarePageResponseVM.setPreviewPath( courseWare.getPreviewPath() );
        courseWarePageResponseVM.setVmType( courseWare.getVmType() );

        courseWarePageResponseVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(courseWare.getCreateTime()) );

        return courseWarePageResponseVM;
    }

    @Override
    public CourseWare toCourseWare(CourseWareEditRequestVM courseWareEditRequestVM) {
        if ( courseWareEditRequestVM == null ) {
            return null;
        }

        CourseWare courseWare = new CourseWare();

        courseWare.setCourseWareArchiveId( courseWareEditRequestVM.getCourseWareArchiveId() );
        courseWare.setDescription( courseWareEditRequestVM.getDescription() );
        courseWare.setFileName( courseWareEditRequestVM.getFileName() );
        courseWare.setFileType( courseWareEditRequestVM.getFileType() );
        courseWare.setId( courseWareEditRequestVM.getId() );
        courseWare.setMaxLength( courseWareEditRequestVM.getMaxLength() );
        courseWare.setName( courseWareEditRequestVM.getName() );
        courseWare.setOriginalPath( courseWareEditRequestVM.getOriginalPath() );
        courseWare.setPreviewPath( courseWareEditRequestVM.getPreviewPath() );
        courseWare.setVmType( courseWareEditRequestVM.getVmType() );

        return courseWare;
    }

    @Override
    public void mapCourseWare(CourseWareEditRequestVM courseWareEditRequestVM, CourseWare courseWare) {
        if ( courseWareEditRequestVM == null ) {
            return;
        }

        courseWare.setCourseWareArchiveId( courseWareEditRequestVM.getCourseWareArchiveId() );
        courseWare.setDescription( courseWareEditRequestVM.getDescription() );
        courseWare.setFileName( courseWareEditRequestVM.getFileName() );
        courseWare.setFileType( courseWareEditRequestVM.getFileType() );
        courseWare.setId( courseWareEditRequestVM.getId() );
        courseWare.setMaxLength( courseWareEditRequestVM.getMaxLength() );
        courseWare.setName( courseWareEditRequestVM.getName() );
        courseWare.setOriginalPath( courseWareEditRequestVM.getOriginalPath() );
        courseWare.setPreviewPath( courseWareEditRequestVM.getPreviewPath() );
        courseWare.setVmType( courseWareEditRequestVM.getVmType() );
    }

    @Override
    public CourseWareEditRequestVM toCourseWareEditRequestVM(CourseWare courseWare) {
        if ( courseWare == null ) {
            return null;
        }

        CourseWareEditRequestVM courseWareEditRequestVM = new CourseWareEditRequestVM();

        courseWareEditRequestVM.setCourseWareArchiveId( courseWare.getCourseWareArchiveId() );
        courseWareEditRequestVM.setDescription( courseWare.getDescription() );
        courseWareEditRequestVM.setFileName( courseWare.getFileName() );
        courseWareEditRequestVM.setFileType( courseWare.getFileType() );
        courseWareEditRequestVM.setId( courseWare.getId() );
        courseWareEditRequestVM.setMaxLength( courseWare.getMaxLength() );
        courseWareEditRequestVM.setName( courseWare.getName() );
        courseWareEditRequestVM.setOriginalPath( courseWare.getOriginalPath() );
        courseWareEditRequestVM.setPreviewPath( courseWare.getPreviewPath() );
        courseWareEditRequestVM.setVmType( courseWare.getVmType() );

        return courseWareEditRequestVM;
    }

    @Override
    public CourseWareQuestionVM toCourseWareQuestionVM(CourseWareQuestion courseWareQuestion) {
        if ( courseWareQuestion == null ) {
            return null;
        }

        CourseWareQuestionVM courseWareQuestionVM = new CourseWareQuestionVM();

        courseWareQuestionVM.setAnchorFormat( courseWareQuestion.getAnchorFormat() );
        courseWareQuestionVM.setAnchorSecond( courseWareQuestion.getAnchorSecond() );
        courseWareQuestionVM.setQuestionFrameId( courseWareQuestion.getQuestionFrameId() );
        courseWareQuestionVM.setQuestionId( courseWareQuestion.getQuestionId() );

        return courseWareQuestionVM;
    }

    @Override
    public CourseWareQuestion toCourseWareQuestion(CourseWareQuestionVM courseWareQuestionVM) {
        if ( courseWareQuestionVM == null ) {
            return null;
        }

        CourseWareQuestion courseWareQuestion = new CourseWareQuestion();

        courseWareQuestion.setAnchorFormat( courseWareQuestionVM.getAnchorFormat() );
        courseWareQuestion.setAnchorSecond( courseWareQuestionVM.getAnchorSecond() );
        courseWareQuestion.setQuestionFrameId( courseWareQuestionVM.getQuestionFrameId() );
        courseWareQuestion.setQuestionId( courseWareQuestionVM.getQuestionId() );

        return courseWareQuestion;
    }
}
