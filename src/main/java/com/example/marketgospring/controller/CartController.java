package com.example.marketgospring.controller;

import com.example.marketgospring.entity.Cart;
import com.example.marketgospring.entity.Goods;
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
    public Optional<Cart> pickOne(@PathVariable("cartId") Integer cartId) {
        return cartRepository.findById(cartId);
    }
    @PostMapping
    public Cart put(@RequestParam("goodsId1") Goods goodsId1, @RequestParam("goodsId2") Goods goodsId2, @RequestParam("goodsId3") Goods goodsId3, @RequestParam("goodsId4") Goods goodsId4, @RequestParam("goodsId5") Goods goodsId5, @RequestParam("goodsId6") Goods goodsId6, @RequestParam("goodsId7") Goods goodsId7, @RequestParam("goodsId8") Goods goodsId8, @RequestParam("goodsId9") Goods goodsId9, @RequestParam("goodsId10") Goods goodsId10, @RequestParam("unit1") Integer unit1, @RequestParam("unit2") Integer unit2, @RequestParam("unit3") Integer unit3, @RequestParam("unit4") Integer unit4, @RequestParam("unit5") Integer unit5, @RequestParam("unit6") Integer unit6, @RequestParam("unit7") Integer unit7, @RequestParam("unit8") Integer unit8, @RequestParam("unit9") Integer unit9, @RequestParam("unit10") Integer unit10) {
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
                .unit1(unit1)
                .unit2(unit2)
                .unit3(unit3)
                .unit4(unit4)
                .unit5(unit5)
                .unit6(unit6)
                .unit7(unit7)
                .unit8(unit8)
                .unit9(unit9)
                .unit10(unit10)
                .build();
        return cartRepository.save(cart);
    }
    @PutMapping(value = "/{cartId}")
    public Cart update(@PathVariable ("cartId") Integer cartId, @RequestParam("goodsId1") Goods goodsId1, @RequestParam("goodsId2") Goods goodsId2, @RequestParam("goodsId3") Goods goodsId3, @RequestParam("goodsId4") Goods goodsId4, @RequestParam("goodsId5") Goods goodsId5, @RequestParam("goodsId6") Goods goodsId6, @RequestParam("goodsId7") Goods goodsId7, @RequestParam("goodsId8") Goods goodsId8, @RequestParam("goodsId9") Goods goodsId9, @RequestParam("goodsId10") Goods goodsId10, @RequestParam("unit1") Integer unit1, @RequestParam("unit2") Integer unit2, @RequestParam("unit3") Integer unit3, @RequestParam("unit4") Integer unit4, @RequestParam("unit5") Integer unit5, @RequestParam("unit6") Integer unit6, @RequestParam("unit7") Integer unit7, @RequestParam("unit8") Integer unit8, @RequestParam("unit9") Integer unit9, @RequestParam("unit10") Integer unit10) {
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
        cart.get().setUnit1(unit1);
        cart.get().setUnit2(unit2);
        cart.get().setUnit3(unit3);
        cart.get().setUnit4(unit4);
        cart.get().setUnit5(unit5);
        cart.get().setUnit6(unit6);
        cart.get().setUnit7(unit7);
        cart.get().setUnit8(unit8);
        cart.get().setUnit9(unit9);
        cart.get().setUnit10(unit10);
        return cartRepository.save(cart.get());
    }
    @DeleteMapping
    public void deleteCart(@RequestParam("cartId")Integer cartId) {
        cartRepository.deleteById(cartId);
    }
}
