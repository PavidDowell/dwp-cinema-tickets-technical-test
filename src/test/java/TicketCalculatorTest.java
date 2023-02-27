import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;


import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TicketCalculatorTest {
    @InjectMocks
    TicketServiceImpl ticketService;


    @Test
    public void testPurchaseTickets() throws Exception {
        TicketTypeRequest[] ticketTypeRequests = getTicketRequestsForPayments();
        int totalPrice = Whitebox.invokeMethod(ticketService, "calculateTicketPrice", ticketTypeRequests);
        assertEquals(280, totalPrice);
    }

    @Test
    public void testCalculateSeatsRequired() throws Exception {
        TicketTypeRequest[] ticketTypeRequests = getTicketRequestsForSeatsCalculation();
        int totalSeats = Whitebox.invokeMethod(ticketService, "calculateNumberOfSeats", ticketTypeRequests);
        assertEquals(15, totalSeats);

    }

    public TicketTypeRequest[] getTicketRequestsForPayments() {
        TicketTypeRequest[] ticketTypeRequests =
                {new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 5),
                 new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2),
                 new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 13)};
        return ticketTypeRequests;
    }


    public TicketTypeRequest[] getTicketRequestsForSeatsCalculation() {
        TicketTypeRequest[] ticketTypeRequests =
                {new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 500),
                 new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2),
                 new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 13)};
        return ticketTypeRequests;
    }

}
