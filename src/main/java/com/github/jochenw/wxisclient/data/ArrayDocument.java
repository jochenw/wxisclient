package com.github.jochenw.wxisclient.data;

import java.util.function.BiConsumer;

public final class ArrayDocument implements IDocument {
	private Object[] values;

	public ArrayDocument(Object[] pValues) {
		values = pValues;
	}

	@Override
	public void setValue(String pKey, Object pValue) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This document is immutable.");
	}

	@Override
	public Object getValue(String pKey) {
		if (values != null) {
			for (int i = 0;  i < values.length;  i += 2) {
				if (pKey.equals(values[i])) {
					return values[i+1];
				}
			}
		}
		return null;
	}

	@Override
	public boolean isDocument(Object pValue) {
		return false;
	}

	@Override
	public void forEach(BiConsumer<String, Object> pAction) {
		if (values != null) {
			for (int i = 0;  i < values.length;  i += 2) {
				pAction.accept((String) values[i], values[i+1]);
			}
		}
	}
}