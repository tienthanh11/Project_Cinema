package org.example.cinema_fullstack.models.dto.ticket;

public class PaymentDTO {
    private Double amount;

    public PaymentDTO() {
    }

    public PaymentDTO(Double amount) {
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
