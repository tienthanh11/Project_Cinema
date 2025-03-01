package org.example.cinema_fullstack.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String code;
    @JsonBackReference
    @OneToMany(mappedBy = "invoice")
    private List<Ticket> ticketList;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "membership_id", referencedColumnName = "id")
    private Membership membership;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "payment_method_id", referencedColumnName = "id")
    private PaymentMethod paymentMethod;
}
