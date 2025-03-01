package org.example.cinema_fullstack.repositories;

import org.example.cinema_fullstack.models.dto.ticket.BookingInvoiceDTO;
import org.example.cinema_fullstack.models.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    @Query(value = "select invoice.id as id, invoice.code as code, membership_id as memberId, membership.member_code as memberCode, membership.name as memberName, " +
            "membership.email as memberEmail, membership.phone as memberPhone, payment_method.name as paymentMethod " +
            "from invoice left join membership on invoice.membership_id = membership.id " +
            "left join payment_method on payment_method.id = invoice.payment_method_id " +
            "where invoice.id = ?1 limit 1", nativeQuery = true)
    BookingInvoiceDTO getInvoiceByInvoiceId(Long invoiceId);
}
