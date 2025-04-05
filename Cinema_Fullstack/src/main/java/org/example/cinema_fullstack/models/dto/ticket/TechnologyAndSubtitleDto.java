package org.example.cinema_fullstack.models.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TechnologyAndSubtitleDto {
    private String filmTechnology;
    private String subtitle;
}
