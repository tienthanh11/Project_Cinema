package org.example.cinema_fullstack.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CinemaRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private String name;
    private int rowSeat;
    private int columnSeat;
    private String seatLayout;
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean status;

    @JsonBackReference
    @OneToMany(mappedBy = "cinemaRoom")
    private List<Showtime> showtimeList;
}
