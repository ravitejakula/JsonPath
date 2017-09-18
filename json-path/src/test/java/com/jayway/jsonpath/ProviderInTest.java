package com.jayway.jsonpath;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.gson.JsonArray;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonOrgJsonProvider;
import com.jayway.jsonpath.spi.json.JsonSmartJsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.JsonOrgMappingProvider;
import com.jayway.jsonpath.spi.mapper.JsonSmartMappingProvider;

import junit.framework.Assert;

import org.assertj.core.util.Lists;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.jayway.jsonpath.JsonPath.using;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class ProviderInTest {
    private final String JSON = "[{\"foo\": \"bar\"}, {\"foo\": \"baz\"}]";
    private final String EQUALS_FILTER = "$.[?(@.foo == %s)].foo";
    private final String IN_FILTER = "$.[?(@.foo in [%s])].foo";
    private final String DOUBLE_QUOTES = "\"bar\"";
    private final String DOUBLE_QUOTES_EQUALS_FILTER = String.format(EQUALS_FILTER, DOUBLE_QUOTES);
    private final String DOUBLE_QUOTES_IN_FILTER = String.format(IN_FILTER, DOUBLE_QUOTES);
    private final String SINGLE_QUOTES = "'bar'";
    private final String SINGLE_QUOTES_EQUALS_FILTER = String.format(EQUALS_FILTER, SINGLE_QUOTES);
    private final String SINGLE_QUOTES_IN_FILTER = String.format(IN_FILTER, SINGLE_QUOTES);


    @Test
    public void testJsonPathQuotesJackson() throws Exception {
        final Configuration jackson = Configuration.builder().jsonProvider(new JacksonJsonProvider()).mappingProvider(new JacksonMappingProvider()).build();
        final DocumentContext ctx = JsonPath.using(jackson).parse(JSON);

        final List<String> doubleQuoteEqualsResult = ctx.read(DOUBLE_QUOTES_EQUALS_FILTER);
        assertEquals(Lists.newArrayList("bar"), doubleQuoteEqualsResult);

        final List<String> singleQuoteEqualsResult = ctx.read(SINGLE_QUOTES_EQUALS_FILTER);
        assertEquals(doubleQuoteEqualsResult, singleQuoteEqualsResult);

        final List<String> doubleQuoteInResult = ctx.read(DOUBLE_QUOTES_IN_FILTER);
        assertEquals(doubleQuoteInResult, doubleQuoteEqualsResult);

        final List<String> singleQuoteInResult = ctx.read(SINGLE_QUOTES_IN_FILTER);
        assertEquals(doubleQuoteInResult, singleQuoteInResult);
    }


    @Test
    public void testJsonPathQuotesJacksonReadroot() throws Exception {
        final Configuration jackson = Configuration.builder().jsonProvider(new JacksonJsonProvider()).mappingProvider(new JacksonMappingProvider()).build();
        final DocumentContext ctx = JsonPath.using(jackson).parse(JSON);

        Object obj = ctx.readRoot(new String[] {DOUBLE_QUOTES_EQUALS_FILTER});
        Assert.assertTrue(jackson.jsonProvider().isArray(obj));
        
        Object doubleQuoteEqualsResult = jackson.jsonProvider().getArrayIndex(obj, 0);
        Assert.assertTrue(jackson.jsonProvider().isMap(doubleQuoteEqualsResult));
        Assert.assertEquals(jackson.jsonProvider().getMapValue(doubleQuoteEqualsResult, "foo"), "bar");

        obj = ctx.readRoot(new String[] {SINGLE_QUOTES_EQUALS_FILTER});
        Assert.assertTrue(jackson.jsonProvider().isArray(obj));
        Object singleQuoteEqualsResult = jackson.jsonProvider().getArrayIndex(obj, 0);
        Assert.assertTrue(jackson.jsonProvider().isMap(singleQuoteEqualsResult));
        assertEquals(doubleQuoteEqualsResult, singleQuoteEqualsResult);

        obj = ctx.readRoot(new String[] {DOUBLE_QUOTES_IN_FILTER});
        Assert.assertTrue(jackson.jsonProvider().isArray(obj));
        Object doubleQuoteInResult = jackson.jsonProvider().getArrayIndex(obj, 0);
        Assert.assertTrue(jackson.jsonProvider().isMap(doubleQuoteInResult));
        assertEquals(doubleQuoteInResult, doubleQuoteEqualsResult);
        
        obj = ctx.readRoot(new String[] {SINGLE_QUOTES_IN_FILTER});
        Assert.assertTrue(jackson.jsonProvider().isArray(obj));
        Object singleQuoteInResult = jackson.jsonProvider().getArrayIndex(obj, 0);
        Assert.assertTrue(jackson.jsonProvider().isMap(singleQuoteInResult));
        assertEquals(singleQuoteInResult, singleQuoteInResult);
    }
    
    @Test
    public void testJsonPathQuotesJacksonJsonNode() throws Exception {
        final Configuration jacksonJsonNode = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider()).mappingProvider(new JacksonMappingProvider()).build();
        final DocumentContext ctx = JsonPath.using(jacksonJsonNode).parse(JSON);

        final ArrayNode doubleQuoteEqualsResult = ctx.read(DOUBLE_QUOTES_EQUALS_FILTER);
        assertEquals("bar", doubleQuoteEqualsResult.get(0).asText());

        final ArrayNode singleQuoteEqualsResult = ctx.read(SINGLE_QUOTES_EQUALS_FILTER);
        assertEquals(doubleQuoteEqualsResult, singleQuoteEqualsResult);

        final ArrayNode doubleQuoteInResult = ctx.read(DOUBLE_QUOTES_IN_FILTER);
        assertEquals(doubleQuoteInResult, doubleQuoteEqualsResult);

        final ArrayNode singleQuoteInResult = ctx.read(SINGLE_QUOTES_IN_FILTER);
        assertEquals(doubleQuoteInResult, singleQuoteInResult);
    }

    @Test
    public void testJsonPathQuotesJacksonJsonNodeReadRoot() throws Exception {
        final Configuration jacksonJsonNode = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider()).mappingProvider(new JacksonMappingProvider()).build();
        final DocumentContext ctx = JsonPath.using(jacksonJsonNode).parse(JSON);

        Object obj = ctx.readRoot(new String[] {DOUBLE_QUOTES_EQUALS_FILTER});
        Assert.assertTrue(jacksonJsonNode.jsonProvider().isArray(obj));
        
        Object doubleQuoteEqualsResult = jacksonJsonNode.jsonProvider().getArrayIndex(obj, 0);
        Assert.assertTrue(jacksonJsonNode.jsonProvider().isMap(doubleQuoteEqualsResult));
        Assert.assertEquals(jacksonJsonNode.jsonProvider().getMapValue(doubleQuoteEqualsResult, "foo"), "bar");

        obj = ctx.readRoot(new String[] {SINGLE_QUOTES_EQUALS_FILTER});
        Assert.assertTrue(jacksonJsonNode.jsonProvider().isArray(obj));
        Object singleQuoteEqualsResult = jacksonJsonNode.jsonProvider().getArrayIndex(obj, 0);
        Assert.assertTrue(jacksonJsonNode.jsonProvider().isMap(singleQuoteEqualsResult));
        assertEquals(doubleQuoteEqualsResult, singleQuoteEqualsResult);

        obj = ctx.readRoot(new String[] {DOUBLE_QUOTES_IN_FILTER});
        Assert.assertTrue(jacksonJsonNode.jsonProvider().isArray(obj));
        Object doubleQuoteInResult = jacksonJsonNode.jsonProvider().getArrayIndex(obj, 0);
        Assert.assertTrue(jacksonJsonNode.jsonProvider().isMap(doubleQuoteInResult));
        assertEquals(doubleQuoteInResult, doubleQuoteEqualsResult);
        
        obj = ctx.readRoot(new String[] {SINGLE_QUOTES_IN_FILTER});
        Assert.assertTrue(jacksonJsonNode.jsonProvider().isArray(obj));
        Object singleQuoteInResult = jacksonJsonNode.jsonProvider().getArrayIndex(obj, 0);
        Assert.assertTrue(jacksonJsonNode.jsonProvider().isMap(singleQuoteInResult));
        assertEquals(singleQuoteInResult, singleQuoteInResult);
    }
    
    @Test
    public void testJsonPathQuotesGson() throws Exception {
        final Configuration gson = Configuration.builder().jsonProvider(new GsonJsonProvider()).mappingProvider(new GsonMappingProvider()).build();
        final DocumentContext ctx = JsonPath.using(gson).parse(JSON);

        final JsonArray doubleQuoteEqualsResult = ctx.read(DOUBLE_QUOTES_EQUALS_FILTER);
        assertEquals("bar", doubleQuoteEqualsResult.get(0).getAsString());

        final JsonArray singleQuoteEqualsResult = ctx.read(SINGLE_QUOTES_EQUALS_FILTER);
        assertEquals(doubleQuoteEqualsResult, singleQuoteEqualsResult);

        final JsonArray doubleQuoteInResult = ctx.read(DOUBLE_QUOTES_IN_FILTER);
        assertEquals(doubleQuoteInResult, doubleQuoteEqualsResult);

        final JsonArray singleQuoteInResult = ctx.read(SINGLE_QUOTES_IN_FILTER);
        assertEquals(doubleQuoteInResult, singleQuoteInResult);
    }
    
    @Test
    public void testJsonPathQuotesJsonOrg() throws Exception {
        final Configuration jsonOrg = Configuration.builder().jsonProvider(new JsonOrgJsonProvider()).mappingProvider(new JsonOrgMappingProvider()).build();
        final DocumentContext ctx = JsonPath.using(jsonOrg).parse(JSON);

        final org.json.JSONArray doubleQuoteEqualsResult = ctx.read(DOUBLE_QUOTES_EQUALS_FILTER);
        assertEquals("bar", doubleQuoteEqualsResult.get(0));

        final org.json.JSONArray singleQuoteEqualsResult = ctx.read(SINGLE_QUOTES_EQUALS_FILTER);
        assertEquals(doubleQuoteEqualsResult.get(0), singleQuoteEqualsResult.get(0));

        final org.json.JSONArray doubleQuoteInResult = ctx.read(DOUBLE_QUOTES_IN_FILTER);
        assertEquals(doubleQuoteInResult.get(0), doubleQuoteEqualsResult.get(0));

        final org.json.JSONArray singleQuoteInResult = ctx.read(SINGLE_QUOTES_IN_FILTER);
        assertEquals(doubleQuoteInResult.get(0), singleQuoteInResult.get(0));
    }

    @Test
    public void testJsonPathQuotesJsonSmart() throws Exception {
        final Configuration jsonSmart = Configuration.builder().jsonProvider(new JsonSmartJsonProvider()).mappingProvider(new JsonSmartMappingProvider()).build();
        final DocumentContext ctx = JsonPath.using(jsonSmart).parse(JSON);

        final net.minidev.json.JSONArray doubleQuoteEqualsResult = ctx.read(DOUBLE_QUOTES_EQUALS_FILTER);
        assertEquals("bar", doubleQuoteEqualsResult.get(0));

        final net.minidev.json.JSONArray singleQuoteEqualsResult = ctx.read(SINGLE_QUOTES_EQUALS_FILTER);
        assertEquals(doubleQuoteEqualsResult, singleQuoteEqualsResult);

        final net.minidev.json.JSONArray doubleQuoteInResult = ctx.read(DOUBLE_QUOTES_IN_FILTER);
        assertEquals(doubleQuoteInResult, doubleQuoteEqualsResult);

        final net.minidev.json.JSONArray singleQuoteInResult = ctx.read(SINGLE_QUOTES_IN_FILTER);
        assertEquals(doubleQuoteInResult, singleQuoteInResult);
    }
}