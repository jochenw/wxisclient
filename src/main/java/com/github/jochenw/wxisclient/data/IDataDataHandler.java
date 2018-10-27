package com.github.jochenw.wxisclient.data;

import java.util.function.BiConsumer;

import javax.annotation.Nonnull;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;

public class IDataDataHandler implements IDataHandler {
	public static class IDataDocument implements IDocument {
		@Nonnull private final IData data;
		@Nonnull private final IDataCursor crsr;

		public IDataDocument(@Nonnull IData pData) {
			data = pData;
			crsr = data.getCursor();
		}

		@Override
		public void setValue(String pKey, Object pValue) throws UnsupportedOperationException {
			IDataUtil.put(crsr,pKey, pValue);
		}

		@Override
		public Object getValue(String pKey) {
			return IDataUtil.get(crsr,pKey);
		}

		@Override
		public boolean isDocument(Object pValue) {
			return pValue instanceof IData;
		}

		@Override
		public void forEach(BiConsumer<String, Object> pAction) {
			final IDataCursor c = data.getCursor();
			if (c.first()) {
				do {
					pAction.accept(c.getKey(), c.getValue());
				} while (c.next());
			}
		}
	}

	@Override
	public void setValue(Object pDocument, String pKey, Object pValue) throws UnsupportedOperationException {
		final IData data = (IData) pDocument;
		final IDataCursor crsr = data.getCursor();
		IDataUtil.put(crsr, pKey, pValue);
		crsr.destroy();
	}

	@Override
	public Object getValue(Object pDocument, String pKey) {
		final IData data = (IData) pDocument;
		final IDataCursor crsr = data.getCursor();
		final Object value = IDataUtil.get(crsr, pKey);
		crsr.destroy();
		return value;
	}

	@Override
	public boolean isDocument(Object pDocument) {
		return pDocument instanceof IData;
	}

	@Override
	public IDocument asDocument(Object pObject) {
		return new IDataDocument((IData) pObject);
	}

	@Override
	public void forEach(Object pDocument, BiConsumer<String, Object> pAction) {
		final IData data = (IData) pDocument;
		final IDataCursor crsr = data.getCursor();
		if (crsr.first()) {
			do {
				pAction.accept(crsr.getKey(), crsr.getValue());
			} while (crsr.next());
		}
	}

	@Override
	public Object empty() {
		return IDataFactory.create();
	}
	@Nonnull public Object document(Object... pValues) {
		final IData data = IDataFactory.create();
		final IDataCursor crsr = data.getCursor();
		if (pValues != null) {
			for (int i = 0;  i < pValues.length;  i += 2) {
				IDataUtil.put(crsr, (String) pValues[i], pValues[i+1]);
			}
		}
		crsr.destroy();
		return data;
	}
}
