/* EditDynamicRole.js */
import React, { useContext, useEffect, useRef, useState } from "react";
import { useNavigate, useParams, useLocation } from "react-router-dom";
import axios from "axios";
import PhoneInput, { parsePhoneNumber } from "react-phone-number-input";
import { isValidPhoneNumber } from "react-phone-number-input";
import "react-phone-number-input/style.css";
import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";

import baseUrl from "../api/utils";
import { GlobalStateContext } from "../Context/GlobalStateProvider";

import profile from "../images/profile.png";
import errorimg from "../images/errorimg.png";

const MySwal = withReactContent(Swal);

export default function EditDynamicRole() {
  const { role, email } = useParams();
  const navigate       = useNavigate();
  const location       = useLocation();
  const token          = sessionStorage.getItem("token");
  const { displayname }= useContext(GlobalStateContext);

  const roleLabel =
    displayname?.[`${role?.toLowerCase()}_name`] ??
    role?.charAt(0).toUpperCase() + role?.slice(1) ?? "User";

  /* ------------ state ------------ */
  const [notFound,setNotFound]=useState(false);
  const [formData,setFormData]=useState({
    username:"",base64Image:null,email:"",dob:"",
    skills:"",phone:"",profile:null,countryCode:"+91",isActive:true,
  });
  const [errors,setErrors]=useState({});
  const [defaultCountry,setDefaultCountry]=useState("IN");
  const [phoneNumber,setPhoneNumber]=useState("");

  /* refs */
  const nameRef=useRef(null),emailRef=useRef(null),dobRef=useRef(null),
        skillsRef=useRef(null),phoneRef=useRef(null);

  /* ------------ helpers ------------ */
  const scrollToError=()=>{
    if(errors.username) nameRef.current?.scrollIntoView({behavior:"smooth"});
    else if(errors.email) emailRef.current?.scrollIntoView({behavior:"smooth"});
    else if(errors.dob) dobRef.current?.scrollIntoView({behavior:"smooth"});
    else if(errors.skills) skillsRef.current?.scrollIntoView({behavior:"smooth"});
    else if(errors.phone) phoneRef.current?.scrollIntoView({behavior:"smooth"});
  };
  useEffect(scrollToError,[errors]);

  const convertImageToBase64=file=>new Promise((res,rej)=>{const r=new FileReader();r.onload=()=>res(r.result);r.onerror=rej;r.readAsDataURL(file)});

  /* ------------ fetch data ------------ */
  useEffect(()=>{
    (async()=>{
      try{
        const res=await axios.get(`${baseUrl}/get/${roleLabel}/${email}`,{headers:{Authorization:token}});
        const u=res.data;
        setFormData(p=>({...p,...u}));
        setPhoneNumber(`${u.countryCode??""}${u.phone??""}`);
        if(u.profile){
          setFormData(p=>({...p,base64Image:`data:image/jpeg;base64,${u.profile}`}));
        }
      }catch(err){
        if(err.response?.status===404) setNotFound(true);
        else if(err.response?.status===401) navigate("/unauthorized");
        else console.error(err);
      }
    })();
  },[roleLabel,email,token,navigate]);

  /* ------------ handlers ------------ */
  const handlePhoneChange=val=>{
    if(typeof val!=="string") return;
    setPhoneNumber(val);
    const pn=parsePhoneNumber(val);
    if(pn) setFormData(p=>({...p,phone:pn.nationalNumber}));
    setErrors(e=>({...e,phone:val&&isValidPhoneNumber(val)?"":"Enter a valid Phone number"}));
  };

  const fetchCountryDialingCode=async cc=>{
    try{
      const res=await fetch(`https://restcountries.com/v3.1/alpha/${cc}`);
      const data=await res.json();
      const dialing=data[0]?.idd?.root+(data[0]?.idd?.suffixes?.[0]||"")||"+91";
      setFormData(p=>({...p,countryCode:dialing}));
      setDefaultCountry(cc);
    }catch(e){console.error(e);}
  };

  const handleChange=({target:{name,value}})=>{
    let err="";
    switch(name){
      case"email":err=/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)?"":"Please enter a valid email address";break;
      case"dob":{
        const d=new Date(value),t=new Date();
        const max=new Date(t.getFullYear()-8,t.getMonth(),t.getDate());
        const min=new Date(t.getFullYear()-100,t.getMonth(),t.getDate());
        err=d<=max&&d>=min?"":"Please enter a valid date of birth";
      }break;
      case"phone":
        err=value.length<10?"Phone number must be at least 10 digits":
            value.length>15?"Phone number cannot be longer than 15 digits":
            /^\d+$/.test(value)?"":"Please enter a valid phone number (digits only)";
      break;
      default:break;
    }
    setErrors(e=>({...e,[name]:err}));
    setFormData(p=>({...p,[name]:value}));
  };

  const handleFileChange=async e=>{
    const file=e.target.files[0];
    if(file && file.size>50*1024) return setErrors(e=>({...e,profile:"Image size must be 50 KB or smaller"}));
    setErrors(e=>({...e,profile:""}));
    const b64=await convertImageToBase64(file);
    setFormData(p=>({...p,profile:file,base64Image:b64}));
  };

  const handleSubmit=async e=>{
    e.preventDefault();
    const required=["email","phone","countryCode"];
    const missing=required.filter(f=>!formData[f]);
    if(missing.length||Object.values(errors).some(Boolean)){
      missing.forEach(f=>setErrors(er=>({...er,[f]:"This field is required"})));
      scrollToError();return;
    }
    const fd=new FormData(); Object.entries(formData).forEach(([k,v])=>fd.append(k,v??""));
    try{
      const res=await axios.patch(`${baseUrl}/Edit/${roleLabel}/${email}`,fd,{headers:{Authorization:token}});
      if(res.status===200){
        await MySwal.fire("Updated!",`${roleLabel} information updated successfully!`,"success");
        navigate(`/view/${roleLabel}s`);
      }
    }catch(err){
      if(err.response?.status===400) setErrors(e=>({...e,email:"This email is already registered."}));
      else if(err.response?.status===401) MySwal.fire("Unauthorized","You are unable to update","error");
      else console.error(err);
    }
  };

  /* ------------ render ------------ */
  return(
    <div>
      {/* breadcrumb header */}
      <div className="page-header">
        <div className="page-block">
          <div className="row align-items-center">
            <div className="col-md-12">
              <div className="page-header-title"><h5 className="m-b-10">Settings</h5></div>
              <ul className="breadcrumb">
                <li className="breadcrumb-item">
                  <a href="#" onClick={()=>navigate("/admin/dashboard")}><i className="feather icon-home"/></a>
                </li>
                <li className="breadcrumb-item">
                  <a href="#" onClick={()=>navigate(`/view/${roleLabel}s`)}>{roleLabel} Details</a>
                </li>
                <li className="breadcrumb-item"><a href="#">{email}</a></li>
              </ul>
            </div>
          </div>
        </div>
      </div>

      {/* card */}
      <div className="card">
        <div className="card-body">
          <div className="row"><div className="col-12">
            <div className="navigateheaders">
              <div onClick={()=>navigate(-1)}><i className="fa-solid fa-arrow-left"/></div>
              <div/>
              <div onClick={()=>navigate(`/view/${roleLabel}s`)}><i className="fa-solid fa-xmark"/></div>
            </div>

            {notFound?(
              <div className="centerflex">
                <div className="enroll">
                  <h2>No {roleLabel} Found for this email.</h2>
                  <button className="btn btn-primary" onClick={()=>navigate(-1)}>Go Back</button>
                </div>
              </div>
            ):(
              <div className="innerFrame">
                <h4>Edit {roleLabel}</h4>

                <div className="mainform">
                  {/* avatar */}
                  <div className="profile-picture">
                    <div className="image-group">
                      <img
                        src={formData.base64Image||profile}
                        onError={e=>e.target.src=errorimg}
                        alt="Selected"
                        className="profile-picture"
                      />
                    </div>
                    <div style={{textAlign:"center"}}>
                      <label htmlFor="fileInput" className="file-upload-btn">Upload</label>
                      <div className="text-danger">{errors.profile}</div>
                      <input
                        type="file" id="fileInput" style={{display:"none"}}
                        accept="image/*" onChange={handleFileChange}
                      />
                    </div>
                  </div>

                  {/* form fields column */}
                  <div>
                    {/* name */}
                    <div className="form-group row" ref={nameRef}>
                      <label className="col-sm-3 col-form-label">Name</label>
                      <div className="col-sm-9">
                        <input
                          type="text" name="username" value={formData.username}
                          onChange={handleChange} placeholder="Full Name" autoFocus
                          className={`form-control mt-1 ${errors.username&&"is-invalid"}`}
                        />
                        <div className="invalid-feedback">{errors.username}</div>
                      </div>
                    </div>

                    {/* email */}
                    <div className="form-group row" ref={emailRef}>
                      <label className="col-sm-3 col-form-label">Email<span className="text-danger">*</span></label>
                      <div className="col-sm-9">
                        <input
                          type="email" name="email" value={formData.email}
                          onChange={handleChange} placeholder="Email Address"
                          className={`form-control ${errors.email&&"is-invalid"}`}
                        />
                        <div className="invalid-feedback">{errors.email}</div>
                      </div>
                    </div>

                    {/* phone */}
                    <div className="form-group row" ref={phoneRef}>
                      <label className="col-sm-3 col-form-label">Phone<span className="text-danger">*</span></label>
                      <div className="col-sm-9">
                        <div className="phone-input-container inputlikeeffect">
                          <PhoneInput
                            placeholder="Enter phone number" id="phone"
                            value={phoneNumber} onChange={handlePhoneChange}
                            defaultCountry={defaultCountry} international
                            className={`form-control ${errors.phone&&"is-invalid"}`}
                            countryCallingCodeEditable onCountryChange={fetchCountryDialingCode}
                          />
                          <div className="invalid-feedback">{errors.phone}</div>
                        </div>
                      </div>
                    </div>

                    {/* dob */}
                    <div className="form-group row" ref={dobRef}>
                      <label className="col-sm-3 col-form-label">Date of Birth</label>
                      <div className="col-sm-9">
                        <input
                          type="date" name="dob" value={formData.dob}
                          onChange={handleChange}
                          className={`form-control ${errors.dob&&"is-invalid"}`}
                        />
                        <div className="invalid-feedback">{errors.dob}</div>
                      </div>
                    </div>

                    {/* skills */}
                    <div className="form-group row" ref={skillsRef}>
                      <label className="col-sm-3 col-form-label">Skills</label>
                      <div className="col-sm-9">
                        <input
                          type="text" name="skills" value={formData.skills}
                          onChange={handleChange} placeholder="Skills"
                          className={`form-control ${errors.skills&&"is-invalid"}`}
                        />
                        <div className="invalid-feedback">{errors.skills}</div>
                      </div>
                    </div>
                  </div>
                </div>
                <div className="btngrp">
                  <button className="btn btn-primary" onClick={handleSubmit}>Save</button>
                </div>
              </div>
            )}
          </div></div>
        </div>
      </div>
    </div>
  );
}
