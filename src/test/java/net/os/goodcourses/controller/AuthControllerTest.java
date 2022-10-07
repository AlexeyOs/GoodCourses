package net.os.goodcourses.controller;

import net.os.goodcourses.configuration.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {EmailConfig.class, JPAConfig.class, MVCConfig.class, CourseWebApplicationInitializer.class, SecurityConfig.class, ServiceConfig.class})
@TestPropertySources({
        @TestPropertySource("classpath:application.properties"),
        @TestPropertySource("classpath:${configProfile}.properties")
})
public class AuthControllerTest {

    @Autowired
    private WebApplicationContext context;


    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }


    @Test
    public void testRoleNotAuthorized() throws Exception {
        this.mockMvc.perform(get("/my-profile"))
                .andDo(print())
                .andExpect(status().isFound());
    }


}
