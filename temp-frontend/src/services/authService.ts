import { LoginRequest, SignupRequest, AuthResponse, User, ApiResponse } from '../types/auth';
import { ENDPOINTS, STORAGE_KEYS } from '../utils/constants';
import apiService from './api';

export class AuthService {
  async login(credentials: LoginRequest): Promise<AuthResponse> {
    try {
      console.log('🔐 Attempting login with:', { email: credentials.email });
      console.log('📡 Login URL:', `${apiService.getBaseUrl()}${ENDPOINTS.AUTH.LOGIN}`);
      
      const response = await apiService.post<ApiResponse<AuthResponse>>(ENDPOINTS.AUTH.LOGIN, credentials);
      
      console.log('✅ Login successful:', response);
      
      if (!response.success || !response.data) {
        throw new Error(response.message || 'Login failed');
      }
      
      // Store tokens and user data
      localStorage.setItem(STORAGE_KEYS.TOKEN, response.data.token);
      localStorage.setItem(STORAGE_KEYS.REFRESH_TOKEN, response.data.refreshToken);
      localStorage.setItem(STORAGE_KEYS.USER, JSON.stringify(response.data.user));
      
      return response.data;
    } catch (error) {
      console.error('❌ Login failed:', error);
      throw this.handleError(error);
    }
  }

  async signup(userData: SignupRequest): Promise<AuthResponse> {
    try {
      console.log('📝 Attempting signup with:', { 
        firstName: userData.firstName, 
        lastName: userData.lastName, 
        email: userData.email 
      });
      console.log('📡 Signup URL:', `${apiService.getBaseUrl()}${ENDPOINTS.AUTH.SIGNUP}`);
      console.log('📦 Full signup data:', userData);
      
      const response = await apiService.post<ApiResponse<AuthResponse>>(ENDPOINTS.AUTH.SIGNUP, userData);
      
      console.log('✅ Signup successful:', response);
      
      if (!response.success || !response.data) {
        throw new Error(response.message || 'Signup failed');
      }
      
      // Store tokens and user data
      localStorage.setItem(STORAGE_KEYS.TOKEN, response.data.token);
      localStorage.setItem(STORAGE_KEYS.REFRESH_TOKEN, response.data.refreshToken);
      localStorage.setItem(STORAGE_KEYS.USER, JSON.stringify(response.data.user));
      
      return response.data;
    } catch (error) {
      console.error('❌ Signup failed:', error);
      console.error('❌ Error details:', {
        message: error instanceof Error ? error.message : 'Unknown error',
        response: (error as any)?.response?.data,
        status: (error as any)?.response?.status
      });
      throw this.handleError(error);
    }
  }

  async logout(): Promise<void> {
    try {
      console.log('🚪 Attempting logout');
      await apiService.post(ENDPOINTS.AUTH.LOGOUT);
      console.log('✅ Logout successful');
    } catch (error) {
      console.error('❌ Logout error:', error);
    } finally {
      // Clear local storage regardless of API call success
      localStorage.removeItem(STORAGE_KEYS.TOKEN);
      localStorage.removeItem(STORAGE_KEYS.REFRESH_TOKEN);
      localStorage.removeItem(STORAGE_KEYS.USER);
    }
  }

  async refreshToken(refreshToken: string): Promise<{ token: string }> {
    try {
      console.log('🔄 Attempting token refresh');
      const response = await apiService.post<ApiResponse<AuthResponse>>(ENDPOINTS.AUTH.REFRESH, {
        refreshToken,
      });
      
      if (!response.success || !response.data) {
        throw new Error(response.message || 'Token refresh failed');
      }
      
      localStorage.setItem(STORAGE_KEYS.TOKEN, response.data.token);
      console.log('✅ Token refresh successful');
      return { token: response.data.token };
    } catch (error) {
      console.error('❌ Token refresh failed:', error);
      throw this.handleError(error);
    }
  }

  getCurrentUser(): User | null {
    const userStr = localStorage.getItem(STORAGE_KEYS.USER);
    if (userStr) {
      try {
        return JSON.parse(userStr);
      } catch (error) {
        console.error('Error parsing user data:', error);
        return null;
      }
    }
    return null;
  }

  getToken(): string | null {
    return localStorage.getItem(STORAGE_KEYS.TOKEN);
  }

  isAuthenticated(): boolean {
    return !!this.getToken() && !!this.getCurrentUser();
  }

  private handleError(error: any): Error {
    if (error.response?.data?.message) {
      return new Error(error.response.data.message);
    }
    if (error.message) {
      return new Error(error.message);
    }
    return new Error('An unexpected error occurred');
  }
}

export const authService = new AuthService();
export default authService; 