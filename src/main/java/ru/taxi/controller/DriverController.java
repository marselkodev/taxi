package ru.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.taxi.dto.DriverSaveRequestDTO;
import ru.taxi.dto.DriverGetAllResponseDTO;
import ru.taxi.dto.DriverGetByIdResponseDTO;
import ru.taxi.dto.DriverSaveResponseDTO;
import ru.taxi.manager.DriverManager;


@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor
public class DriverController {
    private final DriverManager manager;

    @GetMapping("/getAll")
    public DriverGetAllResponseDTO getAll(){
        return manager.getAll();
    }

    @GetMapping("/getById")
    public DriverGetByIdResponseDTO getById(@RequestParam long id){
        return manager.getById(id);
    }

    @PostMapping("/save")
    public DriverSaveResponseDTO save(@RequestBody DriverSaveRequestDTO requestDTO){
        return manager.save(requestDTO);
    }

}
