// API Configuration for Microservices Architecture with API Gateway
export const API_GATEWAY_URL = process.env.REACT_APP_API_GATEWAY_URL || 'http://localhost:8080';
export const API_BASE_URL = API_GATEWAY_URL; // Connect through API Gateway

// Service-specific URLs (for future direct access if needed)
export const AUTH_SERVICE_URL = process.env.REACT_APP_AUTH_SERVICE_URL || 'http://localhost:8081';
export const USER_SERVICE_URL = process.env.REACT_APP_USER_SERVICE_URL || 'http://localhost:8082';
export const CLINIC_SERVICE_URL = process.env.REACT_APP_CLINIC_SERVICE_URL || 'http://localhost:8083';

// API Endpoints through API Gateway
export const ENDPOINTS = {
  AUTH: {
    LOGIN: '/api/auth/login',
    SIGNUP: '/api/auth/register',
    REFRESH: '/api/auth/refresh',
    LOGOUT: '/api/auth/logout',
    VALIDATE: '/api/auth/validate',
  },
  USER: {
    PROFILE: '/api/users/profile',
    UPDATE: '/api/users/update',
  },
} as const;

// Local Storage Keys
export const STORAGE_KEYS = {
  TOKEN: 'tulip_token',
  REFRESH_TOKEN: 'tulip_refresh_token',
  USER: 'tulip_user',
} as const;

// Routes
export const ROUTES = {
  HOME: '/',
  LOGIN: '/login',
  SIGNUP: '/signup',
  DASHBOARD: '/dashboard',
  PROFILE: '/profile',
} as const;

// Clinic Information
export const CLINIC_INFO = {
  NAME: 'Tulip Clinic',
  TAGLINE: 'Your Beauty, Our Priority',
  DESCRIPTION: 'Professional laser body shaving and beauty treatments',
  PHONE: '+1 (555) 123-4567',
  EMAIL: 'info@tulipclinic.com',
  ADDRESS: '123 Beauty Street, Downtown, City',
} as const; 