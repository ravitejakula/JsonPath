package com.jayway.jsonpath;

import static com.jayway.jsonpath.JsonPath.using;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class PredicateTest extends BaseTest {

    private static ReadContext reader = using(GSON_CONFIGURATION).parse(JSON_DOCUMENT);

    @Test
    public void predicates_filters_can_be_applied() {
        Predicate booksWithISBN = new Predicate() {
            @Override
            public boolean apply(PredicateContext ctx) {
                return ctx.item(Map.class).containsKey("isbn");
            }
        };

        assertThat(reader.read("$.store.book[?].isbn", List.class, booksWithISBN)).containsOnly("0-395-19395-8", "0-553-21311-3");
    }

    // TODO KR fix this
    @Ignore
    @Test
    public void predicates_filters_can_be_applied_readRoot() {
        Predicate booksWithISBN = new Predicate() {
            @Override
            public boolean apply(PredicateContext ctx) {
                return ctx.item(Map.class).containsKey("isbn");
            }
        };

        // Object result = reader.readRoot("$.store.book[?].isbn", booksWithISBN);
        // sh*y GSON does not work here
        Object result = using(JACKSON_JSON_NODE_CONFIGURATION_FOR_READROOT).parse(JSON_DOCUMENT).readRoot(new String[] { "$.store.book[?].isbn" },
                booksWithISBN);
        Assert.assertNotNull(result);
        Assert.assertTrue(Configuration.defaultConfiguration().jsonProvider().isMap(result));
        Object storeObj = Configuration.defaultConfiguration().jsonProvider().getMapValue(result, "store");
        Assert.assertTrue(Configuration.defaultConfiguration().jsonProvider().isMap(storeObj));
        Object booksObj = Configuration.defaultConfiguration().jsonProvider().getMapValue(storeObj, "book");
        Object elem1 = Configuration.defaultConfiguration().jsonProvider().getArrayIndex(booksObj, 2);
        Object elem2 = Configuration.defaultConfiguration().jsonProvider().getArrayIndex(booksObj, 3);

        Assert.assertEquals(Configuration.defaultConfiguration().jsonProvider().getMapValue(elem1, "isbn"), "0-553-21311-3");
        Assert.assertEquals(Configuration.defaultConfiguration().jsonProvider().getMapValue(elem2, "isbn"), "0-395-19395-8");
    }
   
    @Test(expected=RuntimeException.class)
    public void predicates_filters_throws_exception_without_suppress_exceptions_for_readroot() {
        Predicate booksWithISBN = new Predicate() {
            @Override
            public boolean apply(PredicateContext ctx) {
                return ctx.item(Map.class).containsKey("isbn");
            }
        };
        Configuration conf = Configuration.builder().options(Option.SUPPRESS_EXCEPTIONS).build();
        DocumentContext dc = using(conf).parse(JSON_DOCUMENT);
        
        Object root = dc.readRoot(new String[] {"$.store.book[?].isbn"}, booksWithISBN);
        Assert.assertTrue(conf.jsonProvider().isMap(root));
        Assert.assertEquals(root.toString(), "{}");
        
        //Remove the SuppressException option.
        conf = conf.defaultConfiguration();
        dc = using(conf).parse(JSON_DOCUMENT);
        dc.readRoot(new String[] {"$.store.book[?].isbn"}, booksWithISBN);
    }
    
}
