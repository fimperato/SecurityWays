package it.imperato.service.security.interceptor;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
//import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Francesco
 *
 */
//@Transactional(readOnly = true)
public class MyFilterSecurityInterceptor extends AbstractSecurityInterceptor
		implements Filter {
	
	private FilterInvocationSecurityMetadataSource securityMetadataSource;

	public void invoke(FilterInvocation fi) throws IOException,
			ServletException {
//		if( SecurityContextHolder.getContext().getAuthentication().getAuthorities().size()==1 
//				&& "ROLE_ANONYMOUS".equals(
//						SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority()) )
		SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
		InterceptorStatusToken token = super.beforeInvocation(fi);
		try {
			fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
		} finally {
			super.afterInvocation(token, null);
		}
	}

	public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
		return securityMetadataSource;
	}

	public void setSecurityMetadataSource(
			FilterInvocationSecurityMetadataSource securityMetadataSource) {
		this.securityMetadataSource = securityMetadataSource;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		FilterInvocation fi = new FilterInvocation(request, response, chain);
		invoke(fi);
	}

	@Override
	public void destroy() {

	}

	@Override
	public Class<? extends Object> getSecureObjectClass() {
		return FilterInvocation.class;
	}

	@Override
	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return this.securityMetadataSource;
	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
