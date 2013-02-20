package com.salvador.spi;

import javax.enterprise.context.NormalScope;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(value = {METHOD, TYPE, FIELD})
@Retention(value = RUNTIME)
@NormalScope
@Inherited
public @interface ViewScoped {

}
