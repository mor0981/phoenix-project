package com.phoenix.project.config;

import com.phoenix.project.client.entity.Client;
import com.phoenix.project.client.repository.ClientRepository;
import com.phoenix.project.order.entity.Order;
import com.phoenix.project.order.repository.OrderRepository;
import com.phoenix.project.product.entity.Product;
import com.phoenix.project.product.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            ClientRepository clientRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            String hash = passwordEncoder.encode("password123");

            // Users
            Client mor = Client.builder()
                    .firstName("Mor").lastName("Biton")
                    .birthDate(LocalDate.of(1990, 1, 1))
                    .contactMethodType(Client.ContactMethodType.EMAIL)
                    .methodValue("mor@phoenix.com")
                    .password(hash)
                    .role(Client.Role.ADMIN)
                    .blocked(false)
                    .build();

            Client dani = Client.builder()
                    .firstName("Dani").lastName("Cohen")
                    .birthDate(LocalDate.of(1995, 5, 15))
                    .contactMethodType(Client.ContactMethodType.EMAIL)
                    .methodValue("dani@test.com")
                    .password(hash)
                    .role(Client.Role.USER)
                    .blocked(false)
                    .build();

            Client tomer = Client.builder()
                    .firstName("Tomer").lastName("Adar")
                    .birthDate(LocalDate.of(1998, 8, 20))
                    .contactMethodType(Client.ContactMethodType.EMAIL)
                    .methodValue("tomer@test.com")
                    .password(hash)
                    .role(Client.Role.USER)
                    .blocked(false)
                    .build();

            clientRepository.save(mor);
            clientRepository.save(dani);
            clientRepository.save(tomer);

            // Products (seller = Dani)
            Product p1 = Product.builder().title("iPhone 14 Pro")
                    .description("Excellent condition, original box, charger and cable included")
                    .price(BigDecimal.valueOf(2999.99)).category("electronics")
                    .status(Product.ProductStatus.AVAILABLE).seller(dani).build();

            Product p2 = Product.builder().title("Keychron K2 Keyboard")
                    .description("Cherry MX Red switches, RGB backlight, barely used")
                    .price(BigDecimal.valueOf(450.00)).category("accessories")
                    .status(Product.ProductStatus.AVAILABLE).seller(dani).build();

            Product p3 = Product.builder().title("Nintendo Switch OLED")
                    .description("Includes 2 Joy-Cons and 128GB memory card")
                    .price(BigDecimal.valueOf(1200.00)).category("gaming")
                    .status(Product.ProductStatus.AVAILABLE).seller(dani).build();

            Product p4 = Product.builder().title("AirPods Pro 2nd Gen")
                    .description("Active noise cancellation, MagSafe charging case")
                    .price(BigDecimal.valueOf(799.00)).category("electronics")
                    .status(Product.ProductStatus.AVAILABLE).seller(dani).build();

            Product p5 = Product.builder().title("DXRacer Gaming Chair")
                    .description("Black/red color, very good condition, used for one year")
                    .price(BigDecimal.valueOf(850.00)).category("furniture")
                    .status(Product.ProductStatus.AVAILABLE).seller(dani).build();

            Product p6 = Product.builder().title("LG 27 4K Monitor")
                    .description("IPS 144Hz, DisplayPort + HDMI, original packaging included")
                    .price(BigDecimal.valueOf(1800.00)).category("electronics")
                    .status(Product.ProductStatus.AVAILABLE).seller(dani).build();

            Product p7 = Product.builder().title("Clean Code - Book")
                    .description("Robert C. Martin, good condition, a few pencil marks inside")
                    .price(BigDecimal.valueOf(60.00)).category("books")
                    .status(Product.ProductStatus.AVAILABLE).seller(dani).build();

            Product p8 = Product.builder().title("MacBook Air M2")
                    .description("16GB RAM, 512GB SSD, excellent condition, no scratches")
                    .price(BigDecimal.valueOf(5500.00)).category("electronics")
                    .status(Product.ProductStatus.SOLD).seller(dani).build();

            Product p9 = Product.builder().title("Sony WH-1000XM5")
                    .description("Premium noise cancelling headphones, like new, with carry case")
                    .price(BigDecimal.valueOf(950.00)).category("electronics")
                    .status(Product.ProductStatus.SOLD).seller(dani).build();

            productRepository.save(p1);
            productRepository.save(p2);
            productRepository.save(p3);
            productRepository.save(p4);
            productRepository.save(p5);
            productRepository.save(p6);
            productRepository.save(p7);
            productRepository.save(p8);
            productRepository.save(p9);

            // Orders (Tomer purchased p8 and p9)
            Order o1 = Order.builder()
                    .buyer(tomer).product(p8)
                    .status(Order.OrderStatus.COMPLETED)
                    .priceAtPurchase(BigDecimal.valueOf(5500.00))
                    .build();

            Order o2 = Order.builder()
                    .buyer(tomer).product(p9)
                    .status(Order.OrderStatus.COMPLETED)
                    .priceAtPurchase(BigDecimal.valueOf(950.00))
                    .build();

            orderRepository.save(o1);
            orderRepository.save(o2);

            System.out.println("=== Data initialized successfully ===");
            System.out.println("Mor Biton   → mor@phoenix.com  (ADMIN)");
            System.out.println("Dani Cohen  → dani@test.com    (USER)");
            System.out.println("Tomer Adar  → tomer@test.com   (USER)");
            System.out.println("Password for all: password123");
        };
    }
}