package com.meals.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter("/*")
public class UrlFilter implements Filter {

	@Override
	public void destroy() {
		// NOT IMPLEMENTED
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			// httpServletRequest.getSession().setAttribute("contextPath", httpServletRequest.getContextPath());
			httpServletRequest.getSession().setAttribute("contextPath", "");
			httpServletRequest.getSession().setAttribute("domain", httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName());
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// NOT IMPLEMENTED
	}
}
