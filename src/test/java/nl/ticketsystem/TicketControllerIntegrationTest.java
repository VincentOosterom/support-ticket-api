package nl.ticketsystem;


import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@AutoConfigureMockMvc
public class TicketControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getTickets_metCustomerToken_userNietGevonden_geeft404() throws Exception {
        mockMvc.perform(get("/tickets")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))))
                .andExpect(status().isNotFound());
    }

    @Test
    void postTicket_withoutToken() throws Exception {
        mockMvc.perform(post("/tickets")).andExpect(status().isUnauthorized());
    }
}
