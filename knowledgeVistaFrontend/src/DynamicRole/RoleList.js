import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom';
import baseUrl from '../api/utils';
import axios from 'axios';

const RoleList = () => {
  const[loading,setloading]=useState()
  const [dynamicRoles, setDynamicRoles] = useState([]);
    const navigate=useNavigate();
    const token=sessionStorage.getItem("token")
     const fetchDataForRole = async () => {
  try {
    setloading(true)
       const res = await axios.get(`${baseUrl}/roles/getAll`, {
      headers: {
        Authorization: token,
      },
    });
    if(res.status===200){
      setDynamicRoles(res.data)
    }
  }catch(err){
    console.log(err);
  }finally{
    setloading(false)
  }
}
    useEffect(() => {
     
fetchDataForRole();
    }, []);
  return (
 <div>
      <div className="page-header">
        <div className="page-block">
          <div className="row align-items-center">
            <div className="col-md-12">
              <div className="page-header-title">
                <h5 className="m-b-10">Role</h5>
              </div>
              <ul className="breadcrumb">
                <li className="breadcrumb-item">
                  <a
                    href="#"
                    onClick={(e) => {
                      e.preventDefault();
                      navigate("/admin/dashboard");
                    }}
                    title="dashboard"
                  >
                    <i className="feather icon-home"></i>
                  </a>
                </li>
                <li className="breadcrumb-item">
                  <a href="#">Role List</a>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>

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
                </div>
                <div className='card-body' style={{minHeight:"60vh"}}>
                  
                 <table className="table table-hover table-bordered table-sm " >
                    <thead className="thead-dark">
                      <tr>
                        <th scope='col'>#</th>
                        <th scope="col">RoleName</th>
                        <th scope="col">Parent Role </th>
                        <th scope="col">status</th>
                        <th scope='col'>Action</th>
                        </tr>
                        </thead>
                        <tbody>
    {loading ? (
      <tr>
        <td colSpan="5" style={{ textAlign: 'center' }}>
          <div className="outerspinner active">
            <div className="spinner"></div>
          </div>
        </td>
      </tr>
              ) : (
               dynamicRoles.map((role,index)=>(
                  <tr key={index}>
<th>{index+1}</th>
<th  className='py-2'>{role.rolename}</th>
<th  className='py-2'>{role.parentrolename}</th>
 <td className="py-2">
                              {role.isactive ? (
                                <div className="Activeuser">
                                  <i className="fa-solid fa-circle pr-3"></i>Active
                                </div>
                              ) : (
                                <div className="InActiveuser">
                                  <i className="fa-solid fa-circle pr-3"></i>Inactive
                                </div>
                              )}
                            </td>
                            <td  className='py-2'>
                              <button className='hidebtn'
                                  onClick={() => navigate(`/manage/role?roleId=${role.roleid}&roleName=${encodeURIComponent(role.rolename)}`)}
       >Manage</button></td>
                  </tr>
                ))
             )}</tbody>
                        </table>
              
                  </div>
             
                </div>
                </div>
                </div>
                </div>

  )
}

export default RoleList