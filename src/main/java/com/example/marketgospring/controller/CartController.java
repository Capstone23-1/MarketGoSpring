package com.example.marketgospring.controller;

import com.example.marketgospring.entity.Cart;
import com.example.marketgospring.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping(value="/cart")
public class CartController {
    private CartRepository cartRepository;

    @Autowired
    public CartController(CartRepository cartRepository) {
        this.cartRepository=cartRepository;
    }

    @GetMapping(value = "/all")
    public Iterable<Cart> list() {
        return cartRepository.findAll();
    }
    @GetMapping(value = "/{cartId}")
    public Optional<Cart> pickOne(@PathVariable("cartId") Long cartId) {
        return cartRepository.findById(cartId);
    }
    @PostMapping
    public Cart put(@RequestParam("goodsId1") Long goodsId1, @RequestParam("goodsId2") Long goodsId2, @RequestParam("goodsId3") Long goodsId3, @RequestParam("goodsId4") Long goodsId4, @RequestParam("goodsId5") Long goodsId5, @RequestParam("goodsId6") Long goodsId6, @RequestParam("goodsId7") Long goodsId7, @RequestParam("goodsId8") Long goodsId8, @RequestParam("goodsId9") Long goodsId9, @RequestParam("goodsId10") Long goodsId10) {
        final Cart cart=Cart.builder()
                .cartDate(LocalDateTime.now())
                .goodsId1(goodsId1)
                .goodsId2(goodsId2)
                .goodsId3(goodsId3)
                .goodsId4(goodsId4)
                .goodsId5(goodsId5)
                .goodsId6(goodsId6)
                .goodsId7(goodsId7)
                .goodsId8(goodsId8)
                .goodsId9(goodsId9)
                .goodsId10(goodsId10)
                .build();
        return cartRepository.save(cart);
    }
    @PutMapping(value = "/{cartId}")
    public Cart update(@PathVariable ("cartId") Long cartId, @RequestParam("goodsId1") Long goodsId1, @RequestParam("goodsId2") Long goodsId2, @RequestParam("goodsId3") Long goodsId3, @RequestParam("goodsId4") Long goodsId4, @RequestParam("goodsId5") Long goodsId5, @RequestParam("goodsId6") Long goodsId6, @RequestParam("goodsId7") Long goodsId7, @RequestParam("goodsId8") Long goodsId8, @RequestParam("goodsId9") Long goodsId9, @RequestParam("goodsId10") Long goodsId10) {
        Optional<Cart> cart=cartRepository.findById(cartId);
        cart.get().setCartDate(LocalDateTime.now());
        cart.get().setGoodsId1(goodsId1);
        cart.get().setGoodsId2(goodsId2);
        cart.get().setGoodsId3(goodsId3);
        cart.get().setGoodsId4(goodsId4);
        cart.get().setGoodsId5(goodsId5);
        cart.get().setGoodsId6(goodsId6);
        cart.get().setGoodsId7(goodsId7);
        cart.get().setGoodsId8(goodsId8);
        cart.get().setGoodsId9(goodsId9);
        cart.get().setGoodsId10(goodsId10);
        return cartRepository.save(cart.get());
    }
    @DeleteMapping
    public void deleteCart(@RequestParam("cartId")Long cartId) {
        cartRepository.deleteById(cartId);
    }
}
