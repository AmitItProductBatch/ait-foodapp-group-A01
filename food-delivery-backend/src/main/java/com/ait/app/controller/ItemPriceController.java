package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.dto.ItemPriceResponseDto;
import com.ait.app.service.ItemPriceService;

@RestController
@RequestMapping("/api/prices")
public class ItemPriceController {

    @Autowired
    private ItemPriceService itemPriceService;

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemPriceResponseDto> getPriceByItemId(@PathVariable Long itemId) {
        ItemPriceResponseDto priceDto = itemPriceService.getItemPrice(itemId);
        return new ResponseEntity<>(priceDto, HttpStatus.OK);
    }
}
