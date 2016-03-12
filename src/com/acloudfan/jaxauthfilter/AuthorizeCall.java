package com.acloudfan.jaxauthfilter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.ws.rs.NameBinding;


@AuthorizeCall //annotation is the name binding annotation
@NameBinding
@Retention(RetentionPolicy.RUNTIME)

public @interface AuthorizeCall {

}
