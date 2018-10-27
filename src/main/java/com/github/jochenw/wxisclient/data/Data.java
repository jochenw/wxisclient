package com.github.jochenw.wxisclient.data;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import javax.annotation.Nonnull;

public class Data {
	public static IDataHandler MAP = new MapDataHandler();
	public static IDataHandler IDATA = new IDataDataHandler();
	public static IDataHandler DEFAULT = new DefaultDataHandler();
	private static final List<IDataHandler> DATA_HANDLERS = newDataHandlerList();

	private static List<IDataHandler> newDataHandlerList(){
		return Arrays.asList(MAP,IDATA,DEFAULT);
	}

	public static List<IDataHandler> getDataHandlers() {
		return DATA_HANDLERS;
	}

	public static boolean isDocument(@Nonnull Object pObject) {
		return DEFAULT.isDocument(pObject);
	}

	public static IDocument asDocument(@Nonnull Object pObject) {
		return DEFAULT.asDocument(pObject);
	}

	public static IDocument asDocument(Object... pValues) {
		if (pValues == null  ||  pValues.length == 0) {
			return EMPTY_DOCUMENT;
		} else if (pValues.length % 2 == 1) {
			throw new IllegalArgumentException("Expected even number of values, got " + pValues.length);
		} else {
			if (pValues != null) {
				for (int i = 0;  i < pValues.length;  i += 2) {
					if (pValues[i] == null) {
						throw new NullPointerException("Null value at index " + i);
					}
					if (!(pValues[i] instanceof String)) {
						throw new IllegalArgumentException("Non-String value at index " + i);
					}
				}
			}
			return new ArrayDocument(pValues);
		}
	}
	
	public static final IDocument EMPTY_DOCUMENT = new IDocument() {
		@Override
		public void setValue(String pKey, Object pValue) throws UnsupportedOperationException {
			throw new UnsupportedOperationException("This document is immutable.");
		}
	
		@Override
		public Object getValue(String pKey) {
			return null;
		}
	
		@Override
		public boolean isDocument(Object pValue) {
			return false;
		}
	
		@Override
		public void forEach(BiConsumer<String, Object> pAction) {
			// Do nothing
		}
	};
}
