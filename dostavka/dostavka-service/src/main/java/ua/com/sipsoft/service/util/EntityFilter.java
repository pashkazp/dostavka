package ua.com.sipsoft.service.util;

@FunctionalInterface
public interface EntityFilter<T> {
	boolean isPass(T entity);
}