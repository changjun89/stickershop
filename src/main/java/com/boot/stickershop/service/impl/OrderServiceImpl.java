package com.boot.stickershop.service.impl;

import com.boot.stickershop.domain.BasketProduct;
import com.boot.stickershop.domain.Order;
import com.boot.stickershop.domain.User;
import com.boot.stickershop.dto.OrderSearch;
import com.boot.stickershop.repository.BasketProductRepository;
import com.boot.stickershop.repository.OrderProductRepository;
import com.boot.stickershop.repository.OrderRepository;
import com.boot.stickershop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderProductRepository orderProductRepository;

    @Autowired
    BasketProductRepository basketProductRepository;

    @Override
    public void addBasket(BasketProduct basketProduct) {
        basketProductRepository.save(basketProduct);
    }

    @Override
    public BasketProduct getBasketProduct(Long userId, Long productId) {
        return basketProductRepository.findBasketProductByUserIdAndProductId(userId, productId);
    }

    @Override
    public void updateBasketProduct(BasketProduct basketProduct) {
        basketProductRepository.saveAndFlush(basketProduct);
    }

    @Override
    public List<BasketProduct> getBasket(Long id) {
        return basketProductRepository.findAllByUserId(id);
    }

    @Override
    public Page<Order> getOrderList(OrderSearch orderSearch) {
        PageRequest pageRequest = null;

        if("RECENT".equals(orderSearch.getSort().toUpperCase())){
            pageRequest = PageRequest.of(orderSearch.getPage() - 1, 10, new Sort(Sort.Direction.DESC, "regtime"));
        }else if("OLD".equals(orderSearch.getSort().toUpperCase())){
            pageRequest = PageRequest.of(orderSearch.getPage() - 1, 10, new Sort(Sort.Direction.ASC, "regtime"));
        }
        return orderRepository.getOrdersByDSL(orderSearch, pageRequest);
    }
}