import java.util.*;

public class InvoiceService {
    private List<User> users;
    private static int MAX_RETRIES = 7;
    private static int MAX_UNPAID_BILLS = 5;
    private static int UNPAID_BILLS_TRACKING_TIME = 1000;
    private PaymentProvider paymentProvider;
    private List<Invoice> currentInvoices = new ArrayList<>();
    private List<Invoice> paidInvoices = new ArrayList<>();


    public InvoiceService(PaymentProvider paymentProvider) {
        this.paymentProvider = paymentProvider;
    }


    public void addUser(User user) {
        // here some additional checks/actions
        users.add(user);
    }


    // add new invoices when triggered by request
    public void updateInvoices(Date date) {
        List<Invoice> newInvoices = new ArrayList<>();
        for (User user: users) {
            if (user.getNextPaymentDate() == date) {
                Invoice invoice = new Invoice(user, user.getTariff().getPrice(), user.getNextPaymentDate());
                newInvoices.add(invoice);
            }
        }
        this.currentInvoices.addAll(newInvoices);
    }


    // charge all current invoices
    public void payInvoices() {
        for (Invoice invoice : currentInvoices) {
            User user = invoice.getUser();
            // charging only for automatic payment type
            if (user.getPaymentType() == PaymentType.AUTOMATIC) {
                boolean paymentSuccessful = paymentProvider.charge(user.getCardDetails(), invoice.getAmount());
                // if payment is successful - just move it to paidInvoices
                if (paymentSuccessful) {
                    invoice.setPaymentStatus(PaymentStatus.PAID);
                    currentInvoices.remove(invoice);
                    paidInvoices.add(invoice);
                } else {
                    // if failed - increasing number of trials to pay the invoice
                    invoice.increasePaymentTrials();
                    // if max_trials achieved - add this invoice as unpaid
                    if (invoice.getPaymentTrials() >= MAX_RETRIES) {
                        invoice.setPaymentStatus(PaymentStatus.UNPAID);
                        user.addUnpaidBill(invoice);
                        // check if user requires blocking after adding last unpaid bill - and block if necessary
                        blockUserIfRequired(user, invoice.getDate());
                    } else {
                        // set status to failed and delay payment for one day
                        invoice.setPaymentStatus(PaymentStatus.FAILED);
                        invoice.delayDate(1);
                    }
                }
            }
        }
    }


    private void blockUserIfRequired(User user, Date date) {
        int unpaidBills = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -UNPAID_BILLS_TRACKING_TIME);
        Date trackingStart = calendar.getTime();
        // track users unpaid bills and calculate how much of them were made during last N days
        for (Date invoiceDate : user.getUnpaidBills().values()) {
            if (invoiceDate.after(trackingStart)) {
                unpaidBills += 1;
            }
            // if there are more bills than MAX_UNPAID_BILLS - block the user.
            if (unpaidBills >= MAX_UNPAID_BILLS) {
               user.setBlocked(true);
            }
        }
    }
}


