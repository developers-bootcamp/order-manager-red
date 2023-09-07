package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fill")
@CrossOrigin("http://localhost:3000")
public class FillController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    private BCryptPasswordEncoder passwordEncoder;

    public FillController() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @GetMapping("/fill")
    public String fill() {

        AuditData auditData = AuditData.builder().createDate(LocalDate.now()).updateDate(LocalDate.now()).build();

        Company company = Company.builder().name("Kama Tech").currency(Currency.$).auditData(auditData).build();
        companyRepository.save(company);


        Role role1 = Role.builder().id("1").name(RoleOptions.ADMIN).desc("admin").auditData(auditData).build();
        Role role2 = Role.builder().id("2").name(RoleOptions.EMPLOYEE).desc("employee").auditData(auditData).build();
        Role role3 = Role.builder().id("3").name(RoleOptions.CUSTOMER).desc("customer").auditData(auditData).build();
        roleRepository.save(role1);
        roleRepository.save(role2);
        roleRepository.save(role3);

        List<User> users = new ArrayList<>();
        User userAdmin1 = User.builder().fullName("Klaus Wernreuther").password(passwordEncoder.encode("123456789"))
                .address(Address.builder().email("klaus.wernreuther@gmail.com").name("Walldorf, Baden-Wurttemberg, Germany").phone("0504100000").build())
                .roleId(role1).companyId(company).auditData(auditData).build();
        users.add(userAdmin1);

        User userCust1 = User.builder().fullName("Ayala Brandman")
                .address(Address.builder().email("ayala09542@gmail.com").name("Rashi Street").phone("0504111111").build())
                .roleId(role3).companyId(company).auditData(auditData).build();
        User userCust2 = User.builder().fullName("Chany Fisher")
                .address(Address.builder().email("ch5706802@gmail.com").name("Arlozorov Street").phone("0504122222").build())
                .roleId(role3).companyId(company).auditData(auditData).build();
        User userCust3 = User.builder().fullName("Avigail Rozovski")
                .address(Address.builder().email("avigail4192@gmail.com").name("Nissan Street").phone("0504133333").build())
                .roleId(role3).companyId(company).auditData(auditData).build();
        User userCust4 = User.builder().fullName("Ayala Sprei")
                .address(Address.builder().email("a0583269340@gmail.com").name("father Street").phone("0504144444").build())
                .roleId(role3).companyId(company).auditData(auditData).build();
        User userCust5 = User.builder().fullName("Chani Rozenboim")
                .address(Address.builder().email("chani17393@gmail.com").name("Moshe Arens Street").phone("0504155555").build())
                .roleId(role3).companyId(company).auditData(auditData).build();
        User userCust6 = User.builder().fullName("efrat")
                .address(Address.builder().email("efratav.post@gmail.com").name("Hannah Szenes Street").phone("0504166666").build())
                .roleId(role3).companyId(company).auditData(auditData).build();
        User userCust7 = User.builder().fullName("Esty Froyvirt")
                .address(Address.builder().email("esty2048@gmail.com").name("Lela Sulica Street").phone("0504177777").build())
                .roleId(role3).companyId(company).auditData(auditData).build();
        User userCust8 = User.builder().fullName("Rachel Livne")
                .address(Address.builder().email("rachel01576@gmail.com").name("Lela Sulica Street").phone("0504188888").build())
                .roleId(role3).companyId(company).auditData(auditData).build();
        User userCust9 = User.builder().fullName("Sara Leah Margulies")
                .address(Address.builder().email("slm3201386@gmail.com").name("Rival Street").phone("0504199999").build())
                .roleId(role3).companyId(company).auditData(auditData).build();
        users.add(userCust1);
        users.add(userCust2);
        users.add(userCust3);
        users.add(userCust4);
        users.add(userCust5);
        users.add(userCust6);
        users.add(userCust7);
        users.add(userCust8);
        users.add(userCust9);

        User userEmp1 = User.builder().fullName("Chani Donat").password(passwordEncoder.encode("987654321"))
                .address(Address.builder().email("chani72901@gmail.com").name("Rival Street").phone("0527611111").build())
                .roleId(role2).companyId(company).auditData(auditData).build();
        User userEmp2 = User.builder().fullName("Elisheva Bandel").password(passwordEncoder.encode("987654321"))
                .address(Address.builder().email("e39857@gmail.com").name("Pavel Frankel Street").phone("0527622222").build())
                .roleId(role2).companyId(company).auditData(auditData).build();
        User userEmp3 = User.builder().fullName("Pnina Friedman").password(passwordEncoder.encode("987654321"))
                .address(Address.builder().email("p0583289998@gmail.com").name("Lela Sulica Street").phone("0527633333").build())
                .roleId(role2).companyId(company).auditData(auditData).build();
        User userEmp4 = User.builder().fullName("Shirel Asayag").password(passwordEncoder.encode("987654321"))
                .address(Address.builder().email("asayagshirel@gmail.com").name("Hannah Szenes Street").phone("0527644444").build())
                .roleId(role2).companyId(company).auditData(auditData).build();
        User userEmp5 = User.builder().fullName("Ruti").password(passwordEncoder.encode("987654321"))
                .address(Address.builder().email("r0527694893@gmail.com").name("Joseph Trumpeldor Street").phone("0527655555").build())
                .roleId(role2).companyId(company).auditData(auditData).build();
        User userEmp6 = User.builder().fullName("Rachel").password(passwordEncoder.encode("987654321"))
                .address(Address.builder().email("racelblau@gmail.com").name("Moshe Arens Street").phone("0527666666").build())
                .roleId(role2).companyId(company).auditData(auditData).build();
        User userEmp7 = User.builder().fullName("Shulamit").password(passwordEncoder.encode("987654321"))
                .address(Address.builder().email("s0583287870@gmail.com").name("Yair Stern Street").phone("0527666666").build())
                .roleId(role2).companyId(company).auditData(auditData).build();
        users.add(userEmp1);
        users.add(userEmp2);
        users.add(userEmp3);
        users.add(userEmp4);
        users.add(userEmp5);
        users.add(userEmp6);
        users.add(userEmp7);

        userRepository.saveAll(users);


        ProductCategory productCategory1 = ProductCategory.builder().name("Advertising products and gifts").desc("Advertising products and branded gifts").companyId(company).auditData(auditData).build();
        ProductCategory productCategory2 = ProductCategory.builder().name("Printing notebooks").desc("Printing notebooks").companyId(company).auditData(auditData).build();
        ProductCategory productCategory3 = ProductCategory.builder().name("Stamps").desc("Stamps for business at unbeatable prices").companyId(company).auditData(auditData).build();
        ProductCategory productCategory4 = ProductCategory.builder().name("Printing flyers").desc("printing quality flyers at an unprecedented sale - fast delivery").companyId(company).auditData(auditData).build();
        ProductCategory productCategory5 = ProductCategory.builder().name("Letterhead").desc("your business also deserves official letterhead - and it's much cheaper than you thought...").companyId(company).auditData(auditData).build();
        productCategoryRepository.save(productCategory1);
        productCategoryRepository.save(productCategory2);
        productCategoryRepository.save(productCategory3);
        productCategoryRepository.save(productCategory4);
        productCategoryRepository.save(productCategory5);

        Product product1 = Product.builder().name("Invoice books no").desc("Invoice books no").price(100).discount(10)
                .discountType(DiscountType.FIXED_AMOUNT).productCategoryId(productCategory2).inventory(400).companyId(company).auditData(auditData).build();
        Product product2 = Product.builder().name("Receipt books").desc("Receipt books").price(200).discount(20)
                .discountType(DiscountType.PERCENTAGE).productCategoryId(productCategory2).inventory(420).companyId(company).auditData(auditData).build();
        Product product3 = Product.builder().name("Shipping certificate registers").desc("Shipping certificate registers").price(250).discount(40)
                .discountType(DiscountType.FIXED_AMOUNT).productCategoryId(productCategory2).inventory(100).companyId(company).auditData(auditData).build();

        Product product4 = Product.builder().name("Engraving on branded pens").desc("Engraving on branded pens").price(300).discount(25)
                .discountType(DiscountType.PERCENTAGE).productCategoryId(productCategory1).inventory(200).companyId(company).auditData(auditData).build();

        Product product5 = Product.builder().name("special stamps").desc("special stamps").price(850).discount(50)
                .discountType(DiscountType.FIXED_AMOUNT).productCategoryId(productCategory3).inventory(100).companyId(company).auditData(auditData).build();
        Product product6 = Product.builder().name("Standard stamp").desc("Standard stamp").price(250).discount(0)
                .discountType(DiscountType.PERCENTAGE).productCategoryId(productCategory3).inventory(60).companyId(company).auditData(auditData).build();

        Product product7 = Product.builder().name("Printing on glass").desc("Printing on glass").price(50).discount(10)
                .discountType(DiscountType.PERCENTAGE).productCategoryId(productCategory1).inventory(50).companyId(company).auditData(auditData).build();
        Product product8 = Product.builder().name("Samson signs").desc("Samson signs").price(160).discount(30)
                .discountType(DiscountType.FIXED_AMOUNT).productCategoryId(productCategory1).inventory(150).companyId(company).auditData(auditData).build();

        Product product9 = Product.builder().name("Interior and exterior signage").desc("Interior and exterior signage").price(500).discount(20)
                .discountType(DiscountType.PERCENTAGE).productCategoryId(productCategory4).inventory(30).companyId(company).auditData(auditData).build();

        Product product10 = Product.builder().name("Logo pages and envelopes").desc("Logo pages and envelopes").price(630).discount(50)
                .discountType(DiscountType.PERCENTAGE).productCategoryId(productCategory3).inventory(80).companyId(company).auditData(auditData).build();
        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
        productRepository.save(product4);
        productRepository.save(product5);
        productRepository.save(product6);
        productRepository.save(product7);
        productRepository.save(product8);
        productRepository.save(product9);
        productRepository.save(product10);


        List<OrderItem> orderItemList1 = new ArrayList<>();
        orderItemList1.add(OrderItem.builder().productId(product10).amount(315).quantity(1).build());
        Order order1 = Order.builder().employeeId(userEmp1).customerId(userCust1)
                .orderItemsList(orderItemList1)
                .totalAmount(315).orderStatus(OrderStatus.APPROVED).companyId(company).currency(Currency.$)
                .creditCardNumber("1111222233334444").expireOn("12/26").cvc(123).notificationFlag(false).auditData(auditData).build();
        orderRepository.save(order1);

        List<OrderItem> orderItemList2 = new ArrayList<>();
        orderItemList2.add(OrderItem.builder().productId(product1).amount(330.84).quantity(1).build());
        orderItemList2.add(OrderItem.builder().productId(product2).amount(1176.32).quantity(2).build());
        Order order2 = Order.builder().employeeId(userEmp2).customerId(userCust1)
                .orderItemsList(orderItemList2)
                .totalAmount(1507.16).orderStatus(OrderStatus.DELIVERED).companyId(company).currency(Currency.₪)
                .creditCardNumber("1111222233334444").expireOn("12/26").cvc(123).notificationFlag(false).auditData(auditData).build();
        orderRepository.save(order2);

        List<OrderItem> orderItemList3 = new ArrayList<>();
        orderItemList3.add(OrderItem.builder().productId(product7).amount(118.395).quantity(3).build());
        Order order3 = Order.builder().employeeId(userEmp2).customerId(userCust1)
                .orderItemsList(orderItemList3)
                .totalAmount(118.395).orderStatus(OrderStatus.PACKING).companyId(company).currency(Currency.Fr)
                .creditCardNumber("1111222233334444").expireOn("12/26").cvc(123).notificationFlag(false).auditData(auditData).build();
        orderRepository.save(order3);

        List<OrderItem> orderItemList4 = new ArrayList<>();
        orderItemList4.add(OrderItem.builder().productId(product8).amount(118.43).quantity(1).build());
        orderItemList4.add(OrderItem.builder().productId(product9).amount(1093.2).quantity(3).build());
        Order order4 = Order.builder().employeeId(userEmp2).customerId(userCust3)
                .orderItemsList(orderItemList4)
                .totalAmount(1211.63).orderStatus(OrderStatus.APPROVED).companyId(company).currency(Currency.€)
                .creditCardNumber("1111222233334444").expireOn("12/26").cvc(123).notificationFlag(false).auditData(auditData).build();
        orderRepository.save(order4);

        List<OrderItem> orderItemList5 = new ArrayList<>();
        orderItemList5.add(OrderItem.builder().productId(product10).amount(1260).quantity(4).build());
        orderItemList5.add(OrderItem.builder().productId(product3).amount(210).quantity(1).build());
        orderItemList5.add(OrderItem.builder().productId(product2).amount(160).quantity(1).build());
        Order order5 = Order.builder().employeeId(userEmp1).customerId(userCust2)
                .orderItemsList(orderItemList5)
                .totalAmount(1630).orderStatus(OrderStatus.PACKING).companyId(company).currency(Currency.$)
                .creditCardNumber("1111222233334444").expireOn("12/26").cvc(123).notificationFlag(false).auditData(auditData).build();
        orderRepository.save(order5);

        List<OrderItem> orderItemList6 = new ArrayList<>();
        orderItemList6.add(OrderItem.builder().productId(product6).amount(500).quantity(2).build());
        Order order6 = Order.builder().employeeId(userEmp5).customerId(userCust1)
                .orderItemsList(orderItemList6)
                .totalAmount(500).orderStatus(OrderStatus.APPROVED).companyId(company).currency(Currency.$)
                .creditCardNumber("1111222233334444").expireOn("12/26").cvc(123).notificationFlag(false).auditData(auditData).build();
        orderRepository.save(order6);

        List<OrderItem> orderItemList7 = new ArrayList<>();
        orderItemList7.add(OrderItem.builder().productId(product9).amount(800).quantity(2).build());
        Order order7 = Order.builder().employeeId(userEmp4).customerId(userCust4)
                .orderItemsList(orderItemList7)
                .totalAmount(800).orderStatus(OrderStatus.APPROVED).companyId(company).currency(Currency.$)
                .creditCardNumber("1111222233334444").expireOn("12/26").cvc(123).notificationFlag(false).auditData(auditData).build();
        orderRepository.save(order7);

        List<OrderItem> orderItemList8 = new ArrayList<>();
        orderItemList8.add(OrderItem.builder().productId(product5).amount(1600).quantity(2).build());
        Order order8 = Order.builder().employeeId(userEmp1).customerId(userCust5)
                .orderItemsList(orderItemList8)
                .totalAmount(1600).orderStatus(OrderStatus.APPROVED).companyId(company).currency(Currency.$)
                .creditCardNumber("1111222233334444").expireOn("12/26").cvc(123).notificationFlag(false).auditData(AuditData.builder().createDate(LocalDate.of(2023, 5, 1)).updateDate(LocalDate.now()).build()).build();
        orderRepository.save(order8);

        List<OrderItem> orderItemList9 = new ArrayList<>();
        orderItemList9.add(OrderItem.builder().productId(product6).amount(500).quantity(2).build());
        Order order9 = Order.builder().employeeId(userEmp3).customerId(userCust5)
                .orderItemsList(orderItemList9)
                .totalAmount(500).orderStatus(OrderStatus.CANCELLED).companyId(company).currency(Currency.$)
                .creditCardNumber("1111222233334444").expireOn("12/26").cvc(123).notificationFlag(false).auditData(AuditData.builder().createDate(LocalDate.of(2023, 6, 1)).updateDate(LocalDate.now()).build()).build();
        orderRepository.save(order9);

        List<OrderItem> orderItemList10 = new ArrayList<>();
        orderItemList10.add(OrderItem.builder().productId(product8).amount(130).quantity(1).build());
        Order order10 = Order.builder().employeeId(userEmp1).customerId(userCust7)
                .orderItemsList(orderItemList10)
                .totalAmount(130).orderStatus(OrderStatus.DELIVERED).companyId(company).currency(Currency.$)
                .creditCardNumber("1111222233334444").expireOn("12/26").cvc(123).notificationFlag(false).auditData(AuditData.builder().createDate(LocalDate.of(2023, 7, 1)).updateDate(LocalDate.now()).build()).build();
        orderRepository.save(order10);

        List<OrderItem> orderItemList11 = new ArrayList<>();
        orderItemList11.add(OrderItem.builder().productId(product8).amount(130).quantity(1).build());
        Order order11 = Order.builder().employeeId(userEmp1).customerId(userCust7)
                .orderItemsList(orderItemList11)
                .totalAmount(130).orderStatus(OrderStatus.CANCELLED).companyId(company).currency(Currency.$)
                .creditCardNumber("1111222233334444").expireOn("12/26").cvc(123).notificationFlag(false).auditData(AuditData.builder().createDate(LocalDate.of(2023, 7, 1)).updateDate(LocalDate.now()).build()).build();
        orderRepository.save(order11);

        List<OrderItem> orderItemList12 = new ArrayList<>();
        orderItemList12.add(OrderItem.builder().productId(product5).amount(1600).quantity(2).build());
        Order order12 = Order.builder().employeeId(userEmp1).customerId(userCust5)
                .orderItemsList(orderItemList12)
                .totalAmount(1600).orderStatus(OrderStatus.APPROVED).companyId(company).currency(Currency.$)
                .creditCardNumber("1111222233334444").expireOn("12/26").cvc(123).notificationFlag(false).auditData(AuditData.builder().createDate(LocalDate.of(2023, 5, 1)).updateDate(LocalDate.now()).build()).build();
        orderRepository.save(order12);

        return "filled";
    }
}
