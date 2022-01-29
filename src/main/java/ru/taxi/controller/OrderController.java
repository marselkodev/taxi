package ru.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.taxi.dto.OrderSaveRequestDTO;
import ru.taxi.dto.OrderSaveResponseDTO;
import ru.taxi.manager.OrderManager;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderManager manager;

    @PostMapping("/create")
    public OrderSaveResponseDTO create(@RequestBody OrderSaveRequestDTO requestDTO) {
        return manager.create(requestDTO);
    }

    @PostMapping("/accept")
    public void accept(@RequestParam long id) {
        manager.accept(id);
    }
}
