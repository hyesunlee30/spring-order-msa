package com.encore.ordering.order.domain;

import com.encore.ordering.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class OrderItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Ordering ordering;



    @JoinColumn(nullable = false)
    private Long itemId;


//    public void cancel() {
//        // 재고수량 복구
//        getItem().addStockQuantity(this.quantity);
//    }

}
