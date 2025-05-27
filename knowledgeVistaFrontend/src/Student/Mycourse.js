import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import baseUrl from "../api/utils";
import axios from "axios";
import errorimg from "../images/errorimg.png";
const Mycourse = () => {
  const token = sessionStorage.getItem("token");
  const [courses, setCourses] = useState([]);
  const navigate = useNavigate();
  useEffect(() => {
    const fetchItems = async () => {
      try {
        // Replace {userId} with the actual user ID
        const response = await axios.get(
          `${baseUrl}/view/courselist`,
          {
            headers: {
              Authorization: token,
            },
          }
        );

        const data = response.data;
        setCourses(data);
      } catch (error) {
        console.error("Error fetching courses:", error);
        // Handle error here, for example, show an error message
        throw error
      }
    };

    fetchItems();
  }, []);

  return (
    <>
      <div className="page-header"></div>
      <div className="row">
        <div className="col-sm-12">
          {courses.length === 0 ? (
            <div className="card">
              <div className="card-body">
                <div className="centerflex">
                  <div className="enroll">
                    <h3 className="mt-4">No courses Enrolled </h3>
                    <Link to="/dashboard/course" className="btn btn-primary">
                      Enroll Now
                    </Link>
                  </div>
                </div>
              </div>
            </div>
          ) : (
            <div className="row">
              {Array.isArray(courses) &&  courses.map((item) => (
                <div className=" course" key={item.courseId}>
                  <div className="card mb-3">
                    <img
                      style={{ cursor: "pointer" }}
                      onClick={(e) => {
                        navigate(item.courseUrl)
                      }}
                      className="img-fluid card-img-top"
                      src={`data:image/jpeg;base64,${item.courseImage}`}
                      onError={(e) => {
                        e.target.src = errorimg; // Use the imported error image
                      }}
                      alt="Course"
                    />
                    <div className="card-body">
                      <h5>
                        <a title={item.courseName} className="courseName" onClick={(e) => {e.preventDefault(); navigate(item.courseUrl)}}
                        href="#">
                          { item.courseName}
                        </a>
                      </h5>
                      <p title={item.courseDescription} className="courseDescription"> {item.courseDescription}</p>
                      <div className="mt-3">
        <div className="d-flex justify-content-between align-items-center mb-1">
          <small className="text-muted">Course Completed</small>
          <small className="text-muted">{item.progressPercent?.toFixed(1)}%</small>
        </div>
        <div className="progress" style={{ height: '8px', borderRadius: '4px' }}>
          <div
            className={`progress-bar ${item.progressPercent >= 95 ? 'bg-success' : 'bg-info'}`}
            role="progressbar"
            style={{ width: `${item.progressPercent || 0}%` }}
            aria-valuenow={item.progressPercent || 0}
            aria-valuemin="0"
            aria-valuemax="100"
          />
        </div>
      </div>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </>
  );
};

export default Mycourse;