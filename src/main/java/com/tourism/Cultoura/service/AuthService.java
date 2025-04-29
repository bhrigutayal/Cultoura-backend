package com.tourism.Cultoura.service;

import com.tourism.Cultoura.RequestModels.LoginRequest;
import com.tourism.Cultoura.RequestModels.RegistrationRequest;

public interface AuthService {
    void register(RegistrationRequest request);
    String authenticate(LoginRequest request);
}