package utpDesarrolloMovil.demo.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailRequest {

    private String email;
    private String message;

}
