package net.os.goodcourses.auth;

import net.os.goodcourses.configuration.*;
import net.os.goodcourses.filter.CourseFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;



@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {EmailConfig.class, JPAConfig.class, MVCConfig.class, CourseWebApplicationInitializer.class, SecurityConfig.class, ServiceConfig.class})
@TestPropertySource("classpath:/test.properties")
public class CourseFilterTests {

    private FilterChainProxy filterChainProxy;

    @Autowired
    List<Filter> filters;

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
    public void testFilterCourse() {
        CourseFilter courseFilter = context.getBean(CourseFilter.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = new MockFilterChain();
        try {
            courseFilter.doFilter(request,response,chain);
            assertEquals("", request.getAttribute("REQUEST_URL"));
        } catch (IOException | ServletException ex) {
            assertNull(ex.getMessage());
        }
    }
}
