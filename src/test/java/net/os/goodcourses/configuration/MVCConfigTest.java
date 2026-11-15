package net.os.goodcourses.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.StaticWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

class MVCConfigTest {

	@Test
	void includedJspKeepsUtf8ResponseEncoding() throws Exception {
		MockServletContext servletContext = new MockServletContext();
		try (StaticWebApplicationContext context = new StaticWebApplicationContext()) {
			context.setServletContext(servletContext);
			context.refresh();
			InternalResourceViewResolver resolver = (InternalResourceViewResolver) new MVCConfig().viewResolver();
			resolver.setApplicationContext(context);

			View view = resolver.resolveViewName("sign-in", Locale.ROOT);
			assertNotNull(view);
			MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
			request.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
			MockHttpServletResponse response = new MockHttpServletResponse();
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			view.render(Map.of(), request, response);

			assertEquals("/WEB-INF/JSP/sign-in.jsp", response.getIncludedUrl());
			assertEquals(StandardCharsets.UTF_8.name(), response.getCharacterEncoding());
		}
	}
}
