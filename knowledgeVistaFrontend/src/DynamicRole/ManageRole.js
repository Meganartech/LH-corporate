import { useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import baseUrl from '../api/utils';
import altBatchImage from '../images/altBatchImage.jpg'
import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";
import errorimg from '../images/errorimg.png'
import axios from 'axios';

const ManageRole = () => {
  const [searchParams] = useSearchParams();
  const roleId = searchParams.get('roleId');
  const roleName = searchParams.get('roleName');
  const [selectedbatch,setselectbatches]=useState(null)
  const[submitting,setsubmitting]=useState({
    assigned:false,
    other:false,
    courses:false
  })
  const itemsperpage=20
  const [courses, setCourses] = useState([]);
  const MySwal = withReactContent(Swal);
  const token=sessionStorage.getItem("token");
  const navigate=useNavigate();
  const[assignedBatches,setassignedBatches]=useState([]);
  const[otherBatches,setotherBatches]=useState([]);
  const [view, setView] = useState(null); 
  const[filteredBatches,setfilteredBatches]=useState([]);
  const [currentPage, setCurrentPage] = useState(0);
const [totalPages, setTotalPages] = useState(1);
const [AssignedcurrentPage, setAssignedcurrentPage] = useState(0);
const [AssignedtotalPages, setAssignedtotalPages] = useState(1);
   const fetchAssignedBAtches=async(page=0,size=8)=>{
    try {
      setsubmitting((prev)=>({
        ...prev,
        assigned:true,
      }))
      const response = await axios.get(`${baseUrl}/view/Batches?roleId=${roleId}&page=${page}&size=${size}`,{
        headers:{
          Authorization:token
        }
      });
      if(response?.status===200){
        if(Array.isArray(response?.data?.content)){
      setassignedBatches(response?.data?.content)
      setAssignedtotalPages(response?.data?.totalPages || 1);
        }
       
      }
    }catch(err){
      if(err.response && err.response.status===401){
        navigate("/unauthorized");
      }else{
        console.log(err);
      }
      }finally{
        setsubmitting((prev)=>({
          ...prev,
          assigned:false,
        }))
      }
  }
  const fetchOtherBAtches=async(page=0,size=8)=>{
    try {
      setsubmitting((prev)=>({
        ...prev,
      other:true,
      }))
      const response = await axios.get(`${baseUrl}/view/OtherBatches?roleId=${roleId}&page=${page}&size=${size}`,{
        headers:{
          Authorization:token
        }
      });
      if(response?.status===200){
        if(Array.isArray(response?.data?.content)){
        setotherBatches(response?.data?.content)
        setfilteredBatches(response?.data?.content)
        setTotalPages(response?.data?.totalPages || 1);
        } 
      }
    }catch(err){
      if(err.response && err.response.status===401){
        navigate("/unauthorized");
      }else{
        console.log(err);
      }
      }finally{
        setsubmitting((prev)=>({
          ...prev,
          assigned:false,
        }))
      }
  }
 
  useEffect(() => {
    fetchData()
    fetchOtherBAtches();
    fetchAssignedBAtches();
}, []);

const handleBatchSelection = (batchId) => {
  if(selectedbatch===batchId){
    setselectbatches(null)
  }else{
  setselectbatches(batchId);
  }
};
const handleAssignCourse = async () => {
  if (!selectedbatch) {
      return;
  }

  try {
      const response = await axios.post(
          `${baseUrl}/roles/AssignBatch`,
          {},
          {
              headers: {
                  Authorization: token,
              },
              params: {
                  roleId: roleId,
                  batchId: selectedbatch,
              },
          }
      );

      if (response.status === 200) {
          await MySwal.fire({
              icon: "success",
              title: "batch Assigned!",
              text: response?.data,
          });
            fetchOtherBAtches();
    fetchAssignedBAtches();
      }
  } catch (error) {
      console.error("Error assigning course:", error);
      MySwal.fire({
          icon: "error",
          title: "An unexpected error occurred!",
          text: "Try again after some time.",
      });
  }
};


const [selectedCourses, setSelectedCourses] = useState([]);

const handleCourseSelection = (courseName) => {
  setSelectedCourses((prev) =>
    prev.includes(courseName)
      ? prev.filter((name) => name !== courseName)
      : [...prev, courseName]
  );
 
};
useEffect(()=>{
if(view==="assigned"){
  fetchAssignedBAtches(0,itemsperpage);
}else if(view === "other"){
  fetchOtherBAtches(0,itemsperpage);
}
},[view])
useEffect(()=>{
  const filter = otherBatches.filter((batch) =>
    selectedCourses.length === 0 ||
    selectedCourses.some((course) => batch?.coursenames?.includes(course))
  );
  setfilteredBatches(filter)
},[selectedCourses])
useEffect(() => {
  fetchOtherBAtches(currentPage,itemsperpage);
}, [currentPage]);

  const fetchData = async () => {
    try {
      setsubmitting((prev)=>({
        ...prev,
        courses:true,
      }))
        const response1 = await axios.get(`${baseUrl}/course/getList`, {
            headers: {
                Authorization: token
            }
        });
        const data = response1.data;
        setCourses(data);
    } catch (error) {
      if(error.response && error.response.status===401){
        navigate("/unauthorized");
      }else{
        console.error('Error fetching user data:', error);
        throw error
      }
    }finally{
      setsubmitting((prev)=>({
        ...prev,
        assigned:false,
      }))
    }
};

const handleAssignedPageChange = (page) => {
  if (page >= 0 && page < AssignedtotalPages) {
    setAssignedcurrentPage(page);
  }
};
const handlePageChange = (page) => {
  if (page >= 0 && page < totalPages) {
    setCurrentPage(page);
  }
};
const AssignedrenderPaginationButtons = () => {
  const buttons = [];
  for (let i = 0; i < AssignedtotalPages; i++) {
    buttons.push(
      <li
        className={`page-item ${i === AssignedcurrentPage ? "active" : ""}`}
        key={i}
      >
        <button className="page-link" onClick={() => handleAssignedPageChange(i)}>
          {i + 1}
        </button>
      </li>
    );
  }
  return buttons;
};

const renderPaginationButtons = () => {
  const buttons = [];
  for (let i = 0; i < totalPages; i++) {
    buttons.push(
      <li
        className={`page-item ${i === currentPage ? "active" : ""}`}
        key={i}
      >
        <button className="page-link" onClick={() => handlePageChange(i)}>
          {i + 1}
        </button>
      </li>
    );
  }
  return buttons;
};

  return (
    <div>
    <div className="page-header">
    <div className="page-block">
          <div className="row align-items-center">
            <div className="col-md-12">
              <div className="page-header-title">
                <h5 className="m-b-10">Assign batch </h5>
              </div>
              <ul className="breadcrumb">
                <li className="breadcrumb-item">
                  <a
                    href="#"
                    onClick={() => {
                      navigate("/admin/dashboard");
                    }}
                    title="dashboard"
                  >
                    <i className="feather icon-home"></i>
                  </a>
                </li>
                <li className="breadcrumb-item">
                  <a href="#" onClick={()=>{navigate("/role/ViewAll")}}>
                    {roleName}
                  </a>
                </li>
                <li className="breadcrumb-item">
                  <a href="#" >
                    Assign Batch
                  </a>
                </li>
              </ul>
            </div>
          </div>
        </div>
    </div>
    <div className="card">
      <div className="card-body">
      <div className="row">
      <div className="col-12">
        <div className='navigateheaders'>
      <div onClick={()=>{navigate(-1)}}><i className="fa-solid fa-arrow-left"></i></div>
      <div></div>
      <div onClick={()=>{navigate(-1)}}><i className="fa-solid fa-xmark"></i></div>
      </div>
         <div className='tableheader '><h4>Assign batch </h4> <div className='selectandadd'>
           <p><b className='text-blue'></b></p><p><b className='text-blue'>Role Name : </b>{roleName}</p></div></div>
         <div className='courseBatchSplit '>
          <div className='courseList card'>
            {courses.map((item)=>(
              <span key={item?.courseId}   className='checkboxes'>  <input
              type="checkbox"
              onChange={() => handleCourseSelection(item?.courseName)}
            /> <label title={item?.courseName}>{item?.courseName}</label></span>
            ))}
             
          </div>

          <div className="batchView pl-2 ">
      {view !== "other" && (
        <div>
          <div className="head">
            <h5>Assigned Batches</h5>
            {(assignedBatches.length > 4 ||view === "assigned") &&  <button
              className="text-blue hidebtn"
              onClick={() => setView(view === "assigned" ? null : "assigned")}
            >
              {view === "assigned" ? "Show Less" : "See All"}
            </button>}
          </div>
        
          {assignedBatches.length > 0 ? (
            <div>
              <div className="batch-container-small">
  {(view === "assigned" ? assignedBatches : assignedBatches.slice(0, 4)).map((item) => (
    <div className="batch-small pointer mb-1 card" key={item.id}>
      {item.batchImage ? (
        <img
          title={`${item.batchtitle} image`}
          className="img-fluid card-img-top pointer"
          src={`data:image/jpeg;base64,${item.batchImage}`}
          onError={(e) => {
            e.target.src = errorimg;
          }}
          //onClick={() => navigate(`/batch/viewcourse/${item.batchtitle}/${item.batchId}`)}
          alt={item?.batchtitle || "Batch"}
        />
      ) : (
        <div
          className="img-fluid card-img-top"
          title={item.batchtitle}
          //onClick={() => navigate(`/batch/viewcourse/${item.batchtitle}/${item.batchId}`)}
          style={{
            backgroundImage: `url(${altBatchImage})`,
            backgroundSize: "cover",
            backgroundPosition: "center",
            height: "100px",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
          }}
        >
          <h5 style={{ color: "white", textAlign: "center", margin: 0 }}>
            {item?.batchtitle}
          </h5>
        </div>
      )}
      <div className="card-body pt-1 pb-1">
        <p
          className="courseName mb-0 pointer"
          title={item.batchtitle}
         // onClick={() => navigate(`/batch/viewcourse/${item.batchtitle}/${item.batchId}`)}
        >
          <b>{item.batchtitle}</b>
        </p>
       <small className="batchlist">
        <b>Courses:</b> {item?.coursenames || "N/A"}
      </small>
      <small className="batchlist">
        <b>Duration:</b> {item.durationinhours ? `${item.durationinhours} hrs` : "N/A"}
      </small>

      </div>
    </div>
  ))}</div>
  {AssignedtotalPages > 1 && (
   <div className="cornerbtn">
      <ul className="pagination">
        <li
          className={`page-item ${AssignedcurrentPage === 0 ? "disabled" : ""}`}
          key="prev"
        >
          <button
            className="page-link"
            onClick={() => handleAssignedPageChange(AssignedcurrentPage - 1)}
            disabled={AssignedcurrentPage === 0}
          >
            «
          </button>
        </li>

        {AssignedrenderPaginationButtons()}

        <li
          className={`page-item ${
            AssignedcurrentPage === AssignedtotalPages - 1 ? "disabled" : ""
          }`}
          key="next"
        >
          <button
            className="page-link"
            onClick={() => handleAssignedPageChange(AssignedcurrentPage + 1)}
            disabled={AssignedcurrentPage === AssignedtotalPages - 1}
          >
            »
          </button>
        </li>
      </ul>
      </div>
    )}
  </div>
) : !submitting.assigned ? (
 <div className='w-100 align-center'> <small className="text-muted ">No Batches Enrolled</small></div>
) : null}

</div>)}
        
      {view === null && <hr className="borderLine p-0 my-2" />}
      {view !== "assigned" && (
        <div>
          <div className="head">
            <h5>Other Batches</h5>
            {(otherBatches.length > 4 || view==="other" )&&    <button
              className="text-blue hidebtn"
              onClick={() => setView(view === "other" ? null : "other")}
            >
              {view === "other" ? "Show Less" : "See All"}
            </button>}
          </div>
          {filteredBatches.length > 0 ? (<div>
          <div className="batch-container-small">
          {(view === "other" ? filteredBatches : filteredBatches.slice(0,4)).map((item) => (
  <div
  className={`batch-small mb-1 card pointer ${selectedbatch === item.id ? "selectedBatch" : ""}`}
  key={item.batchid}
  onClick={() => handleBatchSelection(item.id)}
>
    {item?.batchImage ? (
      <img
        style={{ cursor: "pointer" }}
        title={`${item?.batchtitle} image`}
        className="img-fluid card-img-top"
        src={`data:image/jpeg;base64,${item?.batchImage}`}
        onError={(e) => (e.target.src = errorimg)}
        alt={item?.batchtitle || "Batch"}
      />
    ) : (
      <div
        className="img-fluid card-img-top"
        title={item?.batchtitle}
        style={{
          backgroundImage: `url(${altBatchImage})`,
          backgroundSize: "cover",
          backgroundPosition: "center",
          height: "100px",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        <h5 style={{ color: "white", textAlign: "center", margin: 0 }}>{item?.batchtitle}</h5>
      </div>
    )}

    <div className="card-body pt-1 pb-1">
      <p
        className="courseName mb-0 pointer"
        title={item?.batchtitle}
      >
        <b>{item?.batchtitle}</b>
      </p>

      <small className="batchlist">
        <b>Courses:</b> {item?.coursenames || "N/A"}
      </small>
      <small className="batchlist">
        <b>Duration:</b> {item.durationinhours ? `${item.durationinhours} hrs` : "N/A"}
      </small>


     
    </div>
  </div>
  
)) }</div>
 {totalPages > 1 && (
   <div className="cornerbtn">
      <ul className="pagination">
        <li
          className={`page-item ${currentPage === 0 ? "disabled" : ""}`}
          key="prev"
        >
          <button
            className="page-link"
            onClick={() => handlePageChange(currentPage - 1)}
            disabled={currentPage === 0}
          >
            «
          </button>
        </li>

        {renderPaginationButtons()}

        <li
          className={`page-item ${
            currentPage === totalPages - 1 ? "disabled" : ""
          }`}
          key="next"
        >
          <button
            className="page-link"
            onClick={() => handlePageChange(currentPage + 1)}
            disabled={currentPage === totalPages - 1}
          >
            »
          </button>
        </li>
      </ul>
      </div>
    )}
    </div>):!submitting.other ? (
 <div className='w-100 align-center'> <small className="text-muted ">No Batch Found</small></div>
) :  null}
<div>

</div>
        </div>
      )}
    </div>
         </div>
          <div className='cornerbtn'>
          <button className='btn btn-secondary' onClick={()=>{navigate(-1)}} >Cancel</button>
            <button className='btn btn-primary' 
            disabled={selectedbatch==null}
            onClick={handleAssignCourse} 
            >Assign</button>
          </div>
        </div>
      </div>
      </div>
      </div>
    </div>
  );
};

export default ManageRole;
