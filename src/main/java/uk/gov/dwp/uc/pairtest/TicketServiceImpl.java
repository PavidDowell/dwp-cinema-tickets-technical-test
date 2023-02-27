package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.validation.TicketValidator;

import java.util.Arrays;
import java.util.Set;

public class TicketServiceImpl implements TicketService {

    private final TicketPaymentService ticketPaymentService;
    private final SeatReservationService seatReservationService;
    private final TicketValidator ticketValidator;


    public TicketServiceImpl(final TicketPaymentService ticketPaymentService,
                             final SeatReservationService seatReservationService,
                             final TicketValidator ticketValidator) {
        this.ticketPaymentService = ticketPaymentService;
        this.seatReservationService = seatReservationService;
        this.ticketValidator = ticketValidator;
    }

    /**
     *
     * @param accountId - id of the account making the requests
     * @param ticketTypeRequests contains enum for tickets types {@link TicketTypeRequest}
     * @throws InvalidPurchaseException when conditions aren't met see {@link TicketValidator}
     */
    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        Set<String> errors = ticketValidator.validate(accountId, ticketTypeRequests);
        if (errors.isEmpty()) {
            final int totalPrice = calculateTicketPrice(ticketTypeRequests);
            final int totalSeats = calculateNumberOfSeats(ticketTypeRequests);
            ticketPaymentService.makePayment(accountId, totalPrice);
            seatReservationService.reserveSeat(accountId, totalSeats);

        } else {
            errors.stream().findFirst().ifPresent(error -> {throw new InvalidPurchaseException(error); });
        }
    }

    /**
     * calculates price of tickets submitted, infants don't pay have a charge
     * @param ticketTypeRequests - ticket requests submitted
     * @return price of tickets requested
     */
    private int calculateTicketPrice(final TicketTypeRequest... ticketTypeRequests) {
        return Arrays.stream(ticketTypeRequests)
                .map(ticketTypeRequest ->
                        ticketTypeRequest.getTicketType().getPrice() * ticketTypeRequest.getNoOfTickets())
                .mapToInt(value -> value).sum();
    }

    /**
     * Calculates number of seats needed, uses filter to remove any Infants
     * @param ticketTypeRequests - tickets requests submitted
     * @return number of seats required
     */
    private int calculateNumberOfSeats(final TicketTypeRequest... ticketTypeRequests) {
        return Arrays.stream(ticketTypeRequests)
              .filter(type -> type.getTicketType().equals(TicketTypeRequest.Type.CHILD) || type.getTicketType().equals(TicketTypeRequest.Type.ADULT))
                .map(TicketTypeRequest::getNoOfTickets)
                .mapToInt(value -> value).sum();

    }
}
