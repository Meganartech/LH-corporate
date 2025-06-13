
import React, { useState } from 'react';
import baseUrl from '../api/utils';
import axios from 'axios';

import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js'; 

const CreateBatchModel = ({ setSelectedBatches, closeModal,setErrors }) => {

  const [batchTitle, setBatchTitle] = useState('First batch');
  const [durationInHours,setdurationInHours]=useState('300');
  const handleSubmit = async () => {
    const token = sessionStorage.getItem('token'); // Get the token from sessionStorage

    if (!batchTitle || !durationInHours) {
           MySwal.fire({
                toast:true,
          position: 'top-end', 
          icon: 'warning',
          title: 'Please fill all Fields before Submitting !',
          showConfirmButton: false,
          timer: 3000,
          timerProgressBar: true,
          didOpen: (toast) => {
            toast.addEventListener('mouseenter', Swal.stopTimer);
            toast.addEventListener('mouseleave', Swal.resumeTimer);
          }
        });
      return;
    }

    try {
      const response = await axios.post(
        `${baseUrl}/batch/partial/save`, // Construct the API endpoint
        null, // No request body since data is sent as query parameters
        {
          params: {
            batchTitle,
            durationInHours
          },
          headers: {
            Authorization: token, // Add the token as a header
          },
        }
      );

      if (response.status === 200) {
        const batch = response.data;

        setSelectedBatches((prev) => [
          ...prev, // Spread the previous state (array)
          { id: batch.id, batchId: batch.batchId, batchTitle: batchTitle } // Add the new batch
        ]);
        setErrors((prevErrors) => ({
          ...prevErrors,
          batches: "",
        }));
  
        // Close the modal after submitting
        closeModal();
      }
    } catch (error) {
      console.error('Error creating batch:', error);
           MySwal.fire({
                toast:true,
          position: 'top-end', 
          icon: 'error',
          title: 'Error occured whiled creating the batch !',
          showConfirmButton: false,
          timer: 3000,
          timerProgressBar: true,
          didOpen: (toast) => {
            toast.addEventListener('mouseenter', Swal.stopTimer);
            toast.addEventListener('mouseleave', Swal.resumeTimer);
          }
        });
    }
  };

  return (
    <div
      className="modal fade show "
      id="createBatchModal"
      tabIndex="-1"
      aria-labelledby="createBatchModalLabel"
      aria-hidden="true"
      style={{ display: 'block' }} // Display the modal
    >
      <div className="modal-dialog modal-dialog-centered">
        <div className="modal-content shadowcard">
          <div className="modal-header">
            <h5 className="modal-title" id="createBatchModalLabel">Add New Batch</h5>
            <button
              type="button"
              className="btn-close"
              onClick={closeModal} // Call closeModal function on click
              aria-label="Close"
            />
          </div>
          <div className="modal-body">
            <form>
              <div className="form-group">
                <label htmlFor="batch-title" className="col-form-label">
                  Batch Title:
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="batch-title"
                  value={batchTitle}
                  onChange={(e) => setBatchTitle(e.target.value)}
                  autoFocus
                />
              </div>
              <div className="form-group">
                <label htmlFor="durationInHours" className="col-form-label">
                  Duration (Hours):
                </label>
                <input
                  type="number"
                  className="form-control"
                  id="durationInHours"
                  min={1} // Set the min attribute dynamically based on the start date
                  value={durationInHours}
                  onChange={(e) => setdurationInHours(e.target.value)}
                />
              </div>
            </form>
          </div>
          <div className="modal-footer">
            <button
              type="button"
              className="btn btn-secondary"
              onClick={closeModal} // Close modal
            >
              Close
            </button>
            <button
              type="button"
              className="btn btn-primary"
              onClick={handleSubmit} // Call handleSubmit to save data
            >
              Save
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CreateBatchModel;
