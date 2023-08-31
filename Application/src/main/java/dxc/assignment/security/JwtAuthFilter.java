package dxc.assignment.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

// Authenticate the jwt on each request to the api
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	private JwtProvider jwtProvider;
	private MemberDetailService memberDetailService;

	public JwtAuthFilter(JwtProvider jwtProvider,
			MemberDetailService memberDetailService) {
		this.jwtProvider = jwtProvider;
		this.memberDetailService = memberDetailService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			// Get jwt from request
			String jwt = getJwtFromRequest(request);

			if (StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {
				// Get email from jwt
				String email = jwtProvider.getEmailFromToken(jwt);
				// Get user details from email
				UserDetails userDetails = memberDetailService.loadUserByUsername(email);
				if (userDetails != null) {
					// Set security context
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(
							new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		} catch (Exception ex) {
			System.out.println("failed on set user authentication" + ex);
			// Return 403 on fail jwt validation
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
		}

		filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		// Get token from the request header
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
