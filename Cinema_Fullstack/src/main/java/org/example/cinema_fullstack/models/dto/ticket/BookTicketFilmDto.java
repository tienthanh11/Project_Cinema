package org.example.cinema_fullstack.models.dto.ticket;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookTicketFilmDto {
    private Long filmId;
    private String filmName;
    private String filmCategory;
    private Integer filmDuration;
    private String filmImageUrl;
}
