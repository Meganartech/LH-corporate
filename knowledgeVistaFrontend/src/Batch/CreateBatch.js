import axios from "axios";
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import baseUrl from "../api/utils";
import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";

const CreateBatch = () => {
  const navigate = useNavigate();
   const MySwal = withReactContent(Swal);
  const [searchQuerycourse,setSearchQuerycourse]=useState('')
  const [courses, setCourses] = useState({});
  const[selectedCourse,setSelectedCourse]=useState([])
  const token = sessionStorage.getItem("token");
  const role=sessionStorage.getItem("role")
  const[batch,setbatch]=useState({
    batchTitle:"",
    durationInHours:"",
    courses:selectedCourse,
    BatchImage:null,
    base64Image:null
  })
  const [errors, setErrors] = useState({
    batchTitle: "",
    durationInHours:"",
    BatchImage:null,
    base64Image:null
  });
  const convertImageToBase64 = (file) => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result);
      reader.onerror = (error) => reject(error);
    });
  };
  const handleFileChange = (e) => {
    const file = e.target.files[0];

    // Define allowed MIME types for images
    const allowedImageTypes = ["image/jpeg", "image/png"];

    // Check file type (MIME type)
    if (file &&  !allowedImageTypes.includes(file.type)) {
      setErrors((prevErrors) => ({
        ...prevErrors,
        BatchImage: "Please select an image of type JPG or PNG",
      }));
      return;
    }

    // Check file size (should be 1 MB or less)
    if (file && file.size > 50 * 1024) {
      setErrors((prevErrors) => ({
        ...prevErrors,
        BatchImage: "Image size must be 50 kb or smaller",
      }));
      return;
    }

    // Update formData with the new file
    setbatch((prevFormData) => ({
      ...prevFormData,
      BatchImage: file,
    }));

    // Convert the file to base64
    convertImageToBase64(file)
      .then((base64Data) => {
        // Set the base64 encoded image in the state
        setbatch((prevFormData) => ({
          ...prevFormData,
          base64Image: base64Data,
        }));
        setErrors((prevErrors) => ({
          ...prevErrors,
          BatchImage: "",
        }));
      })
      .catch((error) => {
        console.error("Error converting image to base64:", error);
        setErrors((prevErrors) => ({
          ...prevErrors,
          BatchImage: "Error converting image to base64",
        }));
      });
  };
  const handleCourseClick = (course) => {
   
    setSelectedCourse((prevSelected) => {
      // Check if course is already selected
      const exists = prevSelected.find((courseprev) => courseprev.courseId === course.courseId);
      let updatedCourses;
      if (exists) {
        // Remove the course if already selected
        updatedCourses= prevSelected
      } else {
        // Add the course if not selected
        updatedCourses= [...prevSelected, { courseId: course.courseId, courseName: course.courseName ,amount:course.amount,duration:course.duration}];
      }
 
    setbatch((prevBatch) => ({
      ...prevBatch,
      courses:updatedCourses,
      durationInHours: Number(prevBatch.durationInHours) + Number(course.duration), // Update batch amount
    }));
    return updatedCourses;
  })
    setCourses({});
    setSearchQuerycourse('')
  };
 
  const handleCourseRemove = (course) => {
   
    setSelectedCourse((prevSelected) => {
      // Check if course is already selected
      const exists = prevSelected.find(
        (courseprev) => courseprev.courseId === course.courseId
      );
    
      if (exists) {
        const updatedCourses = prevSelected.filter(
          (courseprev) => courseprev.courseId !== course.courseId
        );
        setbatch((prevBatch) => {
          return {
            ...prevBatch,
            courses: updatedCourses,
            durationInHours: Number(prevBatch.durationInHours) - Number(course.duration), 
          };
        });
    
        return updatedCourses; // Return updated courses list
      }
    
      return prevSelected; // If not found, return original list
    });
    
    
  };
 
  const searchCourses = async (e) => {
    try {
      setSearchQuerycourse(e.target.value)
      const response = await axios.get(`${baseUrl}/searchCourse`, {
        params: { courseName: e.target.value },
        headers: {
          Authorization: token,
        },
      });
      setCourses(response.data);
    } catch (error) {
      console.error("Error fetching courses:", error);
    }
  };
 
 
  
  const handleBatchChange = (e) => {
    const { name, value } = e.target;
  
    // Initialize an empty error object
    let errorObj = { ...errors };
  
    // Check for errors based on the name of the field being updated
    switch (name) {
      case "batchTitle":
        if (!value.trim()) {
          errorObj.batchTitle = "Batch title cannot be empty!";
        } else if (/[\/\\]/.test(value)) {
          errorObj.batchTitle = "Batch title cannot contain '/' or '\\'";
        } else {
          errorObj.batchTitle = "";
          setbatch((prev) => ({
            ...prev,
            [name]: value,
          }));
        }
        break;
        case "durationInHours":
          if (value && value <= 0) {
            errorObj.durationInHours = "Duration cannot be zero or negative!";
          } else {
            errorObj.durationInHours = "";
            setbatch((prev) => ({
              ...prev,
              [name]: value,
            }));
          }
          break;
  
         default:
        break;
    }
  
    
  
    // Update the errors state
    setErrors(errorObj);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
  
    const newErrors = {};

    // Validation checks
    if (!batch?.batchTitle) newErrors.batchTitle = "Batch title is required.";
    if (!batch?.durationInHours) newErrors.durationInHours = "Batch Duration is required.";
    if(!batch?.courses?.length<0)newErrors.courses="select atleast one Course"
    setErrors(newErrors);
    if (Object.keys(newErrors)?.length > 0) {
      return;
    }
  
    const formData = new FormData();
    formData.append("batchTitle", batch.batchTitle);
    formData.append("courses", JSON.stringify(batch.courses)); 
    formData.append("durationInHours",batch.durationInHours);
 
    // Append batch image if exists
    if (batch.BatchImage) {
      formData.append("batchImage", batch.BatchImage);
    }
    
    try {
      const response = await axios.post(`${baseUrl}/batch/save`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization:token
        },
      });
  
      if (response.status === 200) {
        MySwal.fire({
          title: "Batch Created",
          text: "batch Created sucessfully !",
          icon: "success",
        })
        setbatch({
          batchTitle: "",
          courses: []
        });
        setSelectedCourse([]);
        setErrors({});
        const batchId=response?.data?.batchId;
        const batchTitle=response?.data?.batchName;
        navigate("/batch/viewall")
        
      }
    } catch (error) {
      console.error("Error saving batch:", error);
    }
  };
  
  

  return (
    <div>
      <div className="page-header"></div>
      <div className="card">
        <div className=" card-body">
          <div className="row">
            <div className="col-12">
              <div className="navigateheaders">
                <div
                  onClick={() => {
                    navigate(-1);
                  }}
                >
                  <i className="fa-solid fa-arrow-left"></i>
                </div>
                <div></div>
                <div
                  onClick={() => {
                    navigate("/dashboard/course");
                  }}
                >
                  <i className="fa-solid fa-xmark"></i>
                </div>
              </div>

              <h4>Create batch</h4>
              <div className="col-10">
                <div className="form-group row">
                  <label
                    htmlFor="courseName"
                    className="col-sm-3 col-form-label"
                  >
                    batch Title <span className="text-danger">*</span>
                  </label>
                  <div className="col-sm-9">
                    <input
                      type="text"
                      className={`form-control ${errors.batchTitle && "is-invalid"} `}
                      placeholder="Batch Title"
                      value={batch.batchTitle}
                      name="batchTitle"
                      onChange={handleBatchChange}
                    />
                    <div className="invalid-feedback">{errors.batchTitle}</div>
                  </div>
                </div>
               

                <div className="form-group row">
                  <label className="col-sm-3 col-form-label">
                    Courses
                  </label>
                  <div className="col-sm-9">
                  <div className="inputlike">
                  {selectedCourse?.length > 0 && (
        <div className="listemail">
          {selectedCourse?.map((course) => (
            <div key={course?.courseId} className="selectedemail">
              {course?.courseName}{" "}
              <i
                onClick={() => handleCourseRemove(course)}
                className="fa-solid fa-xmark"
              ></i>
            </div>
          ))}
        </div>
      )}

                      <input
                        type="input"
                        id="customeinpu"
                        className={`form-control ${errors.courses && "is-invalid"} `} 
                        placeholder="search Course..."
                        onChange={searchCourses}
                        value={searchQuerycourse}
                      />
                      <div className="invalid-feedback">{errors.courses}</div>
                    </div>
                    {courses?.length > 0 && (
        <div className="user-list">
          {courses.map((course) => (
            <div key={course.courseId} className="usersingle">
              <label
                id="must"
                className="p-1 w-100"
                htmlFor={course.courseName}
                onClick={() => handleCourseClick(course)}
              >
                {course.courseName}
              </label>
            </div>
          ))}
        </div>
      )}
                  </div>
                </div>
              


<div className="form-group row">
                  <label className="col-sm-3 col-form-label">
                    Duration (Hours) <span className="text-danger">*</span>
                  </label>
                  <div className="col-sm-9">
                    <input
                      type="number"
                      name="durationInHours"
                      placeholder="Duration in Months"
                      className={`form-control ${errors.durationInHours && "is-invalid"} `}
                      value={batch.durationInHours}
                      onChange={handleBatchChange}
                    />
                    <div className="invalid-feedback">{errors.durationInHours}</div>
                  </div>
                </div>

                
                <div className="form-group row" >
                <label htmlFor="BatchImage" 
                className="col-sm-3 col-form-label">
                  batch Image 
                </label>
                <div className="col-sm-9 ">
                  <div className="custom-file">
                  <input
                    type="file"
                    className={`custom-file-input 
                      ${errors.BatchImage && "is-invalid"}`}
                    onChange={handleFileChange}
                    id="BatchImage"
                    name="BatchImage"
                    accept="image/*"
                  />
                  <label className="custom-file-label" 
                  htmlFor="BatchImage">
                    Choose file...
                  </label>
                  <div className="invalid-feedback">{errors.BatchImage}</div>
                </div>
                </div>
              </div>
              {batch.base64Image && (
                <div className="form-group row">
                  <label className="col-sm-3 col-form-label"></label>
                  <div className="col-sm-9">
                    <img
                      src={batch.base64Image}
                      alt="Selected"
                      style={{ width: "100px", height: "100px" }}
                    />
                  </div>
                </div>
              )}
              </div>
              <div className="cornerbtn">
                <button
                  className="btn btn-secondary"
                  type="button"
                  onClick={() => {
                    navigate(-1);
                  }}
                >
                  Cancel
                </button>
                <button className="btn btn-primary" onClick={handleSubmit}>Save</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CreateBatch;
