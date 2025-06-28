export interface LoginRequest {
  email: string;
  password: string;
}

export interface SignupRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  phoneNumber: string;
  role: string;
}

export interface UserInfo {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  role: string;
  isVerified: boolean;
}

export interface AuthResponse {
  token: string;
  refreshToken: string;
  tokenType: string;
  expiresIn: number;
  user: UserInfo;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data?: T;
  error?: any;
}

export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  role: string;
  isVerified: boolean;
}

export interface AuthState {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
} 