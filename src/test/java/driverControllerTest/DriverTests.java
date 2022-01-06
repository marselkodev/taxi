package driverControllerTest;

import model.DriverDefault;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.taxi.controller.DriverController;
import ru.taxi.dto.DriverGetAllResponseDTO;
import ru.taxi.manager.DriverManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DriverTests {

    @InjectMocks
    DriverController driverController;

    @Mock
    DriverManager driverManager;

    @Test
    @DisplayName("Получение всех водителей")
    void DriverGetAllResponseDTOTest() {
        when(driverManager.getAll()).thenReturn(DriverDefault.get());

        DriverGetAllResponseDTO result = driverController.getAll();

        assertNotNull(result);
    }

}
