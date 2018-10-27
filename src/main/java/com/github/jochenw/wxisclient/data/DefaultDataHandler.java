package com.github.jochenw.wxisclient.data;

import java.util.Objects;
import java.util.function.BiConsumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DefaultDataHandler implements IDataHandler {
	@Override
	public void setValue(@Nonnull Object pDocument, @Nonnull String pKey, @Nullable Object pValue) throws UnsupportedOperationException {
		final Object doc = Objects.requireNonNull(pDocument, "Document");
		final String key = Objects.requireNonNull(pKey, "Key");
		for (IDataHandler dh : Data.getDataHandlers()) {
			if (dh != this  &&  dh.isDocument(doc)) {
				dh.setValue(doc, key, pValue);
				return;
			}
		}
		throw new IllegalStateException("Invalid document type: " + doc.getClass().getName());
	}

	@Override
	public Object getValue(Object pDocument, String pKey) {
		final Object doc = Objects.requireNonNull(pDocument, "Document");
		final String key = Objects.requireNonNull(pKey, "Key");
		for (IDataHandler dh : Data.getDataHandlers()) {
			if (dh != this   &&  dh.isDocument(doc)) {
				return dh.getValue(doc, key);
			}
		}
		throw new IllegalStateException("Invalid document type: " + doc.getClass().getName());
	}

	@Override
	public boolean isDocument(Object pDocument) {
		final Object doc = Objects.requireNonNull(pDocument, "Document");
		for (IDataHandler dh : Data.getDataHandlers()) {
			if (dh != this  &&  dh.isDocument(doc)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public IDocument asDocument(Object pObject) {
		final Object doc = Objects.requireNonNull(pObject, "Document");
		for (IDataHandler dh : Data.getDataHandlers()) {
			if (dh != this  &&  dh.isDocument(doc)) {
				return dh.asDocument(doc);
			}
		}
		throw new IllegalStateException("Invalid document type: " + doc.getClass().getName());
	}

	@Override
	public void forEach(Object pDocument, BiConsumer<String, Object> pAction) {
		final Object doc = Objects.requireNonNull(pDocument, "Document");
		for (IDataHandler dh : Data.getDataHandlers()) {
			if (dh != this  &&  dh.isDocument(doc)) {
				dh.forEach(pDocument, pAction);
				return;
			}
		}
		throw new IllegalStateException("Invalid document type: " + doc.getClass().getName());
	}

	@Override
	@Nonnull public Object empty() {
		return Data.getDataHandlers().get(0).empty();
	}
}
