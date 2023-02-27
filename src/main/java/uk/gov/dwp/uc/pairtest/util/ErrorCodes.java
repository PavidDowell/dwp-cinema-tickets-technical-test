package uk.gov.dwp.uc.pairtest.util;

public enum ErrorCodes {
    INVALID_ACCOUNT_ID("Account Id cannot be equal to or less than 0"),
    TOO_FEW_TICKETS_ERROR("Ticket purchases cannot be less than or equal 0"),
    TOO_MANY_TICKETS_ERROR("Ticket purchases cannot be more than 20"),
    NO_ADULT_TICKETS_PRESENT("Ticket purchases don't include Adult tickets");


    private String errorMessage;

    private ErrorCodes(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
