package com.github.jochenw.wxisclient.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.Assert;

import org.junit.Test;

import com.github.jochenw.wxisclient.data.ArrayDocument;
import com.github.jochenw.wxisclient.data.Data;
import com.github.jochenw.wxisclient.data.IDataHandler;
import com.github.jochenw.wxisclient.data.IDocument;
import com.github.jochenw.wxisclient.util.Objects;


public class DataTest {
	@Test
	public void testMap() {
		runTest(Data.MAP);
	}

	@Test
	public void testIData() {
		runTest(Data.IDATA);
	}

	@Test
	public void testDefault() {
		runTest(Data.DEFAULT);
		final Object obj = new Object();
		Assert.assertFalse(Data.DEFAULT.isDocument(obj));
		final IDocument doc = Data.asDocument(Collections.emptyMap());
		Assert.assertNotNull(doc);
		try {
			Data.DEFAULT.setValue(obj, "Foo", "Bar");
			Assert.fail("Expected Exception");
		} catch (IllegalStateException e) {
			Assert.assertEquals("Invalid document type: java.lang.Object", e.getMessage());
		}
		try {
			Data.DEFAULT.getValue(obj, "Foo");
			Assert.fail("Expected Exception");
		} catch (IllegalStateException e) {
			Assert.assertEquals("Invalid document type: java.lang.Object", e.getMessage());
		}
		try {
			Data.DEFAULT.asDocument(obj);
			Assert.fail("Expected Exception");
		} catch (IllegalStateException e) {
			Assert.assertEquals("Invalid document type: java.lang.Object", e.getMessage());
		}
		try {
			Data.DEFAULT.forEach(obj, (s,o) -> { throw new IllegalStateException("Unexpected element"); });
			Assert.fail("Expected Exception");
		} catch (IllegalStateException e) {
			Assert.assertEquals("Invalid document type: java.lang.Object", e.getMessage());
		}
	}

	@Test
	public void testImmutableDocuments() {
		try {
			Data.EMPTY_DOCUMENT.setValue("one", Integer.valueOf(1));
			Assert.fail("Expected Exception");
		} catch (UnsupportedOperationException e) {
			Assert.assertEquals("This document is immutable.", e.getMessage());
		}
		try {
			new ArrayDocument(new Object[] {"a", "b"}).setValue("c", "d");
			Assert.fail("Expected Exception");
		} catch (UnsupportedOperationException e) {
			Assert.assertEquals("This document is immutable.", e.getMessage());
		}
		Assert.assertSame(Data.EMPTY_DOCUMENT, Data.asDocument((Object[]) null));
		Assert.assertSame(Data.EMPTY_DOCUMENT, Data.asDocument(new Object[] {}));
	}
	
	public void runTest(IDataHandler pHandler) {
		final Object document = pHandler.document("Answer", Integer.valueOf(42), "Foo", "Bar", "IsValid", Boolean.TRUE);
		final Integer i = (Integer) Objects.requireNonNull(pHandler.getValue(document, "Answer"));
		Assert.assertEquals(42, i.intValue());
		Assert.assertEquals("Bar", pHandler.getString(document, "Foo"));
		Assert.assertEquals("Bar", pHandler.requireString(document, "Foo"));
		Assert.assertTrue(((Boolean) Objects.requireNonNull(pHandler.getValue(document, "IsValid"))).booleanValue());
		Assert.assertNull(pHandler.getValue(document, "NoSuchKey"));
		Assert.assertTrue(pHandler.isDocument(document));
		Assert.assertTrue(Data.DEFAULT.isDocument(document));
		final IDocument doc = pHandler.asDocument(document);
		Assert.assertEquals(42, Objects.requireNonNull(((Integer) doc.getValue("Answer"))).intValue());
		Assert.assertEquals("Bar", doc.getString("Foo"));
		Assert.assertEquals("Bar", doc.requireString("Foo"));
		Assert.assertTrue(((Boolean) Objects.requireNonNull(doc.getValue("IsValid"))).booleanValue());
		Assert.assertNull(doc.getValue("NoSuchKey"));
		try {
			doc.requireValue("NoSuchKey");
			Assert.fail("Expected Exception");
		} catch (NoSuchElementException e) {
			Assert.assertEquals("No value available for key: NoSuchKey", e.getMessage());
		}
		Assert.assertNull(doc.getString("NoSuchKey"));
		try {
			doc.requireString("NoSuchKey");
			Assert.fail("Expected Exception");
		} catch (NoSuchElementException e) {
			Assert.assertEquals("No value available for key: NoSuchKey", e.getMessage());
		}
		Assert.assertNull(doc.getFile("NoSuchKey"));
		Assert.assertNull(doc.getUrl("NoSuchKey"));
		try {
			doc.requireUrl("NoSuchKey");
			Assert.fail("Expected Exception");
		} catch (NoSuchElementException e) {
			Assert.assertEquals("No value available for key: NoSuchKey", e.getMessage());
		}
		Assert.assertNull(doc.getFile("NoSuchFile"));
		Assert.assertNull(doc.getUrl("NoSuchUrl"));
		Assert.assertEquals("Bar", doc.getFile("Foo").getPath());
		try {
			doc.getUrl("Foo");
			Assert.fail("Expected Exception");
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Invalid URL value for key Foo: Bar", e.getMessage());
		}
		pHandler.setValue(document, "NoSuchKey", "Some Value");
		Assert.assertEquals("Some Value", doc.getString("NoSuchKey"));
		Assert.assertEquals("Some Value", pHandler.getString(document, "NoSuchKey"));
		doc.setValue("NoSuchKey", "Another Value");
		Assert.assertEquals("Another Value", doc.requireString("NoSuchKey"));
		Assert.assertEquals("Another Value", pHandler.requireString(document, "NoSuchKey"));
		final Map<String,Object> map = new HashMap<String,Object>();
		doc.forEach((s,o) -> map.put(s, o));
		Assert.assertEquals(4, map.size());
		Assert.assertEquals("Bar", (String) map.get("Foo"));
		Assert.assertEquals(42, ((Integer) map.get("Answer")).intValue());
		Assert.assertTrue(((Boolean) map.get("IsValid")).booleanValue());
		Assert.assertEquals("Another Value", map.get("NoSuchKey"));
		map.clear();
		pHandler.forEach(document, (s,o) -> map.put(s, o));
		Assert.assertEquals(4, map.size());
		Assert.assertEquals("Bar", (String) map.get("Foo"));
		Assert.assertEquals(42, ((Integer) map.get("Answer")).intValue());
		Assert.assertTrue(((Boolean) map.get("IsValid")).booleanValue());
		Assert.assertEquals("Another Value", map.get("NoSuchKey"));
		Assert.assertTrue(doc.isDocument(document));
		Assert.assertTrue(pHandler.isDocument(document));
		Assert.assertTrue(Data.isDocument(document));
		final Object emptyDocument = pHandler.empty();
		map.clear();
		pHandler.forEach(emptyDocument, (s,o) -> map.put(s, o));
		Assert.assertTrue(map.isEmpty());
	}
}
