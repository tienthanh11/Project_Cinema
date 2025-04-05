package org.example.cinema_fullstack.controllers;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.example.cinema_fullstack.common.paypal.PaypalPaymentIntent;
import org.example.cinema_fullstack.common.paypal.PaypalPaymentMethod;
import org.example.cinema_fullstack.models.dto.ticket.BookingInformation;
import org.example.cinema_fullstack.models.dto.ticket.PaymentDTO;
import org.example.cinema_fullstack.services.PaypalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/paypal")
public class PayPalController {

    public static final String URL_PAYPAL_SUCCESS = "/pay/success";
    public static final String URL_PAYPAL_CANCEL = "/pay/cancel";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PaypalService paypalService;

    @GetMapping("/pay")
    public String pay(HttpSession session,
                      RedirectAttributes redirectAttributes) {
        PaymentDTO paymentDTO = (PaymentDTO) session.getAttribute("paymentDTO");
        BookingInformation bookingInformation = (BookingInformation) session.getAttribute("bookingInformation");

        if (paymentDTO == null || paymentDTO.getAmount() == null) {
            redirectAttributes.addFlashAttribute("error", "Thông tin thanh toán không hợp lệ");
            return "redirect:/book/booking-confirmation";
        }

        if (bookingInformation == null || bookingInformation.getSeatIdList() == null || bookingInformation.getSeatIdList().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Thông tin đặt vé không hợp lệ");
            return "redirect:/book/booking-confirmation";
        }

        String cancelUrl = "http://localhost:8080/paypal" + URL_PAYPAL_CANCEL;
        String successUrl = "http://localhost:8080/paypal" + URL_PAYPAL_SUCCESS;

        try {
            Payment payment = paypalService.createPayment(
                    paymentDTO.getAmount(),
                    "USD",
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "Payment by Paypal",
                    cancelUrl,
                    successUrl);
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return "redirect:" + links.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            log.error("PayPal error: " + e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Lỗi xử lý thanh toán PayPal: " + e.getMessage());
        }
        return "redirect:/book/booking-confirmation";
    }

    @GetMapping(URL_PAYPAL_CANCEL)
    public String cancelPay(RedirectAttributes redirectAttributes,
                            HttpSession session) {
        session.removeAttribute("bookingInformation");
        session.removeAttribute("paymentDTO");

        redirectAttributes.addFlashAttribute("message", "Thanh toán đã bị hủy");
        return "redirect:/book/booking-confirmation";
    }

    @GetMapping(URL_PAYPAL_SUCCESS)
    public String successPay(@RequestParam("paymentId") String paymentId,
                             @RequestParam("PayerID") String payerId,
                             RedirectAttributes redirectAttributes) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                redirectAttributes.addFlashAttribute("message", "Thanh toán thành công");
                return "redirect:/book/create-booking";
            }
        } catch (PayPalRESTException e) {
            log.error("PayPal error: " + e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Xác minh thanh toán thất bại: " + e.getMessage());
        }
        return "redirect:/book/booking-confirmation";
    }
}
