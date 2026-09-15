package com.ait.app.service;

import com.ait.app.dto.ItemPriceResponseDto;

public interface ItemPriceService {
    ItemPriceResponseDto getItemPrice(Long itemId);
}
