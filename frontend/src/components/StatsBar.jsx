export default function StatsBar({ stats }) {
  if (!stats) return null;

  const items = [
    { label: 'Total', value: stats.TOTAL, color: '#2c3e50' },
    { label: 'Scheduled', value: stats.SCHEDULED, color: '#3498db' },
    { label: 'In Progress', value: stats.IN_PROGRESS, color: '#f39c12' },
    { label: 'Completed', value: stats.COMPLETED, color: '#2ecc71' },
    { label: 'Cancelled', value: stats.CANCELLED, color: '#e74c3c' },
  ];

  return (
    <div className="stats-bar">
      {items.map((item) => (
        <div key={item.label} className="stat-card">
          <div className="stat-value" style={{ color: item.color }}>
            {item.value ?? 0}
          </div>
          <div className="stat-label">{item.label}</div>
        </div>
      ))}
    </div>
  );
}