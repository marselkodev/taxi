package ru.taxi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Path;

@Testcontainers
@SpringBootTest
@DirtiesContext
@AutoConfigureMockMvc
class DriverControllerTest {
    @Container
    static DockerComposeContainer<?> compose = new DockerComposeContainer<>(
            Path.of("docker-compose.yml").toFile()
    );

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldPerformController() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/drivers/getAll")
                )
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                //language=JSON
                                """
                                        {
                                           "drivers": [
                                             {
                                               "id": 1,
                                               "name": "Ahmed",
                                               "phoneNumber": "88005736459",
                                               "photoUrl": "photoAhmed.png",
                                               "rating": 4.949999809265137,
                                               "carName": "Kia ceed",
                                               "carNumber": "x 751 xx 116",
                                               "carColor": "white"
                                             },
                                             {
                                               "id": 2,
                                               "name": "Umajon",
                                               "phoneNumber": "88004521935",
                                               "photoUrl": "photoUmajon.png",
                                               "rating": 4.96999979019165,
                                               "carName": "Kia k5",
                                               "carNumber": "x 425 pa 777",
                                               "carColor": "yellow"
                                             },
                                             {
                                               "id": 3,
                                               "name": "Erevan",
                                               "phoneNumber": "87567461285",
                                               "photoUrl": "photoErevan.png",
                                               "rating": 4.699999809265137,
                                               "carName": "VW polo",
                                               "carNumber": "b 353 py 799",
                                               "carColor": "black"
                                             }
                                           ]
                                         }
                                        """
                        )
                );
    }
}
