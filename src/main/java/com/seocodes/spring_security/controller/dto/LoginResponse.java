package com.seocodes.spring_security.controller.dto;

public record LoginResponse(String accessToken, Long expiresIn) {
}
