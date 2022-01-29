package ru.taxi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.taxi.Main;

import java.io.File;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
class TaxiTest {
    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Container
    private final DockerComposeContainer<?> composeContainer = new DockerComposeContainer<>(
            new File("docker-compose.yml"))
            .withLocalCompose(true)
            .withExposedService("db", 5432, Wait.forListeningPort());

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldPerformManager() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/drivers/getAll"))
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

        mockMvc.perform(MockMvcRequestBuilders.get("/drivers/getById")
                .queryParam("id", String.valueOf(3))).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.content().json(
                        // language=JSON
                        """ 
                                {
                                  "driver": {
                                    "id": 3,
                                    "name": "Erevan",
                                    "phoneNumber": "87567461285",
                                    "photoUrl": "photoErevan.png",
                                    "rating": 4.699999809265137,
                                    "carName": "VW polo",
                                    "carNumber": "b 353 py 799",
                                    "carColor": "black"
                                  }
                                }
                                """
                )
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/drivers/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                // language=JSON
                                """
                                        {
                                          "id": 0,
                                          "name": "Maks",
                                          "phoneNumber": "88005697859",
                                          "photoUrl": "photo.png",
                                          "carName": "Skoda Rapid",
                                          "carNumber": "X 407 AB 116",
                                          "carColor": "orange"
                                        }
                                        """
                        ))
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                //language=JSON
                                """
                                           {
                                           "driver": {
                                           "id": 4,
                                           "name": "Maks",
                                           "phoneNumber": "88005697859",
                                           "photoUrl": "photo.png",
                                           "carName": "Skoda Rapid",
                                           "carNumber": "X 407 AB 116",
                                           "carColor": "orange"
                                         }
                                        }
                                        """
                        )
                );

        mockMvc.perform(MockMvcRequestBuilders.post("/drivers/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                // language=JSON
                                """
                                        {
                                          "id": 4,
                                          "phoneNumber": "89045687910"
                                        }
                                        """
                        ))
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                //language=JSON
                                """
                                           {
                                           "driver": {
                                           "id": 4,
                                           "name": "Maks",
                                           "phoneNumber": "89045687910",
                                           "photoUrl": "photo.png",
                                           "carName": "Skoda Rapid",
                                           "carNumber": "X 407 AB 116",
                                           "carColor": "orange"
                                         }
                                        }
                                        """
                        )
                );

        var connection = DriverManager.getConnection(dbUrl);
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery("select removed from drivers where id = 4")) {
            if (rs.next()) {
                assertFalse(rs.getBoolean(1));
            }
        }
        mockMvc.perform(MockMvcRequestBuilders.post("/drivers/removeById/").param("id", "4"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery("select removed from drivers where id = 4")) {
            if (rs.next()) {
                assertTrue(rs.getBoolean(1));
            }
            connection.close();
        }

        mockMvc.perform(MockMvcRequestBuilders.post("/orders/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        // language=JSON
                        """
                                        {
                                          "addressFrom": "Пушкина 17, Казань",
                                          "addressTo": "Мавлютова 17",
                                          "babyChair": true
                                        }
                                """
                )
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.content().json(
                        //language=JSON
                        """
                                {
                                  "order": {
                                    "id": 1,
                                    "addressFrom": "Пушкина 17, Казань",
                                    "addressTo": "Мавлютова 17",
                                    "driverComment": "No comment",
                                    "babyChair": true,
                                    "distance": 8.437,
                                    "duration": 12.416666666666666,
                                    "price": 153.183,
                                    "accept": false
                                  }
                                }
                                """
                )
        );

        connection = DriverManager.getConnection(dbUrl);
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery("select accept from orders where id = 1")) {
            if (rs.next()) {
                assertFalse(rs.getBoolean(1));
            }
        }
        mockMvc.perform(MockMvcRequestBuilders.post("/orders/accept/").param("id", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery("select accept from orders where id = 1")) {
            if (rs.next()) {
                assertTrue(rs.getBoolean(1));
            }
            connection.close();
        }

        mockMvc.perform(MockMvcRequestBuilders.get("/drivers/getAccept"))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                //language=JSON
                                """
                                        {
                                          "orders": [
                                          {
                                              "id": 1,
                                              "addressFrom": "Пушкина 17, Казань",
                                              "addressTo": "Мавлютова 17",
                                              "driverComment": "No comment",
                                              "babyChair": true,
                                              "choiceOfCarClass": 1,
                                              "distance": 8.437,
                                              "duration": 12.416666666666666,
                                              "price": 153.183,
                                              "accept": true
                                            }
                                            ]
                                        }
                                        """
                        )
                );

        mockMvc.perform(MockMvcRequestBuilders.post("/drivers/acceptDriver")
                .queryParam("id", String.valueOf(1))
                .queryParam("driverId", String.valueOf(2))
        ).andExpectAll(MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.content().json(
                        //language=JSON
                        """
                                {
                                  "driver": {
                                    "id": 2,
                                    "name": "Umajon",
                                    "phoneNumber": "88004521935",
                                    "photoUrl": "photoUmajon.png",
                                    "rating": 4.96999979019165,
                                    "carName": "Kia k5",
                                    "carNumber": "x 425 pa 777",
                                    "carColor": "yellow"
                                  }
                                }
                                """
                )
        );
        connection = DriverManager.getConnection(dbUrl);
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery("select accept from orders where id = 1")) {
            if (rs.next()) {
                assertTrue(rs.getBoolean(1));
            }
            connection.close();
        }
        mockMvc.perform(MockMvcRequestBuilders.post("/drivers/completeOrder")
                .queryParam("id", String.valueOf(1))
        ).andExpect(MockMvcResultMatchers.status().isOk());
        connection = DriverManager.getConnection(dbUrl);
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery("select complete_order from orders where id = 1")) {
            if (rs.next()) {
                assertTrue(rs.getBoolean(1));
            }
            connection.close();

            mockMvc.perform(MockMvcRequestBuilders.get("/drivers/getStatsById")
                            .queryParam("id", String.valueOf(2)))
                    .andExpectAll(MockMvcResultMatchers.status().isOk(),
                            MockMvcResultMatchers.content().json(
                                    //language=JSON
                                    """
                                            {
                                              "countOrders": 1,
                                              "countWithBabyChair": 1,
                                              "countWithoutBabyChair": 0,
                                              "countDistance": 8.437,
                                              "countDuration": 12.416666666666666,
                                              "countPrice": 153.183
                                            }
                                            """
                            )
                    );
        }
    }

    @Test
    void shouldPerformFailManager() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/drivers/getById")
                        .queryParam("id", String.valueOf(0)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders.get("/drivers/getById")
                        .queryParam("id", String.valueOf(999)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders.post("/drivers/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                // language=JSON
                                """
                                        {
                                          "id": 5,
                                          "phoneNumber": "89045687910"
                                        }
                                        """
                        ))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders.post("/drivers/removeById")
                        .queryParam("id", String.valueOf(0)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders.post("/drivers/removeById")
                        .queryParam("id", String.valueOf(9)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders.post("/orders/accept")
                        .queryParam("id", String.valueOf(0)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders.post("/orders/accept")
                        .queryParam("id", String.valueOf(999)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders.post("/drivers/acceptDriver")
                        .queryParam("id", String.valueOf(0))
                        .queryParam("driverId", String.valueOf(0)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders.post("/drivers/acceptDriver")
                        .queryParam("id", String.valueOf(999))
                        .queryParam("driverId", String.valueOf(999)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders.post("/drivers/completeOrder")
                        .queryParam("id", String.valueOf(0)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders.post("/drivers/completeOrder")
                        .queryParam("id", String.valueOf(999)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders.get("/drivers/getStatsById")
                        .queryParam("id", String.valueOf(0)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
