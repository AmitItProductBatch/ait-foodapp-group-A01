package com.ait.app.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ait.app.dto.ItemPriceResponseDto;
import com.ait.app.exception.MenuItemServiceException;
import com.ait.app.model.MenuItem;
import com.ait.app.repository.MenuItemRepository;
import com.ait.app.service.ItemPriceService;

@Service
public class ItemPriceServiceImpl implements ItemPriceService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Override
    public ItemPriceResponseDto getItemPrice(Long itemId) {
        MenuItem item = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new MenuItemServiceException(
                        HttpStatus.NOT_FOUND, 
                        "Food item not found with id: " + itemId
                ));

        return new ItemPriceResponseDto(
                item.getId(),
                item.getName(),
                item.getFullPrice(),
                item.getHalfPrice(),
                item.isAvailable()
        );
    }
}
