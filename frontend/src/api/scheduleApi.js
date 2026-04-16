import axios from 'axios';

const api = axios.create({
  baseURL: `/api`,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add JWT token to every request
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Auth
export const register = (data) => api.post('/auth/register', data);
export const login = (data) => api.post('/auth/login', data);

// Schedules
export const getAllSchedules = () => api.get('/schedules');
export const getScheduleById = (id) => api.get(`/schedules/${id}`);
export const createSchedule = (data) => api.post('/schedules', data);
export const updateSchedule = (id, data) => api.put(`/schedules/${id}`, data);
export const deleteSchedule = (id) => api.delete(`/schedules/${id}`);
export const getSchedulesPaged = (params) => api.get('/schedules/paged', { params });