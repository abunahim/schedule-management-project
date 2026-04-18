import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import ScheduleForm from '../components/ScheduleForm';
import ScheduleList from '../components/ScheduleList';
import {
  createSchedule,
  updateSchedule,
  deleteSchedule,
  getSchedulesPaged,
} from '../api/scheduleApi';

export default function Home() {
  const [schedules, setSchedules] = useState([]);
  const [editingSchedule, setEditingSchedule] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [error, setError] = useState(null);

  const [page, setPage] = useState(0);
  const [size, setSize] = useState(5);
  const [status, setStatus] = useState('');
  const [sortBy, setSortBy] = useState('startTime');
  const [sortDir, setSortDir] = useState('asc');
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const navigate = useNavigate();
  const username = localStorage.getItem('username');

  useEffect(() => {
    fetchSchedules();
  }, [page, size, status, sortBy, sortDir]);

  const fetchSchedules = async () => {
    try {
      const res = await getSchedulesPaged({ page, size, status, sortBy, sortDir });
      setSchedules(res.data.content);
      setTotalPages(res.data.totalPages);
      setTotalElements(res.data.totalElements);
      setError(null);
    } catch (err) {
      if (err.response?.status === 401 || err.response?.status === 403) {
        handleLogout();
      } else {
        setError('Failed to load schedules.');
      }
    }
  };

  const handleCreate = async (data) => {
    try {
      await createSchedule(data);
      setShowForm(false);
      setPage(0);
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

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    navigate('/login');
  };

  return (
    <div className="container">
      <header className="app-header">
        <h1>🗓️ Schedule Management</h1>
        <div className="header-right">
          <span className="username-badge">👤 {username}</span>
          <button className="btn-primary" onClick={() => {
            setShowForm(!showForm);
            setEditingSchedule(null);
          }}>
            {showForm ? 'Cancel' : '+ New Schedule'}
          </button>
          <button className="btn-logout" onClick={handleLogout}>Logout</button>
        </div>
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

      {/* Filters & Sort */}
      <div className="filters-bar">
        <div className="filter-group">
          <label>Status</label>
          <select value={status} onChange={(e) => { setStatus(e.target.value); setPage(0); }}>
            <option value="">All</option>
            <option value="SCHEDULED">Scheduled</option>
            <option value="IN_PROGRESS">In Progress</option>
            <option value="COMPLETED">Completed</option>
            <option value="CANCELLED">Cancelled</option>
          </select>
        </div>
        <div className="filter-group">
          <label>Sort By</label>
          <select value={sortBy} onChange={(e) => { setSortBy(e.target.value); setPage(0); }}>
            <option value="startTime">Start Time</option>
            <option value="endTime">End Time</option>
            <option value="title">Title</option>
            <option value="createdAt">Created At</option>
          </select>
        </div>
        <div className="filter-group">
          <label>Direction</label>
          <select value={sortDir} onChange={(e) => { setSortDir(e.target.value); setPage(0); }}>
            <option value="asc">Ascending</option>
            <option value="desc">Descending</option>
          </select>
        </div>
        <div className="filter-group">
          <label>Per Page</label>
          <select value={size} onChange={(e) => { setSize(Number(e.target.value)); setPage(0); }}>
            <option value={5}>5</option>
            <option value={10}>10</option>
            <option value={20}>20</option>
          </select>
        </div>
      </div>

      <h2>All Schedules ({totalElements})</h2>
      <ScheduleList
        schedules={schedules}
        onEdit={handleEdit}
        onDelete={handleDelete}
      />

      {/* Pagination Controls */}
      {totalPages > 1 && (
        <div className="pagination">
          <button
            className="page-btn"
            onClick={() => setPage(0)}
            disabled={page === 0}
          >«</button>
          <button
            className="page-btn"
            onClick={() => setPage(p => p - 1)}
            disabled={page === 0}
          >‹</button>
          <span className="page-info">Page {page + 1} of {totalPages}</span>
          <button
            className="page-btn"
            onClick={() => setPage(p => p + 1)}
            disabled={page >= totalPages - 1}
          >›</button>
          <button
            className="page-btn"
            onClick={() => setPage(totalPages - 1)}
            disabled={page >= totalPages - 1}
          >»</button>
        </div>
      )}
    </div>
  );
}