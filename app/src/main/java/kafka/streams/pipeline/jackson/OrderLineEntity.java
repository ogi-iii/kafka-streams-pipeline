package kafka.streams.pipeline.jackson;

import lombok.Data;

/**
 * json entity for the order line
 */
@Data
public class OrderLineEntity {
    public int product_id;
    public String category;
    public int quantity;
    public double unit_price;
    public double net_price;
}
