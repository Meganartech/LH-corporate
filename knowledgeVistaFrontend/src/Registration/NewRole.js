import React, { useState, useEffect } from "react";
import axios from "axios";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";
import baseUrl from "../api/utils";

const NewRole = () => {
  const [roles, setRoles] = useState([]);
  const [newRoleName, setNewRoleName] = useState("");
  const [parentRoleId, setParentRoleId] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    axios
      .get(`${baseUrl}/roles/all`)
      .then((response) => setRoles(response.data))
      .catch((error) => console.error("Error fetching roles:", error));
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!newRoleName.trim()) {
      Swal.fire("Error", "Enter a valid role name", "error");
      return;
    }

    try {
      await axios.post(`${baseUrl}/roles/add`, {
        roleName: newRoleName,
        parentRoleId: parentRoleId || null,
      });

      await new Promise(resolve => setTimeout(resolve, 500));
      Swal.fire({
        title: "Success!",
        text: "Role created successfully. Would you like to register someone with this role?",
        icon: "success",
        showCancelButton: true,
        confirmButtonText: "Yes, register now",
        cancelButtonText: "No, thanks"
      }).then((result) => {
        if (result.isConfirmed) {
          navigate(`/register/${newRoleName.toLowerCase()}`);
        } else {
          setNewRoleName("");
          setParentRoleId("");
        }
      });
    } catch (error) {
      Swal.fire("Error", "Failed to create role", "error");
    }
  };

  return (
    <div>
      <div className="page-header">
        <div className="page-block">
          <div className="row align-items-center">
            <div className="col-md-12">
              <div className="page-header-title">
                <h5 className="m-b-10">Settings</h5>
              </div>
              <ul className="breadcrumb">
                <li className="breadcrumb-item">
                  <a href="#" onClick={() => navigate("/admin/dashboard")} title="dashboard">
                    <i className="feather icon-home"></i>
                  </a>
                </li>
                <li className="breadcrumb-item">
                  <a href="#">Create Role</a>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>

      <div className="card">
        <div className="card-body">
          <div className="navigateheaders">
            <div onClick={() => navigate(-1)}>
              <i className="fa-solid fa-arrow-left"></i>
            </div>
            <div></div>
            <div onClick={() => navigate(-1)}>
              <i className="fa-solid fa-xmark"></i>
            </div>
          </div>

          <h4>Create New Role</h4>
          <form onSubmit={handleSubmit}>
            <div className="form-group row">
              <label className="col-sm-3 col-form-label">
                Role Name <span className="text-danger">*</span>
              </label>
              <div className="col-sm-9">
                <input
                  type="text"
                  placeholder="Enter Role Name"
                  value={newRoleName}
                  onChange={(e) => setNewRoleName(e.target.value)}
                  className="form-control"
                />
              </div>
            </div>

            <div className="form-group row">
              <label className="col-sm-3 col-form-label">Parent Role</label>
              <div className="col-sm-9">
                <select
                  value={parentRoleId}
                  onChange={(e) => setParentRoleId(e.target.value)}
                  className="form-control"
                >
                  <option value="">Parent Role (optional)</option>
                  {roles
                  .filter((role) => {
                    const name = role.roleName.toLowerCase();
                    return name !== "user" && name !== "sysadmin";
                  })
                  .map((role) => (
                    <option key={role.roleId} value={role.roleId}>
                      {role.roleName}
                    </option>
                  ))}
                </select>
              </div>
            </div>

            <div className="btngrp">
              <button
                className="btn btn-secondary"
                type="button"
                onClick={() => navigate(-1)}
              >
                Cancel
              </button>
              <button className="btn btn-primary" type="submit">
                Create Role
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default NewRole;
