package com.sistemas;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
        "demo.admin.password=unexpected-admin-password",
        "demo.public-read-only=true"
})
@AutoConfigureMockMvc
class PublicDemoSecurityTests {

    @Autowired MockMvc mvc;

    @Test
    void modoPublicoMantieneLectorYDesactivaAdministradorAunqueExistaClave() throws Exception {
        mvc.perform(formLogin().user("demo").password("demo123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        mvc.perform(formLogin().user("admin-demo").password("unexpected-admin-password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuario/login?error"));
    }
}
