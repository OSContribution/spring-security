/*
 * Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.security.web.jackson2;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.security.jackson2.SecurityJacksonModules;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.SavedCookie;

import javax.servlet.http.Cookie;

/**
 * Jackson module for spring-security-web. This module register {@link CookieMixin},
 * {@link DefaultCsrfTokenMixin}, {@link DefaultSavedRequestMixin} and {@link WebAuthenticationDetailsMixin}. If no
 * default typing enabled by default then it'll enable it because typing info is needed to properly serialize/deserialize objects.
 * In order to use this module just add this module into your ObjectMapper configuration.
 *
 * <pre>
 *     ObjectMapper mapper = new ObjectMapper();
 *     mapper.registerModule(new WebJackson2Module());
 * </pre>
 * <b>Note: use {@link SecurityJacksonModules#getModules()} to get list of all security modules.</b>
 *
 * @author Jitendra Singh
 * @see SecurityJacksonModules
 * @since 4.2
 */
public class WebJackson2Module extends SimpleModule {

	public WebJackson2Module() {
		super(WebJackson2Module.class.getName(), new Version(1, 0, 0, null, null, null));
	}

	@Override
	public void setupModule(SetupContext context) {
		SecurityJacksonModules.enableDefaultTyping((ObjectMapper) context.getOwner());
		context.setMixInAnnotations(Cookie.class, CookieMixin.class);
		context.setMixInAnnotations(SavedCookie.class, SavedCookieMixin.class);
		context.setMixInAnnotations(DefaultCsrfToken.class, DefaultCsrfTokenMixin.class);
		context.setMixInAnnotations(DefaultSavedRequest.class, DefaultSavedRequestMixin.class);
		context.setMixInAnnotations(WebAuthenticationDetails.class, WebAuthenticationDetailsMixin.class);
	}
}
