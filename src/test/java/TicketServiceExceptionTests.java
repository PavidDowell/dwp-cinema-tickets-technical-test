import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.util.ErrorCodes;
import uk.gov.dwp.uc.pairtest.validation.TicketValidator;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNotNull;

@RunWith(MockitoJUnitRunner.class)
public class TicketServiceExceptionTests {
    TicketValidator ticketValidator;
    @Mock
    TicketPaymentService ticketPaymentService;
    @Mock
    SeatReservationService seatReservationService;

    TicketServiceImpl ticketService;

    @Before
    public void setUp() {
        ticketValidator = new TicketValidator();
        ticketService = new TicketServiceImpl(ticketPaymentService, seatReservationService, ticketValidator);
    }

    @Test
    public void testPurchasingTicketWithInvalidAccountIDThrowsException() {
        assertThatExceptionOfType(InvalidPurchaseException.class)
                .isThrownBy(() -> ticketService.purchaseTickets(0l,
                        new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 21)))
                .withMessage(ErrorCodes.INVALID_ACCOUNT_ID.getErrorMessage());
    }


    @Test
    public void testPurchasingTooManyTicketsThrowsException() {
      assertThatExceptionOfType(InvalidPurchaseException.class)
              .isThrownBy(() -> ticketService.purchaseTickets(1l,
                      new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 21)))
              .withMessage(ErrorCodes.TOO_MANY_TICKETS_ERROR.getErrorMessage());
    }


    @Test
    public void testPurchasingTooFewTicketsThrowsException() {
        assertThatExceptionOfType(InvalidPurchaseException.class)
                .isThrownBy(() -> ticketService.purchaseTickets(1l,
                        new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 0)))
                .withMessage(ErrorCodes.TOO_FEW_TICKETS_ERROR.getErrorMessage());
    }


    @Test
    public void testPurchasingTicketsWithoutAdultPresentThrowsException() {
        assertThatExceptionOfType(InvalidPurchaseException.class)
                .isThrownBy(() -> ticketService.purchaseTickets(1L,
                        new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 10),
                        new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 10)))
                .withMessage(ErrorCodes.NO_ADULT_TICKETS_PRESENT.getErrorMessage());
    }
}
