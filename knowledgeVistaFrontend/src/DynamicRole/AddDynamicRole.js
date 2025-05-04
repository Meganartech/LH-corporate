import React, { useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import PhoneInput from "react-phone-number-input";
import "react-phone-number-input/style.css";
import profile from "../assets/default-profile.png"; // update if needed

const AddDynamicRole = ({ displayname }) => {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    base64Image: "",
    username: "",
    email: "",
    psw: "",
    confirm_password: "",
    dob: "",
    skills: "",
  });

  const [errors, setErrors] = useState({});
  const [phoneNumber, setPhoneNumber] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const defaultCountry = "US"; // or your preferred default

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setFormData((prev) => ({ ...prev, base64Image: reader.result }));
      };
      reader.readAsDataURL(file);
    }
  };

  const togglePasswordVisibility = () => setShowPassword((prev) => !prev);
  const toggleConfirmPasswordVisibility = () =>
    setShowConfirmPassword((prev) => !prev);

  const handlePhoneChange = (value) => setPhoneNumber(value);
  const fetchCountryDialingCode = () => {}; // placeholder if needed

  const handleSubmit = (e) => {
    e.preventDefault();
    // Add your validation and submission logic here
    console.log("Form submitted", formData, phoneNumber);
  };

  return (
    <div>
      <div className="page-header"></div>
      <div className="card">
        <div className="card-body">
          <div className="row">
            <div className="col-12">
              <div className="navigateheaders">
                <div onClick={() => navigate(-1)}>
                  <i className="fa-solid fa-arrow-left" title="Back"></i>
                </div>
                <div></div>
                <div onClick={() => navigate(-1)}>
                  <i className="fa-solid fa-xmark" title="Close"></i>
                </div>
              </div>

              <div className="innerFrame">
                <h2 style={{ textDecoration: "underline" }}>
                  Add{" "}
                  {displayname && displayname.role_name
                    ? displayname.role_name
                    : "Role"}
                </h2>

                <form className="mainform" onSubmit={handleSubmit}>
                  <div className="profile-picture">
                    <div className="image-group">
                      <img
                        src={
                          formData.base64Image
                            ? formData.base64Image
                            : profile
                        }
                        alt="Profile"
                        className={
                          formData.base64Image ? "profile-picture" : "prof"
                        }
                      />
                    </div>
                    <div style={{ textAlign: "center" }}>
                      <label htmlFor="fileInput" className="file-upload-btn">
                        Upload
                      </label>
                      <input
                        type="file"
                        name="fileInput"
                        style={{ display: "none" }}
                        id="fileInput"
                        accept="image/*"
                        onChange={handleFileChange}
                      />
                      <div className="text-danger">{errors.profile}</div>
                    </div>
                  </div>

                  <div className="form-group row">
                    <label htmlFor="username" className="col-sm-3 col-form-label">
                      Name
                    </label>
                    <div className="col-sm-9">
                      <input
                        type="text"
                        name="username"
                        value={formData.username}
                        onChange={handleChange}
                        className={`form-control mt-1 ${
                          errors.username && "is-invalid"
                        }`}
                        placeholder="Full Name"
                        required
                      />
                      <div className="invalid-feedback">{errors.username}</div>
                    </div>
                  </div>

                  <div className="form-group row">
                    <label htmlFor="email" className="col-sm-3 col-form-label">
                      Email<span className="text-danger">*</span>
                    </label>
                    <div className="col-sm-9">
                      <input
                        type="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        className={`form-control ${
                          errors.email && "is-invalid"
                        }`}
                        placeholder="Email Address"
                        required
                      />
                      <div className="invalid-feedback">{errors.email}</div>
                    </div>
                  </div>

                  <div className="form-group row">
                    <label htmlFor="psw" className="col-sm-3 col-form-label">
                      Password<span className="text-danger">*</span>
                    </label>
                    <div className="col-sm-9">
                      <div
                        className={`inputpsw form-control p-1 ${
                          errors.psw && "is-invalid"
                        }`}
                      >
                        <input
                          type={showPassword ? "text" : "password"}
                          name="psw"
                          value={formData.psw}
                          onChange={handleChange}
                          placeholder="Password"
                          style={{ outline: "none" }}
                          required
                        />
                        <i
                          className={`fa ${
                            showPassword ? "fa-eye-slash" : "fa-eye"
                          }`}
                          onClick={togglePasswordVisibility}
                          style={{ display: "flex", alignItems: "center" }}
                        ></i>
                      </div>
                      <div className="invalid-feedback">{errors.psw}</div>
                    </div>
                  </div>

                  <div className="form-group row">
                    <label
                      htmlFor="confirm_password"
                      className="col-sm-3 col-form-label"
                    >
                      Re-type Password<span className="text-danger">*</span>
                    </label>
                    <div className="col-sm-9">
                      <div
                        className={`inputpsw form-control p-1 ${
                          errors.confirm_password && "is-invalid"
                        }`}
                      >
                        <input
                          type={showConfirmPassword ? "text" : "password"}
                          name="confirm_password"
                          value={formData.confirm_password}
                          onChange={handleChange}
                          placeholder="Repeat Password"
                          style={{ outline: "none" }}
                          required
                        />
                        <i
                          className={`fa ${
                            showConfirmPassword ? "fa-eye-slash" : "fa-eye"
                          }`}
                          onClick={toggleConfirmPasswordVisibility}
                          style={{ display: "flex", alignItems: "center" }}
                        ></i>
                      </div>
                      <div className="invalid-feedback">
                        {errors.confirm_password}
                      </div>
                    </div>
                  </div>

                  <div className="form-group row">
                    <label htmlFor="phone" className="col-sm-3 col-form-label">
                      Phone<span className="text-danger">*</span>
                    </label>
                    <div className="col-sm-9">
                      <PhoneInput
                        placeholder="Enter phone number"
                        value={phoneNumber}
                        onChange={handlePhoneChange}
                        defaultCountry={defaultCountry}
                        international
                        countryCallingCodeEditable={true}
                        onCountryChange={fetchCountryDialingCode}
                        className={`form-control ${
                          errors.phone && "is-invalid"
                        }`}
                      />
                      <div className="invalid-feedback">{errors.phone}</div>
                    </div>
                  </div>

                  <div className="form-group row">
                    <label htmlFor="dob" className="col-sm-3 col-form-label">
                      Date of Birth
                    </label>
                    <div className="col-sm-9">
                      <input
                        type="date"
                        name="dob"
                        value={formData.dob}
                        onChange={handleChange}
                        className={`form-control ${
                          errors.dob && "is-invalid"
                        }`}
                        required
                      />
                      <div className="invalid-feedback">{errors.dob}</div>
                    </div>
                  </div>

                  <div className="form-group row">
                    <label htmlFor="skills" className="col-sm-3 col-form-label">
                      Skills
                    </label>
                    <div className="col-sm-9">
                      <input
                        type="text"
                        name="skills"
                        value={formData.skills}
                        onChange={handleChange}
                        className={`form-control mt-1 ${
                          errors.skills && "is-invalid"
                        }`}
                        placeholder="Skills"
                        required
                      />
                      <div className="invalid-feedback">{errors.skills}</div>
                    </div>
                  </div>

                  <div className="btngrp">
                    <button type="submit" className="btn btn-primary">
                      Add
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AddDynamicRole;
