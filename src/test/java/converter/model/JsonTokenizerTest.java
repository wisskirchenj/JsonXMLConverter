package converter.model;

import jdk.jshell.Snippet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class JsonTokenizerTest {

    private final String longJsonTextNonArray = """
            "inner7": "value6",
            "inner8": {
              "attr3": "value7"
            },
            "inner9": {
              "attr4": "value8",
              "inner9": "value9",
              "something": "value10"
            },
            "inner10": {
              "@attr5": "",
              "#inner10": null
            },
            "inner11": {
              "@attr11": "value11",
              "#inner11": {
                "inner12": {
                  "@attr12": "value12",
                  "#inner12": {
                    "inner13": {
                      "@attr13": "value13",
                      "#inner13": {
                        "inner14": "v14"
                      }
                    }
                  }
                }
              }
            },
            "inner15": "",
            "inner16": {
              "somekey": "keyvalue",
              "inner16": "notnull"
            },
            "crazyattr1": {
              "@attr1": "123",
              "#crazyattr1": "v15"
            },
            "crazyattr2": {
              "@attr1": "123.456",
              "#crazyattr2": "v16"
            }""".indent(6);

    private final String longJsonTextArray = """
            "array2": [
              "",
              null,
              "123",
              "123.456",
              {
                "key1": "value1",
                "key2": {
                  "@attr": "value2",
                  "#key2": "value3"
                }
              },
              {
                "@attr2": "value4",
                "#element": "value5"
              },
              {
                "attr3": "value4",
                "elem": "value5"
              },
              {
                "deep": {
                  "@deepattr": "deepvalue",
                  "#deep": [
                    "1",
                    "2",
                    "3"
                  ]
                }
              }
            ]""".indent(6);

    private JsonTokenizer arrayTokenizer;
    private JsonTokenizer nonArrayTokenizer;

    @BeforeEach
    void setUp() {
        nonArrayTokenizer = new JsonTokenizer(longJsonTextNonArray);
        arrayTokenizer = new JsonTokenizer(longJsonTextArray);
    }

    @Test
    @DisplayName("array get next token")
    void getNextTokenArray() {
        int expectedArrayTokens = 1;
        assertEquals(expectedArrayTokens, arrayTokenizer.asQueue().size());
        assertEquals(expectedArrayTokens, arrayTokenizer.asList().size());
        String modified = arrayTokenizer.asQueue().poll();
        assertNotNull(modified);
        JsonTokenizer tokenizer = new JsonTokenizer(
                modified.substring(modified.indexOf('{') + 1,modified.lastIndexOf(',')));
        int expectedNonArrayTokens = 8;
        assertEquals(expectedNonArrayTokens, tokenizer.asQueue().size());
        assertEquals(expectedNonArrayTokens, tokenizer.asList().size());
        for (String token : tokenizer.asQueue()) {
            System.out.println(token + "\n\n");
        }
    }

    @Test
    @DisplayName("non Array get next token")
    void getNextTokenNonArray() {
        int expectedNonArrayTokens = 9;
        assertEquals(expectedNonArrayTokens, nonArrayTokenizer.asQueue().size());
        assertEquals(expectedNonArrayTokens, nonArrayTokenizer.asList().size());
        for (String token : nonArrayTokenizer.asQueue()) {
            if (token.contains("inner11")) {
                assertEquals(16, token.split("\n").length);
            }
            System.out.println(token + "\n\n");
        }
    }

    @Test
    void modifyArrayToken() {
        int expectedArrayTokens = 1;
        assertEquals(expectedArrayTokens, arrayTokenizer.asQueue().size());
        assertEquals(expectedArrayTokens, arrayTokenizer.asList().size());
        String modified = arrayTokenizer.asQueue().poll();
        System.out.println(modified);
        assertNotNull(modified);
        assertFalse(modified.endsWith("]"));
        // contains 9 times the new inserted tag "element" !?
        assertEquals(10, modified.split("element").length);
    }
}