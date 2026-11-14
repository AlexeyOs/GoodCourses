package net.os.goodcourses.controller;

import net.os.goodcourses.configuration.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {EmailConfig.class, JPAConfig.class, MVCConfig.class, CourseWebApplicationInitializer.class, SecurityConfig.class, ServiceConfig.class})
@TestPropertySource("classpath:/test.properties")
public class CourseControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    public void testRendersCourseCatalogView() throws Exception {
        this.mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses"))
                .andExpect(result -> org.junit.jupiter.api.Assertions.assertTrue(
                        result.getResponse().getIncludedUrls().contains("/WEB-INF/JSP/courses.jsp")));
    }

    @Test
    public void testOnGetCoursesOfModel() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(get("/courses"))
                .andDo(print())
                .andExpect(status().isOk());

        ModelAndView coursesModelAndView = resultActions.andReturn().getModelAndView();
        assert coursesModelAndView != null;
        ModelMap coursesModelMap = coursesModelAndView.getModelMap();
        Collection<Object> courses = (Collection<Object>) coursesModelMap.get("courses");
        assertNotNull(courses);
    }
}
