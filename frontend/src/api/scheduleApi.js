import axios from 'axios';

const api = axios.create({
  baseURL: `/api/schedules`,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const getAllSchedules = () => api.get('');
export const getScheduleById = (id) => api.get(`/${id}`);
export const createSchedule = (data) => api.post('', data);
export const updateSchedule = (id, data) => api.put(`/${id}`, data);
export const deleteSchedule = (id) => api.delete(`/${id}`);