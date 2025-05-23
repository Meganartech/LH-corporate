import React, { useEffect, useState } from "react";
import baseUrl from "../../api/utils.js";
import axios from "axios";
import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";
import errorimg from "../../images/errorimg.png";
import Header from "../../Common Components/Header";
import pcoded from "../../assets/js/pcoded.js"
import Sidebar from "../../Common Components/Sidebar.js";
import { useNavigate } from "react-router-dom";
const ViewCourseVps = () => {
  useEffect(() => {
      pcoded();  
      },[]);
  const MySwal = withReactContent(Swal);
  const [searchQuery, setSearchQuery] = useState("");
  const [course, setCourse] = useState([]);  
  const [submitting,setsubmitting]=useState(false);
  const[notfound,setnotfound]=useState(false);
  const islogedin=sessionStorage.getItem("token")!==null;
  const navigate=useNavigate();
  const Currency=sessionStorage.getItem("Currency")
  const[orderData,setorderData]=useState({
    userId:"",
    courseId:"",
    amount:"" ,
    courseAmount:"",
    coursename:"",
    installment:"",
    paytype:"",
    url:""
})
  useEffect(() => {
    const fetchCourse = async () => {
      try {
        
          setsubmitting(true); // Show loading indicator
          const response = await axios.get(`${baseUrl}/course/viewAllVps`);
          setsubmitting(false); // Hide loading indicator
          const data = response.data;
          setCourse(data); // Set the fetched course data
        
      } catch (error) {
        setnotfound(true)
        console.error('Error fetching courses:', error);
        throw error
      }
    };

   
      fetchCourse();
    
  }, []); // This effect depends on the `showInLandingPage` state

  const handleSearchChange = (e) => {
    setSearchQuery(e.target.value);
  };
  const filteredCourses = course.filter((item) => {
    const name = item.courseName ? item.courseName.toLowerCase() : "";
    return name.includes(searchQuery.toLowerCase());
  });

  const userId = sessionStorage.getItem("userid");
  const token = sessionStorage.getItem("token");
return (
  <>
    {islogedin && <Sidebar />}
    <Header searchQuery={searchQuery}
        handleSearchChange={handleSearchChange}
        setSearchQuery={setSearchQuery} />
  
    
     <div className="pcoded-main-container"   
     style={{ marginLeft: islogedin ? undefined : '0px' }}>
     <div className="pcoded-content" >
    <div className="page-header"></div>
    
    {filteredCourses && filteredCourses.length > 0 ? (
      <div>
       
        <h4 style={{color:"white"}}>Courses For You</h4>
        < div className="course-grid ">
        { filteredCourses
  .map((item, index) => (
    <div className="course" key={index}>
      <div className="card mb-3">
        <img
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
          
          >
            {item.courseName}
          </h5>
          <p title={item.courseDescription} className="courseDescription">
            {item.courseDescription}
          </p>
          <div>
            {item.amount === 0 ? (
              <a
                href="#"
                onClick={(e) => {
                  e.preventDefault();
                  navigate(item.courseUrl);
                }}
                className="btn btn-sm btn-outline-success"
              >
                Enroll for Free
              </a>
            ) : (
              <div className="amountGrid">
                <div className="amt">
                  <i
                    className={
                      Currency === "INR"
                        ? "fa-solid fa-indian-rupee-sign pr-1"
                        : "fa-solid fa-dollar-sign pr-1"
                    }
                  ></i>
                  <span>{item.amount}</span>
                </div>
               
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  ))}

        </div>
      </div>
    ) : notfound ? (
      <h1>No Course Found</h1>
    ) : null}
    </div>
    </div>
  </>
);

};

export default ViewCourseVps;