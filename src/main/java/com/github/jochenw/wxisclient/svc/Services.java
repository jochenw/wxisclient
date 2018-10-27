package com.github.jochenw.wxisclient.svc;

import com.github.jochenw.wxisclient.data.Data;
import com.github.jochenw.wxisclient.data.IDocument;
import com.github.jochenw.wxisclient.util.Exceptions;
import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataUtil;

public class Services {
	public static void run(ISService pService, IData pInput) {
		final IDocument output = run(pService, Data.IDATA.asDocument(pInput));
		final IDataCursor crsr = pInput.getCursor();
		output.forEach((s,o) -> IDataUtil.put(crsr, s, o));
		crsr.destroy();
	}

	public static void run(Class<? extends ISService> pServiceClass, IData pInput) {
		final ISService svc;
		try {
			svc = pServiceClass.newInstance();
		} catch (Throwable t) {
			throw Exceptions.show(t);
		}
		run(svc, pInput);
	}

	public static IDocument run(ISService pService, IDocument pInput) {
		return pService.run(pInput);
	}
}
