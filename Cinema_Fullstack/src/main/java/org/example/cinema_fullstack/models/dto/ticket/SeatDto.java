package org.example.cinema_fullstack.models.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SeatDto {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String seatType;
    private String seatCode;
    private Long priceId;
    private Integer price;
    private Long ticketId;
}