export default function ScheduleList({ schedules, onEdit, onDelete }) {
  if (schedules.length === 0) {
    return <p className="empty-state">No schedules found. Create one above!</p>;
  }

  return (
    <div className="schedule-list">
      {schedules.map((schedule) => (
        <div key={schedule.id} className={`schedule-card status-${schedule.status.toLowerCase()}`}>
          <div className="schedule-card-header">
            <h3>{schedule.title}</h3>
            <span className={`badge ${schedule.status.toLowerCase()}`}>
              {schedule.status.replace('_', ' ')}
            </span>
          </div>
          {schedule.description && (
            <p className="schedule-description">{schedule.description}</p>
          )}
          <div className="schedule-times">
            <span>🕐 Start: {new Date(schedule.startTime).toLocaleString()}</span>
            <span>🕑 End: {new Date(schedule.endTime).toLocaleString()}</span>
          </div>
          <div className="schedule-card-footer">
            <small>Created: {new Date(schedule.createdAt).toLocaleDateString()}</small>
            <div className="card-actions">
              <button className="btn-edit" onClick={() => onEdit(schedule)}>Edit</button>
              <button className="btn-delete" onClick={() => onDelete(schedule.id)}>Delete</button>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
}