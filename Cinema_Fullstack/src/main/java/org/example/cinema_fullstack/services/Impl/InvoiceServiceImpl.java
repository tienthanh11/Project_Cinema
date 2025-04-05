package org.example.cinema_fullstack.services.Impl;

import org.example.cinema_fullstack.common.SeatNotAvailableException;
import org.example.cinema_fullstack.models.dto.ticket.BookTicketShowtimeDto;
import org.example.cinema_fullstack.models.dto.ticket.BookingInformation;
import org.example.cinema_fullstack.models.dto.ticket.BookingInvoiceDTO;
import org.example.cinema_fullstack.models.dto.ticket.BookingTicketDTO;
import org.example.cinema_fullstack.models.entity.Invoice;
import org.example.cinema_fullstack.models.entity.Seat;
import org.example.cinema_fullstack.repositories.InvoiceRepository;
import org.example.cinema_fullstack.repositories.MemberShipRepository;
import org.example.cinema_fullstack.repositories.PaymentMethodRepository;
import org.example.cinema_fullstack.services.InvoiceService;
import org.example.cinema_fullstack.services.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    MemberShipRepository memberShipRepository;

    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private SeatService seatService;

//    @Autowired
//    private JavaMailSender emailSender;

    @Override
    public void checkSeatAvailable(BookingInformation bookingInformation) throws SeatNotAvailableException {
        Long showtimeId = bookingInformation.getShowtimeId();
        List<Long> seatIds = bookingInformation.getSeatIdList();

        if (seatIds == null || seatIds.isEmpty()) {
            throw new SeatNotAvailableException("Danh sách ghế không được để trống");
        }

        List<Seat> bookedSeats = seatService.findBookedSeatsForShowtime(seatIds, showtimeId);

        if (!bookedSeats.isEmpty()) {
            String unavailableSeats = bookedSeats.stream()
                    .map(Seat::getName)
                    .collect(Collectors.joining(", "));
            throw new SeatNotAvailableException("Ghế bạn chọn đã được đặt bởi người dùng khác: " + unavailableSeats);
        }
    }

    @Override
    public Invoice createInvoice(long membershipId, long paymentMethodId, String code) {
        Invoice invoice = new Invoice();
        invoice.setMembership(memberShipRepository.getById(membershipId));
        invoice.setPaymentMethod(paymentMethodRepository.getById(paymentMethodId));
        invoice.setCode(code);
        invoice = invoiceRepository.save(invoice);
        return invoice;
    }

    @Override
    public BookingInvoiceDTO getInvoiceByInvoiceId(long invoiceId) {
        return invoiceRepository.getInvoiceByInvoiceId(invoiceId);
    }

//    @Override
//    public void sendEmail(String email, String invoiceCode, BookTicketShowtimeDto showtime, List<BookingTicketDTO> ticketList) {
//        StringBuilder stringBuilder = new StringBuilder();
//        ArrayList<String> seatNameList = new ArrayList<>();
//        int amount = 0;
//        for (BookingTicketDTO ticket: ticketList){
//            seatNameList.add(ticket.getSeatName());
//            amount += ticket.getPrice();
//        }
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(email);
//        message.setSubject("A0720I1 Cinema - Thông tin đặt vé");
//        message.setText("Chào Quý khách!\n"
//                + "A0720I1 Cinema xin chân thành cảm ơn quý khách đã lựa chọn dịch vụ của chúng tôi.\n"
//                + "Thông tin đặt vé: \n\n"
//                + "Mã đặt vé: " + invoiceCode + "\n"
//                + "Tên phim: " + showtime.getFilmName() + "\n"
//                + "Rạp: " + showtime.getCinemaRoomName() + "\n"
//                + "Ngày chiếu: " + showtime.getShowtimeDay() + "\n"
//                + "Suất chiếu: " + showtime.getShowtimeTime() + "\n"
//                + "Ghế: " + String.join(", ", seatNameList) + "\n"
//                + "Tổng cộng: " + amount + "\n"
//                + "Quý khách vui lòng đưa mã đặt vé đến quầy vé để nhận vé" + "\n\n"
//                + "Trân trọng!");
//        this.emailSender.send(message);
//    }

    @Override
    public String generateCode() {
        int number1 = (int) Math.floor(1000 + Math.random() * 9000);
        int number2 = (int) Math.floor(1000 + Math.random() * 9000);
        int number3 = (int) Math.floor(1000 + Math.random() * 9000);
        int number4 = (int) Math.floor(1000 + Math.random() * 9000);
        return number1+"-"+number2+"-"+number3+"-"+number4;
    }
}
