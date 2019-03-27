package guru.springframework.brewery.web.controllers;

import guru.springframework.brewery.services.BeerService;
import guru.springframework.brewery.web.model.BeerDto;
import guru.springframework.brewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class )
class BeerControllerTest {

    @Mock
    private BeerService service;

    @InjectMocks
    private BeerController controller;

    private MockMvc mockMvc;

    private BeerDto validBeer;

    @BeforeEach
    void setUp() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(this.controller).build();

        this.validBeer = BeerDto.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Beer 1")
                .beerStyle(BeerStyleEnum.PALE_ALE)
                .price(new BigDecimal("12.99"))
                .quantityOnHand(4)
                .upc(123456789012L)
                .createdDate(OffsetDateTime.now())
                .lastModifiedDate(OffsetDateTime.now())
                .build();
    }

    @Test
    void getBeerById() throws Exception {
        given(this.service.findBeerById(any())).willReturn(this.validBeer);
        this.mockMvc.perform(get("/api/v1/beer/"+this.validBeer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.id", is(this.validBeer.getId().toString())))
            .andExpect(jsonPath("$.beerName", is("Beer 1")));
    }
}