package com.bruno.movieflix.dto;

import jakarta.persistence.Embeddable;

import java.time.Instant;

@Embeddable
public record AuthResponseDTO(String email, String token, Instant expires_in) {

}
