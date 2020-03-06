package net.os.goodcourses.controller;

import net.os.goodcourses.configuration.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {ElasticSearchConfig.class, EmailConfig.class, JPAConfig.class, MVCConfig.class, ResumeWebApplicationInitializer.class, SecurityConfig.class, ServiceConfig.class})
public class CourseControllerTest {

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
    public void testMainPageCourses() throws Exception {
        this.mockMvc.perform(get("/courses"))
                .andExpect(forwardedUrl("/WEB-INF/JSP/courses.jsp"));
    }
}
