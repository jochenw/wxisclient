package com.github.jochenw.wxisclient.data;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public interface IDocument {
	public void setValue(@Nonnull String pKey, @Nullable Object pValue) throws UnsupportedOperationException;
	@Nullable public Object getValue(@Nonnull String pKey);
	public boolean isDocument(@Nonnull Object pValue);
	@Nonnull public default Object requireValue(String pKey) throws NoSuchElementException {
		final Object value = getValue(pKey);
		if (value == null) {
			throw new NoSuchElementException("No value available for key: " + pKey);
		}
		return value;
	}
	@Nullable public default String getString(@Nonnull String pKey) {
		final Object value = getValue(pKey);
		if (value == null) {
			return null;
		} else if (value instanceof String) {
			return (String) value;
		} else {
			throw new IllegalStateException("Expected string value for key " + pKey + ", got " + value.getClass().getName());
		}
	}
	@Nonnull public default String requireString(@Nonnull String pKey) throws NoSuchElementException {
		final Object value = getValue(pKey);
		if (value == null) {
			throw new NoSuchElementException("No value available for key: " + pKey);
		} else if (value instanceof String) {
			return (String) value;
		} else {
			throw new IllegalStateException("Expected string value for key " + pKey + ", got " + value.getClass().getName());
		}
	}
	@Nullable public default IDocument getDocument(@Nonnull String pKey) {
		final Object value = getValue(pKey);
		if (value == null) {
			return null;
		} else if (isDocument(value)) {
			return (IDocument) value;
		} else {
			throw new IllegalStateException("Expected document value for key " + pKey + ", got " + value.getClass().getName());
		}
	}
	@Nonnull public default IDocument requireDocument(@Nonnull String pKey) throws NoSuchElementException {
		final Object value = getValue(pKey);
		if (value == null) {
			throw new NoSuchElementException("No value available for key: " + pKey);
		} else if (isDocument(value)) {
			return (IDocument) value;
		} else {
			throw new IllegalStateException("Expected document value for key " + pKey + ", got " + value.getClass().getName());
		}
	}
	@Nullable public default URL getUrl(@Nonnull String pKey) {
		final Object value = getValue(pKey);
		if (value == null) {
			return null;
		} else if (value instanceof URL) {
			return (URL) value;
		} else if (value instanceof String) {
			final String str = (String) value;
			try {
				return new URL(str);
			} catch (MalformedURLException e) {
				throw new IllegalArgumentException("Invalid URL value for key " + pKey + ": " + str);
			}
		} else {
			throw new IllegalStateException("Expected document value for key " + pKey + ", got " + value.getClass().getName());
		}
	}
	@Nonnull public default URL requireUrl(@Nonnull String pKey) throws NoSuchElementException {
		final Object value = getValue(pKey);
		if (value == null) {
			throw new NoSuchElementException("No value available for key: " + pKey);
		} else if (value instanceof URL) {
			return (URL) value;
		} else if (value instanceof String) {
			final String str = (String) value;
			try {
				return new URL(str);
			} catch (MalformedURLException e) {
				throw new IllegalArgumentException("Invalid URL value for key " + pKey + ": " + str);
			}
		} else {
			throw new IllegalStateException("Expected document value for key " + pKey + ", got " + value.getClass().getName());
		}
	}
	@Nonnull public default File getFile(@Nonnull String pKey) {
		final Object value = getValue(pKey);
		if (value == null) {
			return null;
		} else {
			if (value instanceof File) {
				return (File) value;
			} else if (value instanceof String) {
				return new File((String) value);
			} else {
				throw new IllegalStateException("Expected file value for key " + pKey + ", got " + value.getClass().getName());
			}
		}
	}
	void forEach(@Nonnull BiConsumer<String,Object> pAction);
}
