package guru.springframework.brewery.web.controllers;

import guru.springframework.brewery.services.BeerOrderService;
import guru.springframework.brewery.web.model.BeerOrderDto;
import guru.springframework.brewery.web.model.BeerOrderPagedList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerOrderController.class)
class BeerOrderControllerTest {

    @MockBean
    private BeerOrderService service;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void listOrders() throws Exception {
        List<BeerOrderDto> orderList = List.of(new BeerOrderDto(),new BeerOrderDto());
        BeerOrderPagedList pagedList = new BeerOrderPagedList(orderList);
        given(this.service.listOrders(any(),any())).willReturn(pagedList);
        this.mockMvc.perform(get("/api/v1/customers/"+UUID.randomUUID()+"/orders"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    void getOrder() throws Exception {
        BeerOrderDto beerOrder = BeerOrderDto.builder()
                .id(UUID.randomUUID())
                .build();
        given(this.service.getOrderById(any(),any())).willReturn(beerOrder);
        this.mockMvc.perform(get("/api/v1/customers/"+UUID.randomUUID()+"/orders/"+beerOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(beerOrder.getId().toString())));
    }

    @AfterEach
    void tearDown() {
        reset(this.service);
    }
}