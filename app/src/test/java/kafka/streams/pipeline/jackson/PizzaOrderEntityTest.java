package kafka.streams.pipeline.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class PizzaOrderEntityTest {
    private final PizzaOrderEntity pizzaOrderEntity = new PizzaOrderEntity();

    @Test
    void testSetGetStoreId() {
        var store_id = 99;
        this.pizzaOrderEntity.setStore_id(store_id);
        assertEquals(
            store_id,
            this.pizzaOrderEntity.getStore_id());
    }

    @Test
    void testSetGetStoreOrderId() {
        var store_order_id = 11;
        this.pizzaOrderEntity.setStore_order_id(store_order_id);
        assertEquals(
            store_order_id,
            this.pizzaOrderEntity.getStore_order_id());
    }

    @Test
    void testSetGetCouponCode() {
        var coupon_code = 999;
        this.pizzaOrderEntity.setCoupon_code(coupon_code);
        assertEquals(
            coupon_code,
            this.pizzaOrderEntity.getCoupon_code());
    }

    @Test
    void testSetGetDate() {
        var date = 111;
        this.pizzaOrderEntity.setDate(date);
        assertEquals(
            date,
            this.pizzaOrderEntity.getDate());
    }

    @Test
    void testSetGetStatus() {
        var status = "accepted";
        this.pizzaOrderEntity.setStatus(status);
        assertEquals(
            status,
            this.pizzaOrderEntity.getStatus());
    }

    @Test
    void testSetGetStoreOrderLines() {
        var product_id = 9;

        var orderLineEntity = new OrderLineEntity();
        orderLineEntity.setProduct_id(product_id);

        var order_lines = new ArrayList<>(Arrays.asList(orderLineEntity));
        this.pizzaOrderEntity.setOrder_lines(order_lines);
        assertEquals(
            order_lines,
            this.pizzaOrderEntity.getOrder_lines());
    }
}
