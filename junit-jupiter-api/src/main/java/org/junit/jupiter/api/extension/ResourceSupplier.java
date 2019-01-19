/*
 * Copyright 2015-2018 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.jupiter.api.extension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Supplier;

import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;
import org.junit.platform.commons.JUnitException;

public interface ResourceSupplier<R> extends AutoCloseable, CloseableResource, Supplier<R> {

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	@interface New {

		Class<? extends ResourceSupplier<?>> value();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	@interface Singleton {

		Class<? extends ResourceSupplier<?>> value();
	}

	@Override
	default void close() {
		R instance = get();
		if (instance instanceof AutoCloseable) {
			try {
				((AutoCloseable) instance).close();
			}
			catch (Exception e) {
				throw new JUnitException("Closing '" + instance + "' failed", e);
			}
		}
	}
}
