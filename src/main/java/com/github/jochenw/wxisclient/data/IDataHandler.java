package com.github.jochenw.wxisclient.data;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IDataHandler {
	public void setValue(@Nonnull Object pDocument, @Nonnull String pKey, @Nullable Object pValue) throws UnsupportedOperationException;
	@Nullable public Object getValue(@Nonnull Object pDocument, @Nonnull String pKey);
	@Nonnull public default Object requireValue(@Nonnull Object pDocument, String pKey) throws NoSuchElementException {
		final Object value = getValue(pDocument, pKey);
		if (value == null) {
			throw new NoSuchElementException("No value available for key: " + pKey);
		}
		return value;
	}
	@Nullable public default String getString(@Nonnull Object pDocument, @Nonnull String pKey) {
		final Object value = getValue(pDocument, pKey);
		if (value == null) {
			return null;
		} else if (value instanceof String) {
			return (String) value;
		} else {
			throw new IllegalStateException("Expected string value for key " + pKey + ", got " + value.getClass().getName());
		}
	}
	@Nonnull public default String requireString(@Nonnull Object pDocument, @Nonnull String pKey) throws NoSuchElementException {
		final Object value = getValue(pDocument, pKey);
		if (value == null) {
			throw new NoSuchElementException("No value available for key: " + pKey);
		} else if (value instanceof String) {
			return (String) value;
		} else {
			throw new IllegalStateException("Expected string value for key " + pKey + ", got " + value.getClass().getName());
		}
	}
	@Nullable public default IDocument getDocument(@Nonnull Object pDocument, @Nonnull String pKey) {
		final Object value = getValue(pDocument, pKey);
		if (value == null) {
			return null;
		} else if (isDocument(value)) {
			return (IDocument) value;
		} else {
			throw new IllegalStateException("Expected document value for key " + pKey + ", got " + value.getClass().getName());
		}
	}
	@Nonnull public default IDocument requireDocument(@Nonnull Object pDocument, @Nonnull String pKey) throws NoSuchElementException {
		final Object value = getValue(pDocument, pKey);
		if (value == null) {
			throw new NoSuchElementException("No value available for key: " + pKey);
		} else if (isDocument(value)) {
			return (IDocument) value;
		} else {
			throw new IllegalStateException("Expected document value for key " + pKey + ", got " + value.getClass().getName());
		}
	}
	@Nullable public default URL getUrl(@Nonnull Object pDocument, @Nonnull String pKey) {
		final Object value = getValue(pDocument, pKey);
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
	@Nonnull public default URL requireUrl(@Nonnull Object pDocument, @Nonnull String pKey) throws NoSuchElementException {
		final Object value = getValue(pDocument, pKey);
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
	@Nonnull public default File getFile(@Nonnull Object pDocument, @Nonnull String pKey) {
		final Object value = getValue(pDocument, pKey);
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
	public boolean isDocument(Object pDocument);
	@Nonnull public IDocument asDocument(Object pObject);
	public void forEach(@Nonnull Object pDocument, @Nonnull BiConsumer<String,Object> pAction);
	@Nonnull public Object empty();
	@Nonnull public default Object document(Object... pValues) {
		final Object doc = empty();
		if (pValues != null) {
			for (int i = 0;  i < pValues.length;  i += 2) {
				setValue(doc, (String) pValues[i], pValues[i+1]);
			}
		}
		return doc;
	}
}
