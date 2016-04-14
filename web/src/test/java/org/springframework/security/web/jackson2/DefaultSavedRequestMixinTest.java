package org.springframework.security.web.jackson2;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.Collections;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Jitendra Singh
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultSavedRequestMixinTest {

    ObjectMapper buildObjectMapper() {
        ObjectMapper mapper = new ObjectMapper()
                .enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)
                .addMixIn(DefaultSavedRequest.class, DefaultSavedRequestMixin.class)
                .addMixIn(Cookie.class, CookieMixin.class);
        mapper.setVisibilityChecker(
                mapper.getVisibilityChecker()
                        .withVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                        .withVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.ANY)
        );
        return mapper;
    }

    @Test
    public void matchRequestBuildWithConstructorAndBuilder() {
        DefaultSavedRequest request = new DefaultSavedRequest.Builder()
                .setCookies(Collections.singletonList(new Cookie("SESSION", "123456789")))
                .setHeaders(Collections.singletonMap("x-auth-token", Collections.singletonList("12")))
                .setScheme("http").setRequestURL("http://localhost").setServerName("localhost").setRequestURI("")
                .setLocales(Collections.singletonList(new Locale("en"))).setContextPath("").setMethod("")
                .setServletPath("").build();
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setCookies(new Cookie("SESSION", "123456789"));
        mockRequest.addHeader("x-auth-token", "12");

        assert request.doesRequestMatch(mockRequest, new PortResolverImpl());
    }

    @Test
    public void serializeDefaultRequestBuildWithConstructorTest() throws IOException, JSONException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("SESSION", "123456789"));
        request.addHeader("x-auth-token", "12");

        String expectedJsonString = "{" +
                    "\"@class\": \"org.springframework.security.web.savedrequest.DefaultSavedRequest\", \"cookies\": [\"java.util.ArrayList\", [{\"@class\": \"javax.servlet.http.Cookie\", \"name\": \"SESSION\", \"value\": \"123456789\", \"comment\": null, \"maxAge\": -1, \"path\": null, \"secure\":false, \"version\": 0, \"isHttpOnly\": false, \"domain\": null}]]," +
                    "\"locales\": [\"java.util.ArrayList\", [\"en\"]], \"headers\": {\"@class\": \"java.util.TreeMap\", \"x-auth-token\": [\"java.util.ArrayList\", [\"12\"]]}, \"parameters\": {\"@class\": \"java.util.TreeMap\"}," +
                    "\"contextPath\": \"\", \"method\": \"\", \"pathInfo\": null, \"queryString\": null, \"requestURI\": \"\", \"requestURL\": \"http://localhost\", \"scheme\": \"http\", " +
                    "\"serverName\": \"localhost\", \"servletPath\": \"\", \"serverPort\": 80"+
                "}";
        String actualString = buildObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(new DefaultSavedRequest(request, new PortResolverImpl()));
        JSONAssert.assertEquals(expectedJsonString, actualString, true);
    }

    @Test
    public void serializeDefaultRequestBuildWithBuilderTest() throws IOException, JSONException {
        DefaultSavedRequest request = new DefaultSavedRequest.Builder()
                .setCookies(Collections.singletonList(new Cookie("SESSION", "123456789")))
                .setHeaders(Collections.singletonMap("x-auth-token", Collections.singletonList("12")))
                .setScheme("http").setRequestURL("http://localhost").setServerName("localhost").setRequestURI("")
                .setLocales(Collections.singletonList(new Locale("en"))).setContextPath("").setMethod("")
                .setServletPath("").build();

        String expectedJsonString = "{" +
                    "\"@class\": \"org.springframework.security.web.savedrequest.DefaultSavedRequest\", \"cookies\": [\"java.util.ArrayList\", [{\"@class\": \"javax.servlet.http.Cookie\", \"name\": \"SESSION\", \"value\": \"123456789\", \"comment\": null, \"maxAge\": -1, \"path\": null, \"secure\":false, \"version\": 0, \"isHttpOnly\": false, \"domain\": null}]]," +
                    "\"locales\": [\"java.util.ArrayList\", [\"en\"]], \"headers\": {\"@class\": \"java.util.TreeMap\", \"x-auth-token\": [\"java.util.ArrayList\", [\"12\"]]}, \"parameters\": {\"@class\": \"java.util.TreeMap\"}," +
                    "\"contextPath\": \"\", \"method\": \"\", \"pathInfo\": null, \"queryString\": null, \"requestURI\": \"\", \"requestURL\": \"http://localhost\", \"scheme\": \"http\", " +
                    "\"serverName\": \"localhost\", \"servletPath\": \"\", \"serverPort\": 80"+
                "}";
        String actualString = buildObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(request);
        JSONAssert.assertEquals(expectedJsonString, actualString, true);
    }

    @Test
    public void deserializeDefaultSavedRequest() throws IOException {
        String jsonString = "{" +
                    "\"@class\": \"org.springframework.security.web.savedrequest.DefaultSavedRequest\", \"cookies\": [\"java.util.ArrayList\", [{\"@class\": \"javax.servlet.http.Cookie\", \"name\": \"SESSION\", \"value\": \"123456789\", \"comment\": null, \"maxAge\": -1, \"path\": null, \"secure\":false, \"version\": 0, \"isHttpOnly\": false, \"domain\": null}]]," +
                    "\"locales\": [\"java.util.ArrayList\", [\"en\"]], \"headers\": {\"@class\": \"java.util.TreeMap\", \"x-auth-token\": [\"java.util.ArrayList\", [\"12\"]]}, \"parameters\": {\"@class\": \"java.util.TreeMap\"}," +
                    "\"contextPath\": \"\", \"method\": \"\", \"pathInfo\": null, \"queryString\": null, \"requestURI\": \"\", \"requestURL\": \"http://localhost\", \"scheme\": \"http\", " +
                    "\"serverName\": \"localhost\", \"servletPath\": \"\", \"serverPort\": 80"+
                "}";
        DefaultSavedRequest request = buildObjectMapper().readValue(jsonString, DefaultSavedRequest.class);
        assertThat(request).isNotNull();
        assertThat(request.getCookies()).hasSize(1);
        assertThat(request.getLocales()).hasSize(1).contains(new Locale("en"));
        assertThat(request.getHeaderNames()).hasSize(1).contains("x-auth-token");
        assertThat(request.getHeaderValues("x-auth-token")).hasSize(1).contains("12");
    }
}