package amazon10.sys.utils.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface DictAnnotation {

	String key();

	String name() default "Str";

	String nullValue() default "";

}
