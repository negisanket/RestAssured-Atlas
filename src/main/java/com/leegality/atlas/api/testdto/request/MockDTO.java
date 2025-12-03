package com.leegality.atlas.api.testdto.request;



import lombok.Data;
import java.util.List;

@Data
public class MockDTO {

    private String status;
    private String timestamp;
    private User user;
    private List<Order> orders;
    private Meta meta;

    @Data
    public static class User {
        private int id;
        private String name;
        private String email;
        private List<String> roles;
        private Preferences preferences;
        private List<Address> addresses;
    }

    @Data
    public static class Preferences {
        private String theme;
        private Notifications notifications;
    }

    @Data
    public static class Notifications {
        private boolean email;
        private boolean sms;
        private boolean push;
    }

    @Data
    public static class Address {
        private String type;
        private String city;
        private int zip;
        private boolean primary;
    }

    @Data
    public static class Order {
        private String orderId;
        private double amount;
        private String currency;
        private List<Item> items;
        private boolean delivered;
    }

    @Data
    public static class Item {
        private String name;
        private int qty;
    }

    @Data
    public static class Meta {
        private int totalOrders;
        private int loyaltyPoints;
        private String lastLogin;
    }
}