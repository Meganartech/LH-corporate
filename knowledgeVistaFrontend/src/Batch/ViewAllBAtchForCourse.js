import React, { useEffect, useState } from "react";
import baseUrl from "../api/utils";
import errorimg from "../images/errorimg.png";
import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";
import altBatchImage from "../images/altBatchImage.jpg";
const ViewAllBAtchForCourse = () => {
  const { courseId } = useParams();
  const userId = sessionStorage.getItem("userid");
    const [submitting, setsubmitting] = useState(false);
    const token = sessionStorage.getItem("token");
  const MySwal = withReactContent(Swal);
  const [batch, setbatch] = useState([]);
  const navigate = useNavigate();
  const [selectedBatchId, setSelectedBatchId] = useState(null);


  
  useEffect(() => {
    const fetchBatchforcourse = async () => {
      try {
        if (courseId) {
          const response = await axios.get(
            `${baseUrl}/Batch/getAll/${courseId}`,
            {
              headers: {
                Authorization: token,
              },
            }
          );
          const data = response.data;
          setbatch(data);
        }
      } catch (err) {
        console.log(err);
      }
    };
    fetchBatchforcourse();
  }, []);
  const handleSendRequest = async () => {
  if (!selectedBatchId) return;

  try {
    const response = await axios.post(
      `${baseUrl}/Approval/Request?batchId=${selectedBatchId}`,
      {},
      {
        headers: {
          Authorization: token,
        },
      }
    );

    MySwal.fire({
      icon: "success",
      title: "Request Sent",
      text: response.data,
    });
    navigate("/dashboard/course")

    setSelectedBatchId(null); // Reset selection after success
  } catch (err) {
    const message =
      err.response?.status === 404 || err.response?.status === 400
        ? err.response.data
        : "Something went wrong";

    MySwal.fire({
      icon: "error",
      title: "Failed to Send Request",
      text: message,
    });
  }
};

  return (
    <div>
         {submitting && (
        <div className="outerspinner active">
          <div className="spinner"></div>
        </div>
      )}
    
      <div className="page-header"></div>
      <div className="card" style={{ height: "82vh", overflowY: "auto" }}>
        <div className="card-body">
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
          <h4>Select Your Batch</h4>
          {batch.length > 0 ? (
            <>
            <div className="vh-55">
              {batch
                .slice()
                .reverse()
                .map((item) => (
                 <div
  className={` course ${selectedBatchId === item.id ? "selectedBatch" : ""}`}
  key={item.id}
  onClick={() => setSelectedBatchId(item.id)}
>

                    <div className="card mb-0">
                      {item.batchImage ? (
                        <div className="img-fluid card-img-top ">
                          <div
                            title={item.batchTitle}
                            style={{
                              cursor: "pointer",
                              height: "150px", // Set a fixed height or adjust accordingly
                              display: "flex",
                              alignItems: "center",
                              justifyContent: "center",
                              position: "relative", // To ensure proper alignment for text overlay if needed
                            }}
                          >
                            <img
                              style={{
                                width: "100%", // Ensure the image fills the div
                                height: "100%",
                                cursor: "pointer",
                                objectFit: "cover", // Maintain aspect ratio and cover the div
                                borderRadius: "0.25rem", // Add rounded corners
                              }}
                              title={`${item.batchTitle} image`}
                              src={`data:image/jpeg;base64,${item.batchImage}`}
                              onError={(e) => {
                                e.target.src = errorimg; // Use the imported error image
                              }}
                              alt="Batch"
                            />
                          </div>
                        </div>
                      ) : (
                        
                        <div className="img-fluid card-img-top ">
                          <div
                            title={item.batchTitle}
                            style={{
                              cursor: "pointer",
                              height: "150px", // Set a fixed height or adjust accordingly
                              display: "flex",
                              alignItems: "center",
                              justifyContent: "center",
                              position: "relative", // To ensure proper alignment for text overlay if needed
                            }}
                          >
                            <img
                              src={altBatchImage} // Use altBatchImage as the source
                              alt={item.batchTitle}
                              style={{
                                width: "100%", // Ensure the image fills the div
                                height: "100%", // Maintain the height set for the div
                                objectFit: "cover", // Maintain aspect ratio and cover the div
                                borderRadius: "0.25rem", // Add rounded corners
                              }}
                            />
                            <h4
                              style={{
                                color: "white",
                                wordWrap: "break-word", // Ensure the text wraps if it overflows
                                margin: "0", // Remove any default margin
                                padding: "0 10px", // Add padding for better readability if needed
                                textAlign: "center",
                                position: "absolute", // Position the text on top of the image
                                zIndex: 1, // Ensure the text appears above the image
                              }}
                            >
                              {item.batchTitle}
                            </h4>
                          </div>
                        </div>
                      )}
                      <div className="card-body">
                        <h5
                          className="courseName"
                          title={item.batchTitle}
                          style={{ cursor: "pointer" }}
                          // onClick={(e) => {
                          //   handleClick(
                          //     e,
                          //     item.courseId,
                          //     item.amount,
                          //     item.courseUrl
                          //   );
                          // }}
                        >
                          {item.batchTitle}
                        </h5>

                        <p title= {item.course.join(", ")} className="batchlist">
                          <b> Courses :</b> {item.course.join(", ")}
                        </p>
                      <p title={item.duration} className="batchlist">
                      <b>Duration (Hours):</b> {item.durationInHours}
                    </p>
                       
                      </div>
                    </div>
                  </div>
                ))}
   

            </div>
             <div className="cornerbtn">
  <button
    className="btn btn-secondary"
    type="button"
    onClick={() => setSelectedBatchId(null)}
  >
    Cancel
  </button>
  <button
    className="btn btn-primary"
    disabled={!selectedBatchId}
  onClick={handleSendRequest}
  >
    Send Request
  </button>
</div>
</>
          ) : (
            <div>
              <h1 className="text-primary ">No Batch Found </h1>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ViewAllBAtchForCourse;
