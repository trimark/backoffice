package com.trimark.backoffice.auth;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class MyMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler implements MethodSecurityExpressionHandler  {
	 
	@Override
	protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
		MyMethodSecurityExpressionRoot root = new MyMethodSecurityExpressionRoot(authentication);
		root.setThis(invocation.getThis());
		root.setPermissionEvaluator(getPermissionEvaluator());
		return root;
	}
}