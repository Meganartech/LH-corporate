import React, {  useState } from "react";
import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";
import baseUrl from "../../api/utils";
import errorimg from "../../images/errorimg.png";
import axios from "axios";
import { useNavigate } from "react-router-dom";
const CourseView = ({ filteredCourses }) => {
  const MySwal = withReactContent(Swal);
  const userId = sessionStorage.getItem("userid");
  const [submitting, setsubmitting] = useState(false);
  const token = sessionStorage.getItem("token");
  const role=sessionStorage.getItem("role");
  const navigate =useNavigate();
  const Currency=sessionStorage.getItem("Currency");
 
  const handleClick = async (event, id, url) => {
    event.preventDefault();
   
      try {
        const formdata = JSON.stringify({ courseId: id });
        const response = await axios.post(
          `${baseUrl}/CheckAccess/match`,
          formdata,
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: token,
            },
          }
        );

        if (response.status === 200) {
          const message = response.data;
          navigate(message);
        }else if(response.status===204){
          MySwal.fire({
            icon: "error",
            title: "Oops...",
            text: "cannot Find the  Course ",
          });
        }
      } catch (error) {
        if (error.response.status === 401) {
          MySwal.fire({
            icon: "error",
            title: "Oops...",
            text: "cannot Access Course ",
          }).then(()=>{
            navigate("/unauthorized")
          })
        }else if (error.response.status === 400) {
  MySwal.fire({
    icon: "warning",
    title: "Access Denied",
    text: "This course is not assigned to you. Do you want to send an access request?",
    showCancelButton: true,
    confirmButtonText: "Send Request",
    cancelButtonText: "Cancel",
  }).then((result) => {
    if (result.isConfirmed) {
      navigate(`/batch/viewall/${id}`)
    }
  });
}
 else {
          throw error
        }
      }
    
  };

  return (
    <>
      {submitting && (
        <div className="outerspinner active">
          <div className="spinner"></div>
        </div>
      )}
      <div className="page-header"></div>
    
      {filteredCourses.length > 0 ? (
        <div className="row">
        
          {filteredCourses
            .slice()
            .reverse()
            .map((item) => (
              <div className=" course" key={item.courseId}>
                <div className="card mb-3 ">
                  <img
                   style={{ cursor: "pointer" }}
                   onClick={(e) => {
                     handleClick(
                       e,
                       item.courseId,
                       item.courseUrl
                     );
                   }}
                   title={`${item.courseName} image`}
                    className="img-fluid card-img-top"
                    src={`data:image/jpeg;base64,${item.courseImage}`}
                    onError={(e) => {
                      e.target.src = errorimg; // Use the imported error image
                    }}
                    alt="Course"
                  />
                  <div className="card-body">
                    <h5
                      className="courseName"
                      title={item.courseName}
                      style={{ cursor: "pointer" }}
                      onClick={(e) => {
                        handleClick(
                          e,
                          item.courseId,
                          item.courseUrl
                        );
                      }}
                    >
                      {item.courseName}
                    </h5>
                   <p title={item.courseDescription} className="courseDescription">
                    {item.courseDescription}
                    </p>
                  
                  </div>
                </div>
              </div>
            ))}
                 </div>
      ) : (
        <div >
        <h1 className="text-light ">No Course Found </h1>
        </div>
      )}
    
    </>
  );
};

export default CourseView;