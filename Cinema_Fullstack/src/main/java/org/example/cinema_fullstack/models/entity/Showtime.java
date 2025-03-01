package org.example.cinema_fullstack.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Showtime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String filmTechnology;
    private String subTitle;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate day;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime time;


    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "film_id", referencedColumnName = "id")
    private Film film;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "cinemaRoom_id", referencedColumnName = "id")
    private CinemaRoom cinemaRoom;

    @JsonBackReference
    @OneToMany(mappedBy = "showtime")
    private List<Seat> seatList;

}
