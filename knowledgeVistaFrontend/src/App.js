import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import { useState, useEffect } from "react";
import ForgetPassword from "./AuthenticationPages/forgetpassword";
import Login from "./AuthenticationPages/login";
import React from "react";
import PrivateRoute from "./AuthenticationPages/PrivateRoute";
import Missing from "./AuthenticationPages/Missing";
import Unauthorized from "./AuthenticationPages/Unauthorized";
import AttenTest from "./course/Test/AttenTest";
import CertificateInputs from "./certificate/CertificateInputs";
import Template from "./certificate/Template";
import EditCourse from "./course/Update/EditCourse";
import CourseView from "./course/Components/CourseView";
import Mystudents from "./Trainer/Mystudents.js"
import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";
import EditCourseForm from "./course/Update/EditCourseForm.js";
import Layout from "./Common Components/Layout.js";
import CreateTest from "./course/Test/CreateTest";
import TestList from "./course/Test/TestList";
import ViewStudentList from "./Student/ViewStudentList";
import AssignCourse from "./Student/AssignCourse.js";
import Mycourse from "./Student/Mycourse";
import MyCertificateList from "./certificate/MyCertificateList";
import AddTrainer from "./Trainer/AddTrainer";
import AddStudent from "./Student/AddStudent";
import ViewTrainerList from "./Trainer/ViewTrainerList";
import UploadVideo from "./course/Components/UploadVideo";
import CourseCreation from "./course/Components/CourseCreation";
import ViewVideo from "./course/Components/ViewVideo";
import LessonList from "./course/Components/LessonList";
import TrainerProfile from "./Trainer/TrainerProfile.js";
import StudentProfile from "./Student/StudentProfile.js";
import MyAssignedcourses from "./Trainer/MyAssignedcourses.js";
import EditLesson from "./course/Update/EditLesson.js";
import AssignCourseTRAINER from "./Trainer/AssignCourseTRAINER.js";
import EditStudent from "./Student/EditStudent.js";
import EditTrainer from "./Trainer/EditTrainer.js";
import ProfileView from "./Common Components/ProfileView.js";
import CustomViewvideo from "./course/Components/CustomViewvideo.js";
import EditQuestion from "./course/Test/EditQuestion.js";
import AddMoreQuestion from "./course/Test/AddMoreQuestion.js";
import Dashboard from "./AuthenticationPages/Dashboard.js";
import About_Us from "./AuthenticationPages/About_Us";
import baseUrl from "./api/utils.js";
import axios from "axios";
import RefreshToken from "./AuthenticationPages/RefreshToken.js";
import AdminRegister from "./Registration/AdminRegister.js";
import LicenceExpired from "./AuthenticationPages/LicenceExpired.js";
import ViewAdmin from "./SysAdmin/ViewAdmin.js";
import ViewTrainers from "./SysAdmin/ViewTrainers.js";
import ViewStudents from "./SysAdmin/ViewStudents.js";
import SysadminLicenceupload from "./AuthenticationPages/SysadminLicenceupload.js";
import LicenceDetails from "./AuthenticationPages/LicenceDetails.js";
import SheduleZoomMeet from "./Meetings/SheduleZoomMeet.js";
import ZoomAccountkeys from "./Meetings/ZoomAccountkeys.js";
import ZoomKeys from "./SysAdmin/ZoomKeys.js";
import CalenderView from "./Meetings/CalenderView.js";
import StudentCalenderView from "./Meetings/StudentCalenderView.js";
import EditMeeting from "./Meetings/EditMeeting.js";
import Footer from "./Common Components/Footer.js";
import Affiliates from "./SysAdmin/Affiliates.js";
//import MailSending from "./Meetings/MailSending.js";
import SlideViewer from "./course/Components/SlideViewer.js";
import AdminProfileView from "./SysAdmin/AdminProfileView.js";
import StudentRegister from "./Registration/StudentRegister.js";
import RedirectComponent from "./RedirectComponent.js";
import TrainerRegistration from "./Registration/TrainerRegistration.js";
import ViewCourseVps from "./course/Components/ViewCourseVps.js";
import ErrorBoundary from "./ErrorBoundary.js";
import SettingsComponent from "./UserSettings/SettingsComponent.js";
import DisplayName from "./UserSettings/DisplayName.js";
import SocialLoginKeys from "./SysAdmin/SocialLoginKeys.js";
import MailSettings from "./UserSettings/MailSettings.js";
import "./assets/css/style.css";
import Approvals from "./Registration/Approvals.js";
import SocialLoginKeysAdmin from "./UserSettings/SocialLoginKeysAdmin.js";
import FooterDetails from "./UserSettings/FooterDetails.js";
import pcoded from "./assets/js/pcoded.js";
import CreateBatch from "./Batch/CreateBatch.js";
import ViewAllBatch from "./Batch/ViewAllBatch.js";
import EditBatch from "./Batch/EditBatch.js";
import ScrollToTop from "./ScrollToTop.js";
import ViewAllBAtchForCourse from "./Batch/ViewAllBAtchForCourse.js";
import CreateQuizz from "./course/Quizz/CreateQuizz.js";
import ViewQuizz from "./course/Quizz/ViewQuizz.js";
import AddMoreQuizz from "./course/Quizz/AddMoreQuizz.js";
import EditQuizz from "./course/Quizz/EditQuizz.js";
import ViewCourseOfBatch from "./Batch/ViewCourseOfBatch.js";
import SheduleQuizz from "./Batch/SheduleQuizz.js";
import ProgramCalender from "./course/Quizz/ProgramCalender.js";
import StartQuizz from "./course/Quizz/StartQuizz.js";
import WeightageSetting from "./UserSettings/WeightageSetting.js";
import Grades from "./Student/Grades.js";
import QuizzScore from "./Student/QuizzScore.js";
import TestScore from "./course/Test/TestScore.js";
import StudentsBatch from "./Batch/StudentsBatch.js";
import BatchDashboard from "./Batch/StudentBatchDashboard.js";
import Attendance from "./Attendance/Attendance.js";
import MyAttendance from "./Attendance/MyAttendance.js";
import QuizzHistorybatch from "./course/Quizz/QuizzHistorybatch.js";
import UserTestHistory from "./course/Test/UserTestHistory.js";
import UserGrades from "./Student/UserGrades.js";
import CreateModuleTest from "./course/ModuleTest.js/CreateModuleTest.js";
import ListModuleTest from "./course/ModuleTest.js/ListModuleTest.js";
import ViewModuleTest from "./course/ModuleTest.js/ViewModuleTest.js";
import AddMoreMQuestion from "./course/ModuleTest.js/AddMoreMQuestion.js";
import EditModuleQuestion from "./course/ModuleTest.js/EditModuleQuestion.js";
import SheduleModuleTest from "./course/ModuleTest.js/SheduleModuleTest.js";
import StartModuleTest from "./course/ModuleTest.js/StartModuleTest.js";
import CreateAssignment from "./Assignment/CreateAssignment.js";
import GetAssignments from "./Assignment/GetAssignments.js";
import EditAssignment from "./Assignment/EditAssignment.js";
import SheduleAssignment from "./Assignment/SheduleAssignment.js";
import ViewMyAssignment from "./Assignment/ViewMyAssignment.js";
import SubmitAssignment from "./Assignment/SubmitAssignment.js";
import BatchAssignments from "./Assignment/BatchAssignments.js";
import ValidateAssignment from "./Assignment/ValidateAssignment.js";
import LicenceFileCreation from "./AuthenticationPages/LicenceFileCreation.js";
import NewRole from "./Registration/NewRole.js";
import ViewDynamicRole from "./DynamicRole/ViewDynamicRole";
import EditDynamicRole from "./DynamicRole/EditDynamicRole.js";
import RoleRegistration from "./Registration/RoleRegisteration.js";
import RoleList from "./DynamicRole/RoleList.js";
import ManageRole from "./DynamicRole/ManageRole.js";
import AccessRequest from "./course/Components/AccessRequest.js";
import AddDynamicRole from "./DynamicRole/AddDynamicRole.js";

