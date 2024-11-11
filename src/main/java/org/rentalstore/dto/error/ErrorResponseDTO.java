package org.rentalstore.dto.error;

import lombok.Data;

@Data
public class ErrorResponseDTO {
    private int errorCode;
    private String error;
    private String message;
}
