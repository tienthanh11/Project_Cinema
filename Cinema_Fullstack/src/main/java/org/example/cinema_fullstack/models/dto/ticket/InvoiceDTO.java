package org.example.cinema_fullstack.models.dto.ticket;

import lombok.Data;

@Data
public class InvoiceDTO {
    private Long id;
    private Long memberId;
    private String memberCode;
    private String memberName;
    private String memberEmail;
    private String memberPhone;
    private String paymentMethod;
}
