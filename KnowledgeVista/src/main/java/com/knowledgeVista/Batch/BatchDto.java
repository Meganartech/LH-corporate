package com.knowledgeVista.Batch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.knowledgeVista.Course.CourseDetail;
import com.knowledgeVista.User.Muser;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BatchDto {
	private Long id;
	private String batchId;
	private String batchTitle;
	private String institutionName;
	private String courseNames; // For aggregated course names
	private String trainerNames; // For aggregated trainer names
	private List<CourseDto> courses;
	private List<TrainerDto> trainers;
	private List<String> course;
	private List<String> trainer;
	private byte[] batchImage;
	private Long durationInHours;
	  public BatchDto(Batch batch) {
	        this.id = batch.getId();
	        this.batchId = batch.getBatchId();
	        this.batchTitle = batch.getBatchTitle();
	        this.batchImage = batch.getBatchImage();
	        this.institutionName = batch.getInstitutionName();
	        this.durationInHours=batch.getDurationInHours();
	        // Extract course names
	        if (batch.getCourses() != null) {
	            this.course = batch.getCourses().stream()
	                    .map(CourseDetail::getCourseName)
	                    .collect(Collectors.toList());
	        } else {
	            this.course = new ArrayList<>(); // Initialize to avoid null
	        }


	        // Extract trainer usernames
	        if (batch.getTrainers() != null) {
	            this.trainer = batch.getTrainers().stream()
	                    .map(Muser::getUsername)
	                    .distinct() // Remove duplicates
	                    .collect(Collectors.toList());
	        } else {
	            this.trainers = new ArrayList<>();
	        }

	        // Extract user usernames
	       
	    }

	  public BatchDto(Long id, String batchId, String batchTitle, String institutionName,
              byte[] batchImage, Long durationInHours) {
  this.id = id;
  this.batchId = batchId;
  this.batchTitle = batchTitle;
  this.institutionName = institutionName;
  this.batchImage = batchImage;
  this.durationInHours = durationInHours;
  this.course = new ArrayList<>();
  this.trainer = new ArrayList<>();
}

	
	}
