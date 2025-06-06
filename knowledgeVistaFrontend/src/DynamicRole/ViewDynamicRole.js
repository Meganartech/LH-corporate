import React, { useContext, useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";
import axios from "axios";
import baseUrl from "../api/utils";
import { GlobalStateContext } from "../Context/GlobalStateProvider";

const ViewDynamicRole = () => {
  const navigate = useNavigate();
  const { roleName } = useParams();
  const MySwal = withReactContent(Swal);
  const token = sessionStorage.getItem("token");
  const { displayname } = useContext(GlobalStateContext);
  
  // Get display name for the role or fallback to roleName
  const roleDisplay = displayname?.[`${roleName.toLowerCase()}_name`] || roleName;
  
  // State variables
  const [users, setUsers] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [filterOption, setFilterOption] = useState("All");
  const [searchQuery, setSearchQuery] = useState("");
  const itemsperpage = 10;
  const [datacounts, setdatacounts] = useState({ start: "", end: "", total: "" });
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [dob, setDob] = useState("");
  const [skills, setSkills] = useState("");
  const [fullsearch, setfullsearch] = useState(false);
  const [loading, setLoading] = useState(false);

  // Fetch users based on search criteria or pagination
  const fetchUsers = async (page = currentPage) => {
    setLoading(true);
    try {
      let response;
      
      if (username || email || phone || dob || skills) {
        // Search API call
        response = await axios.get(`${baseUrl}/Institution/search/${roleName}`, {
          headers: { Authorization: token },
          params: {
            username: encodeURIComponent(username),
            email: encodeURIComponent(email),
            phone: encodeURIComponent(phone),
            dob: encodeURIComponent(dob),
            skills: encodeURIComponent(skills),
            page: page,
            size: itemsperpage,
          },
        });
      } else {
        // Regular list API call
        response = await axios.get(`${baseUrl}/view/${roleName.charAt(0).toUpperCase() + roleName.slice(1).toLowerCase()}`, {
          headers: { Authorization: token },
          params: { pageNumber: page, pageSize: itemsperpage },
        });
      }
      //console.log("payload", response.data);
      setUsers(response.data.content);
      setTotalPages(response.data.totalPages);
      setdatacounts({
        start: page * itemsperpage + 1,
        end: page * itemsperpage + itemsperpage,
        total: response.data.totalElements,
      });
    } catch (error) {
      if (error.response?.status === 401) {
        navigate("/unauthorized");
      } else {
        console.error("Error fetching users:", error);
        MySwal.fire("Error", `Failed to fetch ${roleDisplay}s`, "error");
      }
    } finally {
      setLoading(false);
    }
  };

  // Handle search input changes
  const handleChange = (e) => {
    const { name, value } = e.target;
    if (value === "") fetchUsers(0); // Reset to first page when clearing search
    
    switch (name) {
      case "username": setUsername(value); break;
      case "email": setEmail(value); break;
      case "phone": setPhone(value); break;
      case "dob": setDob(value); break;
      case "skills": setSkills(value); break;
      default: break;
    }
  };

  // Filter users based on active/inactive status
  const filterData = () => {
    if (filterOption === "All") return users;
    return users.filter(user => user.isActive === (filterOption === "Active"));
  };

  // Handle pagination
  const handlePageChange = (newPage) => {
    setCurrentPage(newPage);
  };

  // Render pagination buttons
  const renderPaginationButtons = () => {
    return [...Array(totalPages)].map((_, i) => (
      <li className={`page-item ${i === currentPage ? "active" : ""}`} key={i}>
        <a className="page-link" href="#" onClick={() => handlePageChange(i)}>
          {i + 1}
        </a>
      </li>
    ));
  };

  // Handle deactivation
  const handleDeactivate = async (userId, username, email) => {
    const formData = new FormData();
    formData.append("email", email);

    MySwal.fire({
      title: "Deactivate?",
      text: `Are you sure you want to deactivate ${roleDisplay} ${username}`,
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#d33",
      confirmButtonText: "Deactivate",
      cancelButtonText: "Cancel",
      input: "text",
      inputValue: "",
      inputAttributes: {
        autocapitalize: "off",
        placeholder: "Enter reason for deactivation (required)",
        //required: true,
      },
      preConfirm: (reason) => {
        return new Promise((resolve, reject) => {
          if (!reason) {
            Swal.showValidationMessage("Please enter a reason for deactivation.");
            reject();
          } else {
            formData.append("reason", reason);
            resolve();
          }
        });
      },
    }).then(async (result) => {
      if (result.isConfirmed) {
        try {
          const response = await axios.delete(
            `${baseUrl}/admin/deactivate/${roleName.charAt(0).toUpperCase() + roleName.slice(1).toLowerCase()}`,
            {
              data: formData,
              headers: { Authorization: token },
            }
          );

          if (response.status === 200) {
            MySwal.fire(
              "Deactivated",
              `${roleDisplay} ${username} deactivated successfully`,
              "success"
            ).then(() => fetchUsers(currentPage));
          }
        } catch (error) {
          MySwal.fire(
            "Error",
            error.response?.status === 404 
              ? `${roleDisplay} not found` 
              : `Error deactivating ${roleDisplay}`,
            "error"
          );
        }
      }
    });
  };

  // Handle activation
  const handleActivate = async (userId, username, email) => {
    const formData = new FormData();
    formData.append("email", email);

    MySwal.fire({
      title: "Activate?",
      text: `Are you sure you want to activate ${roleDisplay} ${username}`,
      icon: "question",
      showCancelButton: true,
      confirmButtonColor: "#28a745",
      confirmButtonText: "Activate",
      cancelButtonText: "Cancel",
    }).then(async (result) => {
      if (result.isConfirmed) {
        try {
          const response = await axios.post(
            `${baseUrl}/admin/activate/${roleName.toLowerCase()}`,
            formData,
            { headers: { Authorization: token } }
          );

          if (response.status === 200) {
            MySwal.fire(
              "Activated",
              `${roleDisplay} ${username} activated successfully`,
              "success"
            ).then(() => fetchUsers(currentPage));
          }
        } catch (error) {
          MySwal.fire(
            "Error",
            error.response?.status === 404 
              ? `${roleDisplay} not found` 
              : `Error activating ${roleDisplay}`,
            "error"
          );
        }
      }
    });
  };

  // Fetch data on component mount and when dependencies change
  useEffect(() => {
    fetchUsers();
  }, [currentPage, username, email, phone, dob, skills, filterOption]);

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
                  <a href="#">{roleDisplay.charAt(0).toUpperCase().slice(1).toLowerCase()} Details</a>
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
              
              <div className="tableheader">
                <h4>{roleDisplay.charAt(0).toUpperCase()+roleDisplay.slice(1).toLowerCase()} Details</h4>
                <div className="selectandadd">
                  <select
                    className="selectstyle btn btn-success text-left"
                    value={filterOption}
                    onChange={(e) => setFilterOption(e.target.value)}
                  >
                    <option className="bg-light text-dark" value="All">All</option>
                    <option className="bg-light text-dark" value="Active">Active</option>
                    <option className="bg-light text-dark" value="Inactive">Inactive</option>
                  </select>
                  <a
                    href="#"
                    className="btn btn-primary mybtn"
                    onClick={(e) => {
                      e.preventDefault();
                      navigate(`/add/${roleName}`);
                    }}
                  >
                    <i className="fa-solid fa-plus"></i> Add {roleDisplay}
                  </a>
                </div>
              </div>
            </div>

            <div className="card-body">
              {loading ? (
                <div className="text-center my-4">
                  <div className="spinner-border text-primary" role="status">
                    <span className="visually-hidden">Loading...</span>
                  </div>
                </div>
              ) : (
                <div className="table-container">
                  <table className="table table-hover table-bordered table-sm">
                    <thead className="thead-dark">
                      <tr>
                        <th scope="col">
                          <i
                            onClick={() => setfullsearch(!fullsearch)}
                            className={
                              fullsearch
                                ? "fa-solid fa-xmark"
                                : "fa-solid fa-magnifying-glass"
                            }
                          ></i>
                        </th>
                        <th scope="col">Username</th>
                        <th scope="col">Email</th>
                        <th scope="col">Phone</th>
                        <th scope="col">Skills</th>
                        <th scope="col">Date of Birth</th>
                        <th scope="col">Status</th>
                        <th colSpan="4" scope="col">Action</th>
                      </tr>
                      {fullsearch && (
                        <tr>
                          <td></td>
                          <td>
                            <input
                              type="search"
                              name="username"
                              value={username}
                              onChange={handleChange}
                              placeholder="Search Username"
                            />
                          </td>
                          <td>
                            <input
                              type="search"
                              name="email"
                              value={email}
                              onChange={handleChange}
                              placeholder="Search Email"
                            />
                          </td>
                          <td>
                            <input
                              type="search"
                              name="phone"
                              value={phone}
                              onChange={handleChange}
                              placeholder="Search Phone"
                            />
                          </td>
                          <td>
                            <input
                              type="search"
                              name="skills"
                              value={skills}
                              onChange={handleChange}
                              placeholder="Search Skills"
                            />
                          </td>
                          <td colSpan="3"></td>
                        </tr>
                      )}
                    </thead>
                    <tbody>
                      {filterData().length > 0 ? (
                        filterData().map((user, index) => (
                          <tr key={user.userId}>
                            <th scope="row">{currentPage * itemsperpage + index + 1}</th>
                            <td className="py-2">{user.username}</td>
                            <td className="py-2">
                              <Link
                                to={`/view/${roleName}/profile/${user.email}`}
                                state={{ user }}
                              >
                                {user.email}
                              </Link>
                            </td>
                            <td className="py-2">{user.phone}</td>
                            <td className="py-2">{user.skills}</td>
                            <td className="py-2">{user.dob}</td>
                            <td className="py-2">
                              {user.isActive ? (
                                <div className="Activeuser">
                                  <i className="fa-solid fa-circle pr-3"></i>Active
                                </div>
                              ) : (
                                <div className="InActiveuser">
                                  <i className="fa-solid fa-circle pr-3"></i>Inactive
                                </div>
                              )}
                            </td>
                            <td className="text-center">
                              <Link
                                to={`/${roleName.toLowerCase()}/edit/${user.email}`}
                                state={{ user }}
                                className="hidebtn"
                              >
                                <i className="fas fa-edit"></i>
                              </Link>
                            </td>
                            <td className="text-center">
                              <Link
                                to={`/assignCourse/${user.userId}`}
                                className="hidebtn"
                              >
                                <i className="fas fa-plus"></i>
                              </Link>
                            </td>
                            <td className="text-center">
                              {user.isActive ? (
                                <button
                                  className="hidebtn"
                                  onClick={() =>
                                    handleDeactivate(user.userId, user.username, user.email)
                                  }
                                >
                                  <i className="fa-solid fa-lock"></i>
                                </button>
                              ) : (
                                <button
                                  className="hidebtn"
                                  onClick={() =>
                                    handleActivate(user.userId, user.username, user.email)
                                  }
                                >
                                  <i className="fa-solid fa-lock-open"></i>
                                </button>
                              )}
                            </td>
                            <td>
                              <Link
                                to={`/view/Attendance`}
                                state={{ user }}
                                className="hidebtn"
                                title="Attendance"
                              >
                                <i className="fa-solid fa-clipboard-user"></i>
                              </Link>
                            </td>
                          </tr>
                        ))
                      ) : (
                        <tr>
                          <td colSpan="11" className="text-center py-4">
                            No {roleDisplay.toLowerCase()}s found
                          </td>
                        </tr>
                      )}
                    </tbody>
                  </table>
                </div>
              )}

              <div className="cornerbtn">
                <ul className="pagination">
                  <li className={`page-item ${currentPage === 0 ? "disabled" : ""}`}>
                    <a
                      className="page-link"
                      href="#"
                      aria-label="Previous"
                      onClick={() => handlePageChange(currentPage - 1)}
                    >
                      <span aria-hidden="true">«</span>
                      <span className="sr-only">Previous</span>
                    </a>
                  </li>
                  {renderPaginationButtons()}
                  <li className={`page-item ${currentPage === totalPages - 1 ? "disabled" : ""}`}>
                    <a
                      className="page-link"
                      href="#"
                      aria-label="Next"
                      onClick={() => handlePageChange(currentPage + 1)}
                    >
                      <span aria-hidden="true">»</span>
                      <span className="sr-only">Next</span>
                    </a>
                  </li>
                </ul>
                <div>
                  <label className="text-primary">
                    ({datacounts.start}-{Math.min(datacounts.end, datacounts.total)}) of {datacounts.total}
                  </label>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
   
  );
};

export default ViewDynamicRole;