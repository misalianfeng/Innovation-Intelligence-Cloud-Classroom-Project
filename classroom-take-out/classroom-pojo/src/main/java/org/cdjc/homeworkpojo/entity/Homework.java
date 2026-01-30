package org.cdjc.homeworkpojo.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data  // Lombok 自动生成 getter/setter

 // 映射到数据库表 sys_homework
public class Homework {
  // 主键自增
    private Long id;

  // 字段不能为空
    private String title;  // 作业标题

   // 长文本类型
    private String content;  // 作业内容


    private Long courseId;  // 关联课程 ID


    private Long chapterId;  // 关联章节 ID

    private LocalDateTime publishTime;  // 发布时间
    private LocalDateTime deadline;  // 截止时间
    private Integer score;  // 作业总分

   // 枚举值存字符串
    private HomeworkStatus status;  // 作业状态

    private LocalDateTime createTime;  // 创建时间
    private LocalDateTime updateTime;  // 更新时间

    // 一对多关联：一个作业对应多个提交记录

    private List<HomeworkSubmission> submissions;

    // 作业状态枚举
    public enum HomeworkStatus {
        UNPUBLISHED, PUBLISHED, EXPIRED
    }
}