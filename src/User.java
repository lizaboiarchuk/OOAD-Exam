import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// this represents the user of our service
class User {
    private String id;
    private Tariff tariff;
    private PaymentPeriod paymentPeriod;
    private PaymentType paymentType;
    private String cardDetails;
    private HashMap<String, Date> unpaidBills = new HashMap<>();

    public Tariff getTariff() {
        return tariff;
    }

    public PaymentPeriod getPaymentPeriod() {
        return getPaymentPeriod();
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public String getCardDetails() {
        return cardDetails;
    }

    public void setBlocked(boolean b) {
    }

    public Date getNextPaymentDate() {
        return new Date();
    }

    public void addUnpaidBill(Invoice invoice) {
        unpaidBills.put(invoice.getId(), invoice.getDate());
    }

    public Map<String, Date> getUnpaidBills() {
        return unpaidBills;
    }
}


// this represents possible tariffs
class Tariff {
    private String name;
    private double price;
    public double getPrice() {
        return price;
    }
}


// payment period - monthly or yearly
enum PaymentPeriod {
    MONTHLY,
    YEARLY
}


// payment type - manual or automatic
enum PaymentType {
    AUTOMATIC,
    MANUAL
}

