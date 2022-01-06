package ru.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.taxi.dto.*;
import ru.taxi.manager.DriverManager;

@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor
public class DriverController {
    private final DriverManager manager;

    @GetMapping("/getAll")
    public DriverGetAllResponseDTO getAll() {
        return manager.getAll();
    }

    @GetMapping("/getById")
    public DriverGetByIdResponseDTO getById(@RequestParam long id) {
        return manager.getById(id);
    }

    @PostMapping("/save")
    public DriverSaveResponseDTO save(@RequestBody DriverSaveRequestDTO requestDTO) {
        return manager.save(requestDTO);
    }

    @PostMapping("/removeById")
    public void removeById(@RequestParam long id) {
        manager.removeById(id);
    }

    @GetMapping("/getAccept")
    public OrderGetAcceptResponseDTO getAccept() {
        return manager.getAccept();
    }

    @PostMapping("/acceptDriver")
    public DriverGetByIdResponseDTO acceptDriver(@RequestParam long id, @RequestParam long driverId) {
        return manager.acceptDriver(id, driverId);
    }

    @PostMapping("/completeOrder")
    public void completeOrder(@RequestParam long id) {
        manager.completeOrder(id);
    }

    @GetMapping("/getStatsById")
    public StatsGetByIdResponseDTO getStatsById(
            @RequestParam long id,
            @RequestParam(required = false, defaultValue = "false") boolean saveToXml) {
        return manager.getStatsById(id, saveToXml);
    }
}
