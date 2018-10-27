package com.github.jochenw.wxisclient.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.UndeclaredThrowableException;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.github.jochenw.wxisclient.util.Exceptions;


public class ExceptionsTest {
	@Test
	public void test() {
		runTest(null, NullPointerException.class, "The Throwable must not be null.");
		final RuntimeException rte = new RuntimeException();
		runTest(rte);
		final Error err = new OutOfMemoryError();
		runTest(err);
		final IOException ioe = new IOException();
		runTest(ioe, UncheckedIOException.class);
		final SAXException se = new SAXException();
		runTest(se, UndeclaredThrowableException.class);
	}

	private void runTest(Throwable pTh, Class<? extends Throwable> pType, String pMessage) {
		try {
			Exceptions.show(pTh);
			Assert.fail("Expected Exception");
		} catch (Throwable t) {
			Assert.assertSame(pType, t.getClass());
			Assert.assertEquals(pMessage, t.getMessage());
		}
	}

	private void runTest(Throwable pTh) {
		try {
			Exceptions.show(pTh);
			Assert.fail("Expected Exception");
		} catch (Throwable t) {
			Assert.assertSame(pTh, t);
		}
	}

	private void runTest(Throwable pTh, Class<? extends Throwable> pType) {
		try {
			Exceptions.show(pTh);
			Assert.fail("Expected Exception");
		} catch (Throwable t) {
			Assert.assertSame(pType, t.getClass());
			Assert.assertSame(pTh, t.getCause());
		}
	}
}
