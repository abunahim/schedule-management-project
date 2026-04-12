import { useState, useEffect } from 'react';

const defaultForm = {
  title: '',
  description: '',
  startTime: '',
  endTime: '',
  status: 'SCHEDULED',
};

export default function ScheduleForm({ onSubmit, initialData, onCancel }) {
  const [form, setForm] = useState(defaultForm);

  useEffect(() => {
    if (initialData) {
      setForm({
        title: initialData.title || '',
        description: initialData.description || '',
        startTime: initialData.startTime?.slice(0, 16) || '',
        endTime: initialData.endTime?.slice(0, 16) || '',
        status: initialData.status || 'SCHEDULED',
      });
    }
  }, [initialData]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit({
      ...form,
      startTime: form.startTime + ':00',
      endTime: form.endTime + ':00',
    });
    setForm(defaultForm);
  };

  return (
    <form onSubmit={handleSubmit} className="schedule-form">
      <div className="form-group">
        <label>Title</label>
        <input
          name="title"
          value={form.title}
          onChange={handleChange}
          required
          placeholder="Enter title"
        />
      </div>
      <div className="form-group">
        <label>Description</label>
        <textarea
          name="description"
          value={form.description}
          onChange={handleChange}
          placeholder="Enter description"
        />
      </div>
      <div className="form-group">
        <label>Start Time</label>
        <input
          type="datetime-local"
          name="startTime"
          value={form.startTime}
          onChange={handleChange}
          required
        />
      </div>
      <div className="form-group">
        <label>End Time</label>
        <input
          type="datetime-local"
          name="endTime"
          value={form.endTime}
          onChange={handleChange}
          required
        />
      </div>
      <div className="form-group">
        <label>Status</label>
        <select name="status" value={form.status} onChange={handleChange}>
          <option value="SCHEDULED">Scheduled</option>
          <option value="IN_PROGRESS">In Progress</option>
          <option value="COMPLETED">Completed</option>
          <option value="CANCELLED">Cancelled</option>
        </select>
      </div>
      <div className="form-actions">
        <button type="submit">Save</button>
        {onCancel && <button type="button" onClick={onCancel}>Cancel</button>}
      </div>
    </form>
  );
}