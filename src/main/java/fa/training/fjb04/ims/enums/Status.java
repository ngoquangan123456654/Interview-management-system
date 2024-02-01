package fa.training.fjb04.ims.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    WAITING_FOR_INTERVIEW(0, "Waiting For Interview"),
    WAITING_FOR_APPROVAL(1, "Waiting For Approval"),
    WAITING_FOR_RESPONSE(2, "Waiting For Response"),
    OPEN(3, "Open"),
    PASSED_INTERVIEW(4, "Passed Interview"),
    APPROVED_OFFER(5, "Approved Offer"),
    REJECTED_OFFER(6, "Rejected Offer"),
    ACCEPTED_OFFER(7, "Accepted Offer"),
    DECLINED_OFFER(8, "Declined Offer"),
    CANCELLED_OFFER(9, "Cancelled Offer"),
    FAILED_INTERVIEW(10, "Failed Interview"),
    CANCELLED_INTERVIEW(11, "Cancelled Interview"),
    BANNED(12, "Banned");

    private final int code;
    private final String name;

    public static Status fromCode(Integer code) {
        for (Status status : Status.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("No constant with name " + code + " found in status enum");
    }

    public static Status fromValue(String value) {
        for (Status status : Status.values()) {
            if (status.name.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No constant with value " + value + " found in status enum");

    }
}

