package com.example.cardatabase;

import com.example.cardatabase.domain.CarRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarControllerTest.class)
public class CarControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarRepository carRepository;

    @Test
    @DisplayName("GET / cars 요청 테스트 검증")
    void testGetCars() throws Exception {
        this.mockMvc.perform(get("/cars")).andExpect(status().isOk());
    }
}
