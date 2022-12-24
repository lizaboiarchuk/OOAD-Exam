import java.util.Date;

public class Invoice {
    private String id;
    private User user;
    private double amount;
    private Date date;
    private PaymentStatus paymentStatus;
    private int paymentTrials = 0;

    public Invoice(User user, double price, Date date) {
    }

    public User getUser() {
        return user;
    }

    public double getAmount() {
        return amount;
    }

    public void setPaymentStatus(PaymentStatus status) {
        this.paymentStatus = status;
        if (status == PaymentStatus.FAILED) {
            paymentTrials = 0;
        }
    }

    public void delayDate(int days) {
    }

    public void increasePaymentTrials() {
        this.paymentTrials += 1;
    }

    public int getPaymentTrials() {
        return paymentTrials;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }
}


// represent the status of each invoice
enum PaymentStatus {
    PAID,
    UNPAID,
    FAILED
}

