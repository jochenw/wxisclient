package com.github.jochenw.wxisclient.svc;

import com.github.jochenw.wxisclient.data.Data;
import com.github.jochenw.wxisclient.data.IDocument;


public abstract class AbstractISService implements ISService {
	protected IDocument emptyDocument() { return Data.EMPTY_DOCUMENT; }
	protected IDocument asDocument(Object... pValues) {
		return Data.asDocument(pValues);
	}
}
