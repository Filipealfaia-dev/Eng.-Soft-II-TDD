package br.ufpa.refactoring.q1;

import java.time.LocalDate;
import java.util.List;

public class PurchaseEmailService {

    public String buildEmail(Purchase purchase) {
        return buildEmail(purchase, LocalDate.now());
    }

    String buildEmail(Purchase purchase, LocalDate confirmationDate) {
        StringBuilder sb = new StringBuilder();
        appendHeader(sb, purchase, confirmationDate);
        appendItems(sb, purchase.items());
        appendSummary(sb, purchase);
        appendAddress(sb, purchase.address());
        appendFooter(sb);
        return sb.toString();
    }

    private void appendHeader(StringBuilder sb, Purchase purchase, LocalDate date) {
        sb.append("Olá, ").append(purchase.customerName()).append("!\n\n");
        sb.append("Seu pedido #").append(purchase.id()).append(" foi confirmado em ")
          .append(date).append(".\n\n");
    }

    private void appendItems(StringBuilder sb, List<PurchaseItem> items) {
        sb.append("Itens:\n");
        for (PurchaseItem item : items) {
            sb.append("- ").append(item.name())
              .append(" (").append(item.quantity()).append("x) : R$ ")
              .append(formatCurrency(item.totalPrice())).append("\n");
        }
    }

    private void appendSummary(StringBuilder sb, Purchase purchase) {
        sb.append("\nSubtotal: R$ ").append(formatCurrency(purchase.subtotal())).append("\n");
        sb.append("Frete: R$ ").append(formatCurrency(purchase.shippingCost())).append("\n");
        sb.append("Descontos: R$ ").append(formatCurrency(purchase.discountAmount())).append("\n");
        sb.append("TOTAL: R$ ").append(formatCurrency(purchase.total())).append("\n\n");
    }

    private void appendAddress(StringBuilder sb, Address address) {
        sb.append("Endereço de entrega:\n");
        sb.append(address.formatted()).append("\n\n");
    }

    private void appendFooter(StringBuilder sb) {
        sb.append("Obrigado por comprar com a gente!\n");
    }

    private String formatCurrency(double value) {
        return String.format("%.2f", value);
    }

    public record Purchase(String id, String customerName, List<PurchaseItem> items,
                           boolean isExpress, String coupon, Address address) {

        private static final double EXPRESS_SHIPPING = 29.90;
        private static final double STANDARD_SHIPPING = 14.90;
        private static final double BULK_DISCOUNT_THRESHOLD = 300.0;
        private static final double BULK_DISCOUNT_RATE = 0.10;
        private static final String VIP_COUPON_PREFIX = "VIP";
        private static final double VIP_DISCOUNT_VALUE = 20.0;

        public double subtotal() {
            return items.stream()
                        .mapToDouble(PurchaseItem::totalPrice)
                        .sum();
        }

        public double shippingCost() {
            return isExpress ? EXPRESS_SHIPPING : STANDARD_SHIPPING;
        }

        public double discountAmount() {
            double sub = subtotal();
            double discount = (sub >= BULK_DISCOUNT_THRESHOLD) ? sub * BULK_DISCOUNT_RATE : 0.0;
            if (hasVipCoupon()) {
                discount += VIP_DISCOUNT_VALUE;
            }
            return discount;
        }

        public double total() {
            return subtotal() + shippingCost() - discountAmount();
        }

        private boolean hasVipCoupon() {
            return coupon != null && coupon.startsWith(VIP_COUPON_PREFIX);
        }
    }

    public record PurchaseItem(String name, int quantity, double unitPrice) {
        public double totalPrice() {
            return unitPrice * quantity;
        }
    }

    public record Address(String street, String number, String city, String zip) {
        public String formatted() {
            return street + ", " + number + "\n" + city + " - " + zip;
        }
    }
}
