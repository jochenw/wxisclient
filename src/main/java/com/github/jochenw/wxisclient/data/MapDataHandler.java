package com.github.jochenw.wxisclient.data;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;


public class MapDataHandler implements IDataHandler {
	public static class MapDocument implements IDocument {
		private final Map<String,Object> map;

		public MapDocument(Map<String,Object> pMap) {
			map = pMap;
		}

		@Override
		public void setValue(String pKey, Object pValue) throws UnsupportedOperationException {
			map.put(pKey,  pValue);
		}

		@Override
		public Object getValue(String pKey) {
			return map.get(pKey);
		}

		@Override
		public boolean isDocument(Object pValue) {
			return pValue instanceof Map;
		}

		@Override
		public void forEach(BiConsumer<String, Object> pAction) {
			for (Map.Entry<String,Object> en : map.entrySet()) {
				pAction.accept(en.getKey(), en.getValue());
			}
		}
	}

	@Override
	public void setValue(Object pDocument, String pKey, Object pValue) throws UnsupportedOperationException {
		@SuppressWarnings("unchecked")
		final Map<String,Object> map = (Map<String,Object>) pDocument;
		map.put(pKey, pValue);
	}

	@Override
	public Object getValue(Object pDocument, String pKey) {
		@SuppressWarnings("unchecked")
		final Map<String,Object> map = (Map<String,Object>) pDocument;
		return map.get(pKey);
	}

	@Override
	public boolean isDocument(Object pDocument) {
		return pDocument instanceof Map;
	}

	@Override
	public IDocument asDocument(Object pObject) {
		@SuppressWarnings("unchecked")
		final Map<String,Object> map = (Map<String,Object>) pObject;
		return new MapDocument(map);
	}

	@Override
	public void forEach(Object pDocument, BiConsumer<String, Object> pAction) {
		@SuppressWarnings("unchecked")
		final Map<String,Object> map = (Map<String,Object>) pDocument;
		for (Map.Entry<String,Object> en : map.entrySet()) {
			pAction.accept(en.getKey(), en.getValue());
		}
	}

	@Override
	public Object empty() {
		return new HashMap<String,Object>();
	}
}
