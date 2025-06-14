package com.knowledgeVista.Batch;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseDto {
	private Long courseId;
    private String courseName;
    private Long duration;

    public CourseDto(Long courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
    }

	public CourseDto(Long courseId, String courseName, Long duration) {
		super();
		this.courseId = courseId;
		this.courseName = courseName;
		this.duration = duration;
	}
}
