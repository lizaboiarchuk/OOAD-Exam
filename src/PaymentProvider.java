// charges given amount from card
interface PaymentProvider {
    boolean charge(String cardDetails, double amount);
}