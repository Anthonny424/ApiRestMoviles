package utpDesarrolloMovil.demo.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username", "message", "jwt", "status", "iduser"})
public record AuthResponse(String username, String message, String jwt, boolean status, int iduser) {
}
