package com.github.jochenw.wxisclient.svc;

import com.github.jochenw.wxisclient.data.IDocument;

@FunctionalInterface
public interface ISService {
	public IDocument run(IDocument pInput);
}
