package kafka.streams.pipeline.jackson;

import java.util.List;

import lombok.Data;

/**
 * json entity for the pizza order
 */
@Data
public class PizzaOrderEntity {
    public int store_id;
    public int store_order_id;
    public int coupon_code;
    public int date;
    public String status;
    public List<OrderLineEntity> order_lines;
}
