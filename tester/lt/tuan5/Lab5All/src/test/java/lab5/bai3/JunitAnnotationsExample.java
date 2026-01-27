
package lab5.bai3;

import org.junit.*;

public
class JunitAnnotationsExample {

    @BeforeClass public static void beforeAll() {
        System.out.println("BeforeClass - 1 lần trước tất cả");
    }

    @AfterClass public static void afterAll() {
        System.out.println("AfterClass - 1 lần sau tất cả");
    }

    @Before public void beforeEach() {
        System.out.println("Before - trước mỗi test");
    }

    @After public void afterEach() {
        System.out.println("After - sau mỗi test");
    }

    @Test public void test1() {
        System.out.println("Running test1");
        Assert.assertTrue(5 > 1);
    }

    @Ignore("Demo ignore")
        @Test public void testIgnored() {
        Assert.fail();
    }

    @Test public void test2() {
        System.out.println("Running test2");
        Assert.assertEquals(10, 5 + 5);
    }
}
