package net.os.goodcourses.controller;

import net.os.goodcourses.configuration.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import static junit.framework.TestCase.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {EmailConfig.class, JPAConfig.class, MVCConfig.class, CourseWebApplicationInitializer.class, SecurityConfig.class, ServiceConfig.class})
@TestPropertySource("classpath:/test.properties")
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
    public void testRedirectOnMainPageCourses() throws Exception {
        this.mockMvc.perform(get("/courses"))
                .andExpect(forwardedUrl("/WEB-INF/JSP/courses.jsp"));
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
        assertTrue(courses.size() > 0);
    }
}
