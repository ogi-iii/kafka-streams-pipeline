package kafka.streams.pipeline.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class OrderLineEntityTest {
    private final OrderLineEntity orderLineEntity = new OrderLineEntity();

    @Test
    void testSetGetProductId() {
        var product_id = 10;
        this.orderLineEntity.setProduct_id(product_id);
        assertEquals(
            product_id,
            this.orderLineEntity.getProduct_id());
    }

    @Test
    void testSetGetCategory() {
        var category = "test";
        this.orderLineEntity.setCategory(category);
        assertEquals(
            category,
            this.orderLineEntity.getCategory());
    }

    @Test
    void testSetGetQuantity() {
        var quantity = 5;
        this.orderLineEntity.setQuantity(quantity);
        assertEquals(
            quantity,
            this.orderLineEntity.getQuantity());
    }

    @Test
    void testSetGetUnitPrice() {
        var unit_price = 10.01;
        this.orderLineEntity.setUnit_price(unit_price);
        assertEquals(
            unit_price,
            this.orderLineEntity.getUnit_price());
    }

    @Test
    void testSetGetNetPrice() {
        var net_price = 50.05;
        this.orderLineEntity.setNet_price(net_price);
        assertEquals(
            net_price,
            this.orderLineEntity.getNet_price());
    }
}
