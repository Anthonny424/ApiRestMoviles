package utpDesarrolloMovil.demo.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"sandboxInitPoint"})
public record RecargaResponse(String sandboxInitPoint) {}
