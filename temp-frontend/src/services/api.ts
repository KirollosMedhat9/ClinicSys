import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios';
import { API_BASE_URL, STORAGE_KEYS } from '../utils/constants';

class ApiService {
  private api: AxiosInstance;

  constructor() {
    this.api = axios.create({
      baseURL: API_BASE_URL,
      timeout: 10000,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    this.setupInterceptors();
  }

  public getBaseUrl(): string {
    return API_BASE_URL;
  }

  private setupInterceptors(): void {
    // Request interceptor to add auth token
    this.api.interceptors.request.use(
      (config) => {
        const token = localStorage.getItem(STORAGE_KEYS.TOKEN);
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );

    // Response interceptor to handle token refresh and errors
    this.api.interceptors.response.use(
      (response: AxiosResponse) => {
        return response;
      },
      async (error) => {
        const originalRequest = error.config;

        // Handle 401 Unauthorized errors
        if (error.response?.status === 401 && !originalRequest._retry) {
          originalRequest._retry = true;

          try {
            const refreshToken = localStorage.getItem(STORAGE_KEYS.REFRESH_TOKEN);
            if (refreshToken) {
              // Call refresh endpoint with Authorization header containing refresh token
              const response = await this.api.post('/api/auth/refresh', {}, {
                headers: {
                  'Authorization': `Bearer ${refreshToken}`
                }
              });

              const { data } = response.data;
              if (data && data.token) {
                localStorage.setItem(STORAGE_KEYS.TOKEN, data.token);
                localStorage.setItem(STORAGE_KEYS.REFRESH_TOKEN, data.refreshToken);
                
                originalRequest.headers.Authorization = `Bearer ${data.token}`;
                return this.api(originalRequest);
              }
            }
          } catch (refreshError) {
            // Refresh token failed, redirect to login
            localStorage.removeItem(STORAGE_KEYS.TOKEN);
            localStorage.removeItem(STORAGE_KEYS.REFRESH_TOKEN);
            localStorage.removeItem(STORAGE_KEYS.USER);
            window.location.href = '/login';
          }
        }

        // Handle other errors
        if (error.response?.data?.message) {
          console.error('API Error:', error.response.data.message);
        }

        return Promise.reject(error);
      }
    );
  }

  // Generic HTTP methods
  public async get<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    const response = await this.api.get<T>(url, config);
    return response.data;
  }

  public async post<T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    const response = await this.api.post<T>(url, data, config);
    return response.data;
  }

  public async put<T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    const response = await this.api.put<T>(url, data, config);
    return response.data;
  }

  public async delete<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    const response = await this.api.delete<T>(url, config);
    return response.data;
  }

  public async patch<T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    const response = await this.api.patch<T>(url, data, config);
    return response.data;
  }
}

export const apiService = new ApiService();
export default apiService; 