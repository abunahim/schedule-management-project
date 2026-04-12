import { useState, useEffect } from 'react';
import ScheduleForm from '../components/ScheduleForm';
import ScheduleList from '../components/ScheduleList';
import {
  getAllSchedules,
  createSchedule,
  updateSchedule,
  deleteSchedule,
} from '../api/scheduleApi';

export default function Home() {
  const [schedules, setSchedules] = useState([]);
  const [editingSchedule, setEditingSchedule] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchSchedules();
  }, []);

  const fetchSchedules = async () => {
    try {
      const res = await getAllSchedules();
      setSchedules(res.data);
      setError(null);
    } catch (err) {
      setError('Failed to load schedules. Is the backend running?');
    }
  };

  const handleCreate = async (data) => {
    try {
      await createSchedule(data);
      setShowForm(false);
      fetchSchedules();
    } catch (err) {
      setError('Failed to create schedule.');
    }
  };

  const handleUpdate = async (data) => {
    try {
      await updateSchedule(editingSchedule.id, data);
      setEditingSchedule(null);
      fetchSchedules();
    } catch (err) {
      setError('Failed to update schedule.');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this schedule?')) return;
    try {
      await deleteSchedule(id);
      fetchSchedules();
    } catch (err) {
      setError('Failed to delete schedule.');
    }
  };

  const handleEdit = (schedule) => {
    setEditingSchedule(schedule);
    setShowForm(false);
  };

  return (
    <div className="container">
      <header className="app-header">
        <h1>🗓️ Schedule Management</h1>
        <button
          className="btn-primary"
          onClick={() => {
            setShowForm(!showForm);
            setEditingSchedule(null);
          }}
        >
          {showForm ? 'Cancel' : '+ New Schedule'}
        </button>
      </header>

      {error && <div className="error-banner">{error}</div>}

      {showForm && (
        <div className="form-container">
          <h2>Create Schedule</h2>
          <ScheduleForm onSubmit={handleCreate} onCancel={() => setShowForm(false)} />
        </div>
      )}

      {editingSchedule && (
        <div className="form-container">
          <h2>Edit Schedule</h2>
          <ScheduleForm
            onSubmit={handleUpdate}
            initialData={editingSchedule}
            onCancel={() => setEditingSchedule(null)}
          />
        </div>
      )}

      <h2>All Schedules ({schedules.length})</h2>
      <ScheduleList
        schedules={schedules}
        onEdit={handleEdit}
        onDelete={handleDelete}
      />
    </div>
  );
}