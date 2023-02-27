package uk.gov.dwp.uc.pairtest.validation;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.util.ErrorCodes;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TicketValidator {

    private boolean isValid = true;

    /**
     * Used to validate ticket requests
     * @param accountId - id of account making request
     * @param ticketTypeRequests - tickets requested
     * @return Set of errors if conditions aren't met
     */
    public Set<String> validate(Long accountId, TicketTypeRequest... ticketTypeRequests) {
      Set<String> errors = new LinkedHashSet<>();
      determineIfValid(accountId, errors, ticketTypeRequests);
      return errors;
    }

    private boolean determineIfValid(final Long accountId, final Set<String> errors,
                                           final TicketTypeRequest... ticketTypeRequests) {
        if (!isValidAccountId(accountId, errors) || !validateTicketCount(errors, ticketTypeRequests)) {
            return false;
        }
        if (!validateAdultTicketIsPresent(errors, ticketTypeRequests)){
            return false;
        }
        return true;
    }

    private static boolean isValidAccountId(Long accountId, Set<String> errors) {
        boolean isValid = true;
        if(accountId <= 0) {
            errors.add(ErrorCodes.INVALID_ACCOUNT_ID.getErrorMessage());
            isValid = false;
        }
        return isValid;
    }

    private boolean validateTicketCount(Set<String> errors, final TicketTypeRequest... ticketTypeRequests) {
        int ticketCount = Arrays.stream(ticketTypeRequests).map(TicketTypeRequest::getNoOfTickets)
                                .mapToInt(Integer::intValue).sum();
        if (ticketCount <= 0) {
            isValid = false;
            errors.add(ErrorCodes.TOO_FEW_TICKETS_ERROR.getErrorMessage());
        } else if (ticketCount > 20) {
            isValid = false;
            errors.add(ErrorCodes.TOO_MANY_TICKETS_ERROR.getErrorMessage());
        }
        return isValid;
    }

    private boolean validateAdultTicketIsPresent(Set<String> errors, final TicketTypeRequest... ticketTypeRequests) {
        isValid = true;
        List<TicketTypeRequest> adultTicketRequests = Arrays.stream(ticketTypeRequests)
                .filter(ticketTypeRequest -> ticketTypeRequest.getTicketType() == TicketTypeRequest.Type.ADULT)
                .filter(ticketTypeRequest -> ticketTypeRequest.getNoOfTickets() != 0).collect(Collectors.toList());
        if (adultTicketRequests.isEmpty()) {
            isValid = false;
            errors.add(ErrorCodes.NO_ADULT_TICKETS_PRESENT.getErrorMessage());
        }
        return isValid;
    }
}
