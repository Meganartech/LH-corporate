import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";

const RoleDetailsPage = () => {
  const { roleName } = useParams(); // captures role name from URL
  const [userData, setUserData] = useState([]);

  useEffect(() => {
    // Fetch users based on role
    axios.get(`/api/users?role=${roleName}`)
      .then(res => setUserData(res.data))
      .catch(err => console.error(err));
  }, [roleName]);

  return (
    <div className="table-container">
      <h2>{roleName.charAt(0).toUpperCase() + roleName.slice(1)} List</h2>
      <table className="table">
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Contact</th>
            {/* Add more columns as needed */}
          </tr>
        </thead>
        <tbody>
          {userData.map(user => (
            <tr key={user.id}>
              <td>{user.name}</td>
              <td>{user.email}</td>
              <td>{user.contact}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default RoleDetailsPage;
