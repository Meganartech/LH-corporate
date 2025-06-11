// import axios from "axios";
// import React, { useEffect, useState } from "react";
// import baseUrl from "../../api/utils";
// import { useNavigate } from "react-router-dom";
// import Swal from "sweetalert2";
// import withReactContent from "sweetalert2-react-content";

// const AccessRequest = () => {
  
//     const MySwal = withReactContent(Swal);
//   const token = sessionStorage.getItem("token");
//   const [requests, setRequests] = useState([]);
//   const navigate = useNavigate();

//   const feetchData = async () => {
//     try {
//       const res = await axios.get(`${baseUrl}/get/Approvals`, {
//         headers: {
//           Authorization: token,
//         },
//       });
//       if (res.status === 200) {
//         setRequests(res.data);
//       }
//     } catch (err) {}
//   };
//   useEffect(() => {
//     feetchData();
//   }, []);


//    const RejectEnrollmentApproval=async(id)=>{
//       try{
//         MySwal.fire({
//           title: "Reject ?",
//         text: "By rejecting, the user Request will be Rejected. Are you sure you want to reject the Request?",
//           icon: "warning",
//           showCancelButton: true,
//           confirmButtonColor: "#d33",
//           confirmButtonText: "Reject",
//           cancelButtonText: "Cancel",
//       }).then(async (result) => {
//         if(result.isConfirmed){
//   const response=await axios.post(`${baseUrl}/Reject/EnrollmentApproval/${id}`,{}, {
//     headers: {
//       Authorization: token,
//     },
//       });
//   MySwal.fire({
//     title:"Rejected",
//     icon:"success",
//     text:"Request Rejected Successfully"
//   }).then(()=>{
//     feetchData();
//   })
//         }else{
//           return
//         }
//       })
//       }catch(error){
//   console.log(error)
//       }
    
//     }
//     const ApproveEnrollmentApproval=async(id)=>{
//       try{
//         MySwal.fire({
//           title: "Approve ?",
//           text: `Are you sure you want to Approve The Request ?`,
//           icon: "warning",
//           showCancelButton: true,
         
//           confirmButtonText: "Approve",
//           cancelButtonText: "Cancel",
//       }).then(async (result) => {
//   const response=await axios.post(`${baseUrl}/approve/EnrollmentApproval/${id}`,{}, {
//     headers: {
//       Authorization: token,
//     },
//       });
//   MySwal.fire({
//     title:"Approved",
//     icon:"success",
//     text:"Request Approved Successfully"
//   }).then(()=>{
//    feetchData();
//   })
//       })
//       }catch(error){
//   console.log(error)
//       }
//     }
//   return (
//     <div>
//       <div className="page-header">
//         <div className="page-block">
//           <div className="row align-items-center">
//             <div className="col-md-12">
//               <div className="page-header-title">
//                 <h5 className="m-b-10">Settings </h5>
//               </div>
//               <ul className="breadcrumb">
//                 <li className="breadcrumb-item">
//                   <a
//                     href="#"
//                     onClick={() => {
//                       navigate("/dashboard/course");
//                     }}
//                     title="dashboard"
//                   >
//                     <i className="feather icon-home"></i>
//                   </a>
//                 </li>
//                 <li className="breadcrumb-item">
//                   <a href="#">Requests</a>
//                 </li>
//               </ul>
//             </div>
//           </div>
//         </div>
//       </div>
//       <div className="row">
//         <div className="col-sm-12">
//           <div className="card">
//             <div className="card-header">
//               <div className="navigateheaders ">
//                 <div
//                   onClick={() => {
//                     navigate(-1);
//                   }}
//                 >
//                   <i className="fa-solid fa-arrow-left"></i>
//                 </div>
//                 <div></div>
//                 <div
//                   onClick={() => {
//                     navigate(-1);
//                   }}
//                 >
//                   <i className="fa-solid fa-xmark"></i>
//                 </div>
//               </div>
//               <div className="tableheader ">
//                 <h4> Request list</h4>
//               </div>
//               <div className="card-body">
//                 <div className="table-container">
//                   <table className="table table-hover table-bordered table-sm">
//                     <thead className="thead-dark">
//                       <tr>
//                         <th scope="col">#</th>
//                         <th scope="col">Username</th>
//                         <th scope="col">Requested batch</th>
//                         <th scope="col"> Time</th>
//                         <th scope="col" colSpan={2}> Action</th>
//                       </tr>
//                     </thead>

//                     <tbody>
//                       {requests.map((request, index) => (
//                         <tr key={request.userId}>
//                           <th scope="row">{index + 1}</th>
//                           <td className="py-2">{request.username}</td>
//                           <td className="py-2">{request.batchName}</td>
//                           <td className="py-2">
//                             {new Date(request.requestTime).toLocaleString(
//                               "en-US",
//                               {
//                                 dateStyle: "medium",
//                                 timeStyle: "short",
//                               }
//                             )}
//                           </td>
//  <td className="text-center padnone">
                           
//                               <button
//                                 type="button"
//                                 onClick={()=>{ApproveEnrollmentApproval(request.approvalId)}}
//                                 className="btn  btn-icon btn-success"
//                                 title="Approve"
//                               >
//                                 <i className="feather icon-check-circle"></i>
//                               </button>
//                           </td>
//                           <td className="text-center padnone">
                           
//                               <button
//                                 type="button"
//                                 title="Reject"
//                                onClick={()=>{RejectEnrollmentApproval(request.approvalId)}}
//                                 className="btn  btn-icon btn-danger"
//                               >
//                                 <i className="fa-solid fa-xmark"></i>
//                               </button>
//                           </td>
//                         </tr>
//                       ))}
//                     </tbody>
//                   </table>
//                 </div>
//               </div>
//             </div>
//           </div>
//         </div>
//       </div>
//     </div>
//   );
// };

