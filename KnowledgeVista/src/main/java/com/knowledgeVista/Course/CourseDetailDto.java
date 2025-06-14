package com.knowledgeVista.Course;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@NoArgsConstructor
public class CourseDetailDto {

	private Long courseId;
    private String courseName;
    private String courseUrl;
    private String courseDescription;
    private String courseCategory;
    private Long amount;
    private byte[] courseImage;
    private Long duration;
    private String institutionName;
    private Long noOfSeats;
    private Double progressPercent;

    
	public CourseDetailDto(Long courseId, String courseName, String courseUrl, String courseDescription,
			String courseCategory, Long amount, byte[] courseImage,  Long duration,
			String institutionName, Long noOfSeats) {
	
		this.courseId = courseId;
		this.courseName = courseName;
		this.courseUrl = courseUrl;
		this.courseDescription = courseDescription;
		this.courseCategory = courseCategory;
		this.amount = amount;
		this.courseImage = courseImage;
		this.duration = duration;
		this.institutionName = institutionName;
		this.noOfSeats = noOfSeats;
	}
	public CourseDetailDto(Long courseId, String courseName, Long duration) {
		super();
		this.courseId = courseId;
		this.courseName = courseName;
		this.duration = duration;
	}
	public CourseDetailDto(Long courseId, String courseName, String courseUrl, String courseDescription,
			String courseCategory, Long amount, byte[] courseImage, Long duration, String institutionName) {
		super();
		this.courseId = courseId;
		this.courseName = courseName;
		this.courseUrl = courseUrl;
		this.courseDescription = courseDescription;
		this.courseCategory = courseCategory;
		this.amount = amount;
		this.courseImage = courseImage;
		this.duration = duration;
		this.institutionName = institutionName;
	}
	public CourseDetailDto(Long courseId, String courseName, String courseUrl, String courseDescription,
            String courseCategory, byte[] courseImage, Long duration,
            String institutionName, Double progressPercent) {
this.courseId = courseId;
this.courseName = courseName;
this.courseUrl = courseUrl;
this.courseDescription = courseDescription;
this.courseCategory = courseCategory;
this.courseImage = courseImage;
this.duration = duration;
this.institutionName = institutionName;
this.progressPercent = progressPercent;
}

	@Getter
	@Setter
	@NoArgsConstructor
	public static class courseIdNameImg{
		private Long courseId;
	    private String courseName;
	    private byte[] courseImage;
		public courseIdNameImg(Long courseId, String courseName, byte[] courseImage) {
			super();
			this.courseId = courseId;
			this.courseName = courseName;
			this.courseImage = courseImage;
		}
		
	}

}


