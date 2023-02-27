package uk.gov.dwp.uc.pairtest.domain;

/**
 * Immutable Object
 */

public final class TicketTypeRequest {

    private final int noOfTickets;
    private final Type type;

    public TicketTypeRequest(Type type, int noOfTickets) {
        this.type = type;
        this.noOfTickets = noOfTickets;
    }

    public int getNoOfTickets() {
        return noOfTickets;
    }

    public Type getTicketType() {
        return type;
    }

    public enum Type {
        ADULT("Adult", 20),
        CHILD("Child", 10) ,
        INFANT("Infant", 0);
        final String ticketType;
        final int price;
        Type(final String ticketType,final int price) {
            this.ticketType = ticketType;
            this.price = price;
        }

        public String getTicketType() {
            return ticketType;
        }

        public int getPrice() {
            return price;
        }

    }

}
