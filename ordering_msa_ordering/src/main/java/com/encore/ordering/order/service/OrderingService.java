package com.encore.ordering.order.service;


import com.encore.ordering.common.CommonResponse;
import com.encore.ordering.order.domain.Ordering;
import com.encore.ordering.order.dto.*;
import com.encore.ordering.order.repository.OrderingRepository;
import com.encore.ordering.order.domain.OrderItem;
import lombok.extern.slf4j.Slf4j;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class OrderingService {

    private final String ITEM_API = "http://item-service/";
    private final String MEMBER_API = "http://member-service/";
    private final RestTemplate restTemplate;
    private final OrderingRepository repository;


    public OrderingService(RestTemplate restTemplate, OrderingRepository repository) {
        this.restTemplate = restTemplate;
        this.repository = repository;

    }

    public Ordering create(List<OrderReqDto> orderReqDtos, String email) {

        String memberUrl =MEMBER_API + "member/findByEmail/"+email;
        ResponseEntity<MemberDto> memberEntity = restTemplate.getForEntity(memberUrl, MemberDto.class);
        MemberDto member = memberEntity.getBody();

        Ordering ordering = Ordering.builder()
                .memberId(member.getId())
                .build();

        for (OrderReqDto req : orderReqDtos) {
            OrderItem orderItem = OrderItem.builder()
                    .itemId(req.getItemId())
                    .quantity(req.getCount())
                    .ordering(ordering)
                    .build();
            ordering.getOrderItems().add(orderItem);
            String itemUrl =ITEM_API + "item/"+req.getItemId();
            ResponseEntity<ItemDto> itemDtoResponseEntity = restTemplate.getForEntity(itemUrl, ItemDto.class);
            System.out.println(itemDtoResponseEntity.getBody());
            if(itemDtoResponseEntity.getBody().getStockQuantity() - req.getCount() < 0) {
                throw new IllegalArgumentException("재고가 부족합니다.");
            }

            String itemPatchUrl = ITEM_API + "item/updateQuantity";
            int newQuantity = itemDtoResponseEntity.getBody().getStockQuantity() - req.getCount();
            ItemQuantityDto updateDto = new ItemQuantityDto();

            updateDto.setId(req.getItemId());
            updateDto.setStockQuantity(newQuantity);

            HttpEntity<ItemQuantityDto> entity = new HttpEntity<>(updateDto);
            ResponseEntity<CommonResponse> response = restTemplate.exchange(itemPatchUrl, HttpMethod.POST, entity, CommonResponse.class);

            System.out.println(response);
        }

        // 외부 API는 트랜잭션을 같이 타지 않아 롤백이 어려움
        //order - item 트랜잭션 묶기
        //전체 코드 try catch 예외처리 이후에 catch에서 updateRollbackQuantity 호출

        return repository.save(ordering);
    }

    public Ordering cancel(Long id) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        Ordering ordering = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("not found order"));
//        if (!ordering.getMember().getEmail().equals(email) && !authentication.getAuthorities().contains((new SimpleGrantedAuthority("ROLE_ADMIN")))) {
//            throw new AccessDeniedException("권한이 없습니다.");
//        }
//;
//        if(ordering.getOrderStatus().equals(OrderStatus.CANCELED)) {
//            throw new IllegalArgumentException("이미 취소된 주문입니다");
//        }
//        List<OrderItem> orderItems = ordering.getOrderItems();
//
//        ordering.cancelOrder();
//        for (OrderItem orderItem : orderItems) {
//            orderItem.getItem().updateStockQuantity(
//                    orderItem.getItem().getStockQuantity() + orderItem.getQuantity()
//            );
//
//            //orderItem.cancel();
//        }
        return null;
    }

    public List<OrderResDto> findAll(){
        List<Ordering> orderings = repository.findAll();
        return orderings.stream().map(OrderResDto::toDto).collect(Collectors.toList());
    }

    public List<OrderResDto> findMyOrders(Long memberId){
        List<Ordering> orderings = repository.findAllByMemberId(memberId);
        return orderings.stream().map(OrderResDto::toDto).collect(Collectors.toList());
    }
}
