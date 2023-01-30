/**
 *
 */
package com.xdclass.shop.test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author daniel
 * @version 2020/03/14
 */
public class RegTest {

    @Test
    public void testZzbds() {
        String regex = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}(\\d|x|X)$";
        String str = "100222299111032434";
        assertEquals(true, str.matches(regex));
    }
}
