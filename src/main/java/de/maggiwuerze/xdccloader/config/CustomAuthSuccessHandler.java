package de.maggiwuerze.xdccloader.config;

import de.maggiwuerze.xdccloader.model.entity.User;
import de.maggiwuerze.xdccloader.persistency.UserRepository;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {
		private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

		private final UserRepository userRepository;

		@Override
		public void onAuthenticationSuccess(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication
		) throws IOException, ServletException {
			Long sessionTimeout;
			if (authentication != null) {

				String currentPrincipalName = authentication.getName();

				User user = userRepository.findUserByName(currentPrincipalName).orElse(null);
				sessionTimeout = user.getUserSettings().getSessionTimeout();

				request.getSession().setMaxInactiveInterval(sessionTimeout.intValue());
			}
			handle(request, response, authentication);
			clearAuthenticationAttributes(request);
		}

		protected void handle(
			HttpServletRequest request,
			HttpServletResponse response, Authentication authentication
		)
			throws IOException {

			String targetUrl = "/";
			if (response.isCommitted()) {
				return;
			}

			redirectStrategy.sendRedirect(request, response, targetUrl);
		}

		protected void clearAuthenticationAttributes(HttpServletRequest request) {
			HttpSession session = request.getSession(false);
			if (session == null) {
				return;
			}
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}