function App() {
  useEffect(() => {
    pcoded();
  }, []);
  const isAuthenticated = sessionStorage.getItem("token") !== null;
  const MySwal = withReactContent(Swal);
  const [searchQuery, setSearchQuery] = useState("");
  const [course, setCourse] = useState([
    {
      courseId: "",
      courseName: "",
      courseUrl: "",
      courseDescription: "",
      courseCategory: "",
      amount: "",
      courseImage: "",
      Duration: "",
      Noofseats: "",
    },
  ]);

  const handleSearchChange = (e) => {
    setSearchQuery(e.target.value);
  };
 



  const filteredCourses = course.filter((item) => {
    const name = item.courseName ? item.courseName.toLowerCase() : "";
    return name.includes(searchQuery.toLowerCase());
  });
  useEffect(() => {
    const fetchItems = async () => {
      try {
        const token = sessionStorage.getItem("token");
        const role = sessionStorage.getItem("role");
        if (token) {
          if (role !== "SYSADMIN") {
            const response = await axios.get(`${baseUrl}/course/viewAll`, {
              headers: {
                Authorization: token,
              },
            });
            const data = response.data;
            setCourse(data);
          }
        }
      } catch (error) {
        console.error(error);
        throw error;
      }
    };
    if (isAuthenticated) {
      fetchItems();
    }
  }, []);

  return (
    <Router>
      <ScrollToTop />
      <div className="App ">
        <Routes>
          <Route
            element={
              <Layout
                searchQuery={searchQuery}
                handleSearchChange={handleSearchChange}
                setSearchQuery={setSearchQuery}
              
              />
            }
          >
             <Route
              path="/newRole"
              element={
                <PrivateRoute authenticationRequired={true}>
                  <RedirectComponent vpsonly={true}>
                    <NewRole />
                  </RedirectComponent>
                </PrivateRoute>
              }
            />
            <Route
              path="/admin/dashboard"
              element={
                // <ErrorBoundary>
                <PrivateRoute
                  // onlyadmin={true}
                  authenticationRequired={true}
                  authorizationRequired={true}
                  licence={true}
                >
                  <Dashboard />
                </PrivateRoute>
                // </ErrorBoundary>
              }
            />
            <Route
              path="/lessonList/:courseName/:courseId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <LessonList />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/edit/:courseName/:courseId/:Lessontitle/:lessonId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <EditLesson />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/AddQuizz/:courseName/:courseId/:Lessontitle/:lessonId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <CreateQuizz />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/ViewQuizz/:courseName/:courseID/:lessonsName/:lessonId/:quizzName/:quizzId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <ViewQuizz />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/AddQuestionInQuizz/:courseName/:courseID/:lessonsName/:lessonId/:quizzName/:quizzId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <AddMoreQuizz />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/editQuizzQuestion/:courseName/:courseID/:lessonsName/:lessonId/:quizzName/:quizzId/:questionId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <EditQuizz />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/courses/:courseName/:courseId/"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true}>
                    <ViewVideo />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/courses/:courseName/:courseId/:current"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <CustomViewvideo />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/course/Addlesson/:courseName/:courseId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <UploadVideo />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/course/addcourse"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <CourseCreation />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />

            <Route
              path="/addTrainer"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <AddTrainer />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/addStudent"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <AddStudent />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
          <Route
            path="/add/user"
            element={
              <ErrorBoundary>
                <PrivateRoute authenticationRequired={true} onlyadmin={true}>
                    <AddDynamicRole />
                </PrivateRoute>
              </ErrorBoundary>
            }
          />
            <Route
              path="/mycourses"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true}>
                    <Mycourse />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/view/Grades/:email/:batchTitle/:batchId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <UserGrades />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
  <Route
              path="/view/Assignments/:batchTitle/:batchId/:userId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <BatchAssignments />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />

            <Route
              path="/myStudents"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    onlytrainer={true}
                  >
                    <Mystudents />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            ></Route>
            <Route
              path="/dashboard/course"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true}>
                    <CourseView filteredCourses={filteredCourses} />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/course/admin/edit"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    onlyadmin={true}
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <EditCourse filteredCourses={filteredCourses} />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/course/AddTest/:courseName/:courseId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <CreateTest />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/test/start/:courseName/:courseId"
              element={
                <ErrorBoundary>
                  <PrivateRoute onlyuser={true} authenticationRequired={true}>
                    <AttenTest />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/test/Edit/:questionId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authorizationRequired={true}
                    authenticationRequired={true}
                  >
                    <EditQuestion />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/test/AddMore/:courseName/:testId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authorizationRequired={true}
                    authenticationRequired={true}
                  >
                    <AddMoreQuestion />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/course/edit/:courseId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <EditCourseForm />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/course/testlist/:courseName/:courseId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <TestList />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/course/dashboard/profile"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true}>
                    <ProfileView />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/MyCertificateList"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true}>
                    <MyCertificateList />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/template/:activityId"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true}>
                    <Template />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/assignCourse/Student/:userId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <AssignCourse />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/assignCourse/:userId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <AssignCourse />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/assignCourse/Trainer/:userId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    onlyadmin={true}
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <AssignCourseTRAINER />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/view/Students"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <ViewStudentList />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/view/Attendance/:paramBatchId?"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <Attendance />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />

            <Route
              path="/view/MyAttendance"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true} onlyuser={true}>
                    <MyAttendance />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/view/MyQuizzScore"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true} onlyuser={true}>
                    <QuizzScore />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/view/MyAssignments"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    onlyuser={true}
                  >
                    <ViewMyAssignment />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/submitAssignment/:batchId/:AssignmentId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    onlyuser={true}
                  >
                    <SubmitAssignment />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/view/MyTestScore"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true} onlyuser={true}>
                    <TestScore />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/view/TestScore/:email/:batchName/:batchId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <UserTestHistory />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            {/* ModuleTest apis */}
            <Route
              path="/course/AddModuleTest/:courseName/:courseId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <CreateModuleTest />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/course/moduleTest/:courseName/:courseId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <ListModuleTest />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/moduleTest/AddmoreQuestion/:courseName/:courseId/:mtestName/:mtestId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <AddMoreMQuestion />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/moduleTest/EditQuestion/:courseName/:courseId/:mtestName/:mtestId/:questionId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <EditModuleQuestion />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/ModuleTest/:mtestName/:mtestId/:batchName/:batchId"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true} onlyuser={true}>
                    <StartModuleTest />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/view/ModuleTest/:courseName/:courseId/:mtestname/:mtestId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <ViewModuleTest />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
          <Route
            path="/view/users"
            element={
              <ErrorBoundary>
                <PrivateRoute authenticationRequired={true} onlyadmin={true}>
                    <ViewDynamicRole/>
                </PrivateRoute>
              </ErrorBoundary>
            }
          />            
            <Route
              path="/view/Trainer"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <ViewTrainerList />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/view/Approvals"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <Approvals />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            
            <Route
              path="/certificate"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    onlyadmin={true}
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <CertificateInputs />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/view/Trainer/profile/:traineremail"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <TrainerProfile />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/view/Student/Dashboard/:studentemail/:userId/:batchId/:batchTitle"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <BatchDashboard />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/view/Student/profile/:studentemail"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <StudentProfile />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/student/edit/:email"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <EditStudent />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/trainer/edit/:email"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <EditTrainer />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
             <Route
              path="/:role/edit/:email"           /*  ← :role is the dynamic part  */
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <EditDynamicRole />           {/* you can still branch inside this
                                                    component on the role param */}
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/AssignedCourses"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    onlytrainer={true}
                    authenticationRequired={true}
                  >
                    <MyAssignedcourses />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/about"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    onlyadmin={true}
                    authenticationRequired={true}
                    authorizationRequired={true}
                    licence={true}
                  >
                    <About_Us />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
          
            <Route
              path="/view/AccessRequest"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    onlyadmin={true}
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <AccessRequest />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />

            <Route
              path="/licenceDetails"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    onlyadmin={true}
                    authorizationRequired={true}
                    licence={true}
                  >
                    <LicenceDetails />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/meeting/Shedule"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <SheduleZoomMeet />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/zoom/settings"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                    onlyadmin={true}
                  >
                    <ZoomAccountkeys />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/meeting/calender"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <CalenderView />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/meet/edit/:meetingId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <EditMeeting />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/user/meeting/calender"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true} onlyuser={true}>
                    <StudentCalenderView />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/user/ProgramCalender"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true} onlyuser={true}>
                    <ProgramCalender />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/Quizz/:quizzName/:quizzId/:batchName/:batchId"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true} onlyuser={true}>
                    <StartQuizz />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/Quizz/gethistory/:email/:batchName/:batchId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <QuizzHistorybatch />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            {/* <Route
              path="/mailSending"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <MailSending />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            /> */}
            <Route
              path="/settings/Weightage"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true} onlyadmin={true}>
                    <WeightageSetting />
                  </PrivateRoute>{" "}
                </ErrorBoundary>
              }
            />
            <Route
              path="/settings/mailSettings"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true} onlyadmin={true}>
                    <MailSettings />
                  </PrivateRoute>{" "}
                </ErrorBoundary>
              }
            />
            <Route
              path="/settings/socialLogins"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true} onlyadmin={true}>
                    <SocialLoginKeysAdmin />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/viewDocument/:documentPath/:lessonId/:docid"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true}>
                    <SlideViewer />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/settings/footer"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true} onlyadmin={true}>
                    <FooterDetails />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/settings/displayname"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                    onlyadmin={true}
                  >
                    <DisplayName />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/settings/viewsettings"
              element={
                <ErrorBoundary>
                  <PrivateRoute authorizationRequired={true} onlyadmin={true}>
                    <SettingsComponent />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
           
            <Route
              path="/batch/addNew"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authorizationRequired={true}
                    authenticationRequired={true}
                  >
                    <CreateBatch />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/batch/viewcourse/:batchTitle/:batchid"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <ViewCourseOfBatch />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/batch/ViewStudents/:batchTitle/:batchid"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <StudentsBatch />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/sheduleQuizz/:batchTitle/:batchId/:courseName/:courseId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <SheduleQuizz />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/sheduleModuleTest/:batchTitle/:batchId/:courseName/:courseId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <SheduleModuleTest />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/batch/viewall"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <ViewAllBatch />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/batch/viewall/:courseId"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true} onlyuser={true}>
                    <ViewAllBAtchForCourse />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/batch/Edit/:id"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <EditBatch />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />

            <Route
              path="/Assignment/create/:courseName/:courseId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <CreateAssignment />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
              <Route
              path="/Assignment/Validate/:batchName/:batchId/:userId/:assignmentId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <ValidateAssignment />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/Assignment/getAll/:courseName/:courseId?"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <GetAssignments />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/Assignment/get/:courseName/:courseId/:assignmentId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <EditAssignment />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/sheduleAssignment/:batchTitle/:batchId/:courseName/:courseId"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <SheduleAssignment />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />

             <Route
              path="/role/ViewAll"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <RoleList />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            
             <Route
              path="/manage/role"
              element={
                <ErrorBoundary>
                  <PrivateRoute
                    authenticationRequired={true}
                    authorizationRequired={true}
                  >
                    <ManageRole />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />



            {/* SysAdminRoutes */}
            <Route
              path="/viewAll/Admins"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true} sysadmin={true}>
                    <ViewAdmin />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/viewAll/Trainers"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true} sysadmin={true}>
                    <ViewTrainers />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/viewAll/Students"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true} sysadmin={true}>
                    <ViewStudents />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            
            <Route
              path="/viewAll/:roleName"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true} sysadmin={true}>
                    <ViewDynamicRole />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />

            <Route
              path="/licenceupload"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true} sysadmin={true}>
                    <SysadminLicenceupload />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />

            <Route
              path="/getlicence"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true} sysadmin={true}>
                    <LicenceFileCreation />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            
            <Route
              path="/Zoomkeyupload"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true} sysadmin={true}>
                    <ZoomKeys />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/view/SocialLogin"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true} sysadmin={true}>
                    <SocialLoginKeys />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/Affiliates"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true} sysadmin={true}>
                    <Affiliates />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            <Route
              path="/viewAdmin/profile/:adminemail"
              element={
                <ErrorBoundary>
                  <PrivateRoute authenticationRequired={true} sysadmin={true}>
                    <AdminProfileView />
                  </PrivateRoute>
                </ErrorBoundary>
              }
            />
            {/* SysAdminRoutes */}
          </Route>
         
         
          <Route
            path="/"
            element={
              <ErrorBoundary>
                <RedirectComponent vpsonly={true} checkvisible={true}>
                  {" "}
                  <ViewCourseVps
                  />
                </RedirectComponent>
              </ErrorBoundary>
            }
          />
          <Route
            path="/login"
            element={
              <ErrorBoundary>
                <Login />
              </ErrorBoundary>
            }
          />
          <Route
            path="/refresh"
            element={
              <ErrorBoundary>
                <PrivateRoute authenticationRequired={true}>
                  <RefreshToken />
                </PrivateRoute>
              </ErrorBoundary>
            }
          />
          <Route
            path="/unauthorized"
            element={
              <ErrorBoundary>
                <Unauthorized />
              </ErrorBoundary>
            }
          />
          <Route
            path="/forgot-password"
            element={
              <ErrorBoundary>
                <ForgetPassword />
              </ErrorBoundary>
            }
          />
          <Route
            path="/RegisterInstitute"
            element={
              <ErrorBoundary>
                <RedirectComponent sasonly={true}>
                  <AdminRegister />
                </RedirectComponent>
              </ErrorBoundary>
            }
          />
          <Route
            path="/adminRegistration"
            element={
              <ErrorBoundary>
                <RedirectComponent admincount={true} vpsonly={true}>
                  <AdminRegister />
                </RedirectComponent>
              </ErrorBoundary>
            }
          />
          <Route
            path="/TrainerRegistration"
            element={
              <ErrorBoundary>
                <RedirectComponent vpsonly={true}>
                  <TrainerRegistration />
                </RedirectComponent>
              </ErrorBoundary>
            }
          />
          <Route
            path="/StudentRegistration"
            element={
              <ErrorBoundary>
                <RedirectComponent vpsonly={true}>
                  <StudentRegister />
                </RedirectComponent>
              </ErrorBoundary>
            }
          />
          <Route path="/register/user" element={  <ErrorBoundary><RoleRegistration /></ErrorBoundary>} />
          <Route
            path="/LicenceExpired"
            element={
              <ErrorBoundary>
                <LicenceExpired />
              </ErrorBoundary>
            }
          />

          <Route
            path="*"
            element={
              <ErrorBoundary>
                <Missing />
              </ErrorBoundary>
            }
          />
        </Routes>
        <Footer />
      </div>
    </Router>
  );
}

export default App;

