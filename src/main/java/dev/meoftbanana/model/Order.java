package dev.meoftbanana.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private int orderId;
    private String timeCreated;
    private String orderStatus;
    private Double total;
   
}
