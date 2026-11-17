package br.ufpa.refactoring.q1;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseEmailServiceTest {

    @Test
    void buildEmail_shouldKeepSameOutputAfterRefactor() {
        PurchaseEmailService svc = new PurchaseEmailService();

        var purchase = new PurchaseEmailService.Purchase(
                "123",
                "Victor",
                List.of(
                        new PurchaseEmailService.PurchaseItem("Caderno", 2, 15.0),
                        new PurchaseEmailService.PurchaseItem("Caneta", 3, 3.5)
                ),
                false,
                "VIP-20",
                new PurchaseEmailService.Address("Rua A", "10", "Belém", "66000-000")
        );

        String email = svc.buildEmail(purchase);

        String expected = "Olá, Victor!\n\n" +
                "Seu pedido #123 foi confirmado em " + LocalDate.now() + ".\n\n" +
                "Itens:\n" +
                "- Caderno (2x) : R$ " + String.format("%.2f", 30.00) + "\n" +
                "- Caneta (3x) : R$ " + String.format("%.2f", 10.50) + "\n" +
                "\nSubtotal: R$ " + String.format("%.2f", 40.50) + "\n" +
                "Frete: R$ " + String.format("%.2f", 14.90) + "\n" +
                "Descontos: R$ " + String.format("%.2f", 20.00) + "\n" +
                "TOTAL: R$ " + String.format("%.2f", 35.40) + "\n\n" +
                "Endereço de entrega:\n" +
                "Rua A, 10\n" +
                "Belém - 66000-000\n\n" +
                "Obrigado por comprar com a gente!\n";

        assertEquals(expected, email);
    }

    @Test
    void buildEmail_shouldApplyExpressShippingAndHighValueDiscount() {
        PurchaseEmailService svc = new PurchaseEmailService();

        var purchase = new PurchaseEmailService.Purchase(
                "999",
                "Ana",
                List.of(new PurchaseEmailService.PurchaseItem("Monitor", 1, 400.0)),
                true,
                null,
                new PurchaseEmailService.Address("Av. Nazaré", "500", "Belém", "66035-170")
        );

        String email = svc.buildEmail(purchase);

        assertTrue(email.contains("Subtotal: R$ " + String.format("%.2f", 400.00)));
        assertTrue(email.contains("Frete: R$ " + String.format("%.2f", 29.90)));
        assertTrue(email.contains("Descontos: R$ " + String.format("%.2f", 40.00)));
        assertTrue(email.contains("TOTAL: R$ " + String.format("%.2f", 389.90)));
    }
}
