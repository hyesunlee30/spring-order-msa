package com.encore.ordering.order.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class OrderReqDto {
    private Long itemId;
    private int count;
//    private List<OrderItemDto> orderItems;
//    @Data
//    public static class OrderItemDto{
//        private Long itemId;
//        private int count;
//    }
}
