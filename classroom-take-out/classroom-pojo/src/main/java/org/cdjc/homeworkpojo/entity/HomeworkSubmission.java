package org.cdjc.homeworkpojo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data

public class HomeworkSubmission {

    private Long id;

    // 多对一关联：多个提交记录对应一个作业
    // 外键字段
    private Homework homework;


    private Long studentId;  // 学生 ID


    private String answerContent;  // 答题内容

    private LocalDateTime submitTime;  // 提交时间
    private Integer score;  // 批改分数
    private String comment;  // 老师评语
    private LocalDateTime correctTime;  // 批改时间


    private SubmissionStatus status;  // 提交状态

    public enum SubmissionStatus {
        UNSUBMITTED, SUBMITTED, CORRECTED
    }
}