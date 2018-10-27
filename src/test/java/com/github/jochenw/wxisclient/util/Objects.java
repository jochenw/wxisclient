package com.github.jochenw.wxisclient.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Objects {
	@Nonnull public static <O> O requireNonNull(@Nullable O pValue) {
		if (pValue == null) {
			throw new NullPointerException();
		}
		return pValue;
	}

	@Nonnull public static <O> O requireNonNull(@Nullable O pValue, @Nonnull String pMessage) {
		if (pValue == null) {
			throw new NullPointerException(pMessage);
		}
		return pValue;
	}
}
