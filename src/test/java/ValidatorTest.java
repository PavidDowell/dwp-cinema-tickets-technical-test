import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.util.ErrorCodes;
import uk.gov.dwp.uc.pairtest.validation.TicketValidator;

import java.util.Set;

import static org.junit.Assert.assertEquals;



@RunWith(MockitoJUnitRunner.class)
public class ValidatorTest {

    TicketValidator ticketValidator;

    @Before
    public void setup() {
        ticketValidator = new TicketValidator();
    }

    @Test
    public void ticketAccountIdValidation() {
        TicketTypeRequest infantTickets = new TicketTypeRequest(null, 0);
        Set<String> errors = ticketValidator.validate(0L, infantTickets);
        assertEquals(1, errors.size());
        assertEquals(errors.stream().findFirst().get(), ErrorCodes.INVALID_ACCOUNT_ID.getErrorMessage());
    }

    @Test
    public void tooFewTicketCountValidation() {
        TicketTypeRequest adultTickets = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 0);
        Set<String> errors = ticketValidator.validate(1L, adultTickets);
        assertEquals(1, errors.size());
    }

    @Test
    public void tooManyTicketCountValidation() {
        TicketTypeRequest adultTickets = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 21);
        Set<String> errors = ticketValidator.validate(1L, adultTickets);
        assertEquals(1, errors.size());
        assertEquals(ErrorCodes.TOO_MANY_TICKETS_ERROR.getErrorMessage(), errors.stream().findFirst().get());
    }

    @Test
    public void ticketCombinationValidation() {
        Set<String> errors = ticketValidator.validate(1L,
                                              new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1),
                                              new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 10));
        assertEquals(1, errors.size());
        assertEquals(ErrorCodes.NO_ADULT_TICKETS_PRESENT.getErrorMessage(), errors.stream().findFirst().get());
    }


    @Test
    public void ensureTerminateOnSingleErrorWhenMultiple() {
        Set<String> errors = ticketValidator.validate(1L,
                                                        new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 21),
                                                        new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 21));
        assertEquals(1, errors.size());
        assertEquals(ErrorCodes.TOO_MANY_TICKETS_ERROR.getErrorMessage(), errors.stream().findFirst().get());
    }
}
