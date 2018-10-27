package com.github.jochenw.wxisclient.svc;

import static org.junit.Assert.assertArrayEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.github.jochenw.wxisclient.data.Data;
import com.github.jochenw.wxisclient.data.IDocument;
import com.github.jochenw.wxisclient.svc.AbstractISService;
import com.github.jochenw.wxisclient.svc.ISService;
import com.github.jochenw.wxisclient.svc.Services;
import com.wm.data.IData;
import com.wm.data.IDataFactory;

import org.junit.Assert;

public class ServicesTest {
	public static class CreateMapService extends AbstractISService {
		@Override
		public IDocument run(IDocument pInput) {
			return asDocument("map", new HashMap<String,Object>());
		}
	}

	@Test
	public void testCreateMapService() {
		final IData data = IDataFactory.create();
		Services.run(CreateMapService.class, data);
		final Object mapObject = Data.IDATA.getValue(data, "map");
		Assert.assertNotNull(mapObject);
		Assert.assertTrue(mapObject instanceof Map);
		@SuppressWarnings("unchecked")
		final Map<String,Object> map = (Map<String,Object>) mapObject;
		Assert.assertTrue(map.isEmpty());
	}

	@Test
	public void testLambdaServices() {
		{
			final IData data = IDataFactory.create();
			Services.run(CreateMapService.class, data);
			final Object mapObject = Data.IDATA.getValue(data, "map");
			Assert.assertNotNull(mapObject);
			Assert.assertTrue(mapObject instanceof Map);
			@SuppressWarnings("unchecked")
			final Map<String,Object> map = (Map<String,Object>) mapObject;
			final ISService setter = (d) -> {
				@SuppressWarnings("unchecked")
				final Map<String,Object> mp = (Map<String,Object>) d.requireValue("map");
				final String key = d.requireString("key");
				mp.put(key, d.getValue("value"));
				return Data.EMPTY_DOCUMENT;
			};
			Services.run(setter, Data.asDocument("key", "Foo", "value", "Bar", "map", map));
			Assert.assertEquals(1, map.size());
			Assert.assertEquals("Bar", (String) map.get("Foo"));
		}
		{
			final Map<String,Object> map = new HashMap<>();
			map.put("IsValid", Boolean.TRUE);
			map.put("Foo", "Bar");
			final IData idata = (IData) Data.IDATA.document("map", map);
			final ISService getter = (d) -> {
				@SuppressWarnings("unchecked")
				final Map<String,Object> mp = (Map<String,Object>) d.requireValue("map");
				final String key = d.requireString("key");
				return Data.asDocument("value", mp.get(key));
			};
			Data.IDATA.setValue(idata, "key", "Foo");
			Services.run(getter, idata);
			Assert.assertEquals("Bar", Data.IDATA.requireValue(idata, "value"));
			Data.IDATA.setValue(idata, "key", "IsValid");
			Services.run(getter, idata);
			Assert.assertEquals(Boolean.TRUE, Data.IDATA.requireValue(idata, "value"));
		}
	}
}
