package com.app.auction.shared;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Target({ElementType.PARAMETER})//methoda parametre atcaz
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal //Annotation that is used to resolve Authentication.getPrincipal() to a methodargument.
/*
 * Authenticated user can use with this annotation like -@CurrentUser User user-
 */
public @interface CurrentUser {

}