// export default AccessRequest;


import axios from "axios";
import React, { useEffect, useState } from "react";
import baseUrl from "../../api/utils";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";

const AccessRequest = () => {
  const MySwal = withReactContent(Swal);
  const token = sessionStorage.getItem("token");
  const [requests, setRequests] = useState([]);
  const [selectedIds, setSelectedIds] = useState([]);
  const navigate = useNavigate();

  const feetchData = async () => {
    try {
      const res = await axios.get(`${baseUrl}/get/Approvals`, {
        headers: {
          Authorization: token,
        },
      });
      if (res.status === 200) {
        setRequests(res.data);
        setSelectedIds([]); // Clear selection on reload
      }
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    feetchData();
  }, []);

  const handleCheckboxChange = (id) => {
    setSelectedIds((prev) =>
      prev.includes(id) ? prev.filter((x) => x !== id) : [...prev, id]
    );
  };

  const handleSelectAll = () => {
    if (selectedIds.length === requests.length) {
      setSelectedIds([]);
    } else {
      setSelectedIds(requests.map((req) => req.approvalId));
    }
  };

const bulkAction = async (action) => {
  if (selectedIds.length === 0) return;

  const actionText = action === "approve" ? "Approve" : "Reject";
  const urlPart = action === "approve" ? "approve" : "Reject";

  const confirm = await MySwal.fire({
    title: `${actionText} Selected?`,
    text: `Are you sure you want to ${actionText.toLowerCase()} selected requests?`,
    icon: "warning",
    showCancelButton: true,
    confirmButtonText: `${actionText}`,
    cancelButtonText: "Cancel",
  });

  if (confirm.isConfirmed) {
    try {
      const url = `${baseUrl}/${urlPart}/EnrollmentApproval`;
      await axios.post(url, selectedIds, {
        headers: {
          Authorization: token,
          "Content-Type": "application/json",
        },
      });

      MySwal.fire(
        `${actionText}d!`,
        `Selected requests ${actionText.toLowerCase()}ed successfully.`,
        "success"
      ).then(() => {
        feetchData();
        setSelectedIds([]); // clear selection after action
      });
    } catch (error) {
      console.error("Bulk action failed:", error);
      MySwal.fire("Error", `Failed to ${actionText.toLowerCase()} requests.`, "error");
    }
  }
};

  return (
    <div>
      {/* Header */}
      <div className="page-header">
        <div className="page-block">
          <div className="row align-items-center">
            <div className="col-md-12">
              <div className="page-header-title">
                <h5 className="m-b-10">Settings</h5>
              </div>
              <ul className="breadcrumb">
                <li className="breadcrumb-item">
                  <a
                    href="#"
                    onClick={() => navigate("/dashboard/course")}
                    title="dashboard"
                  >
                    <i className="feather icon-home"></i>
                  </a>
                </li>
                <li className="breadcrumb-item">
                  <a href="#">Requests</a>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>

      

      {/* Table */}
      <div className="row">
        <div className="col-sm-12">
          <div className="card">
            <div className="card-header">
              <div className="navigateheaders">
                <div onClick={() => navigate(-1)}>
                  <i className="fa-solid fa-arrow-left"></i>
                </div>
                <div></div>
                <div onClick={() => navigate(-1)}>
                  <i className="fa-solid fa-xmark"></i>
                </div>
              </div>
              <div className="tableheader3">
                <h4>Request List</h4>
                {/* Bulk Actions */}
      {selectedIds.length > 0 && (
        <div className=" d-flex gap-2">
          <button
            className="btn btn-success btn-sm"
            onClick={() => bulkAction("approve")}
          >
            <i className="feather icon-check-circle mr-1" /> Approve Selected
          </button>
          <button
            className="btn btn-danger btn-sm"
            onClick={() => bulkAction("reject")}
          >
            <i className="fa-solid fa-xmark mr-1" /> Reject Selected
          </button>
        </div>
      )}
              </div>
              <div className="card-body">
                <div className="table-container">
                  <table className="table table-hover table-bordered table-sm">
                    <thead className="thead-dark">
                      <tr>
                        <th     style={{width:"15px"}}>
                          <input
                            type="checkbox"
                        
                            checked={
                              selectedIds.length === requests.length &&
                              requests.length > 0
                            }
                            onChange={handleSelectAll}
                          />
                        </th>
                        <th>Username</th>
                        <th>Requested Batch</th>
                        <th>Time</th>
                      </tr>
                    </thead>
                    <tbody>
                      {requests.map((request, index) => (
                        <tr key={request.userId}>
                          <td>
                            <input
                              type="checkbox"
                              checked={selectedIds.includes(request.approvalId)}
                              onChange={() =>
                                handleCheckboxChange(request.approvalId)
                              }
                            />
                          </td>
                          <td className="py-2">{request.username}</td>
                          <td className="py-2">{request.batchName}</td>
                          <td className="py-2">
                            {new Date(
                              request.requestTime
                            ).toLocaleString("en-US", {
                              dateStyle: "medium",
                              timeStyle: "short",
                            })}
                          </td>
                       
                        </tr>
                      ))}
                      {requests.length === 0 && (
                        <tr>
                          <td colSpan={7} className="text-center py-3">
                            No requests found.
                          </td>
                        </tr>
                      )}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div> 
    </div>
  );
};

export default AccessRequest;

