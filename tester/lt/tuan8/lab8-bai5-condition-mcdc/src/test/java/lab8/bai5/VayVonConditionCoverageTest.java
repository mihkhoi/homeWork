package lab8.bai5;

import org.testng.Assert;
import org.testng.annotations.Test;

public
class VayVonConditionCoverageTest {

    @Test(description = "Condition Coverage - Tat ca dieu kien don deu True") public void testConditionCoverage_AllTrue() {
        boolean actual = VayVon.duDieuKienVay(22, 10_000_000, true, 700);

        Assert.assertTrue(
            actual,
            "Khi tat ca dieu kien don deu True thi ket qua phai la true");
    }

    @Test(description = "Condition Coverage - Tat ca dieu kien don deu False") public void testConditionCoverage_AllFalse() {
        boolean actual = VayVon.duDieuKienVay(21, 9_000_000, false, 650);

        Assert.assertFalse(
            actual,
            "Khi tat ca dieu kien don deu False thi ket qua phai la false");
    }
}
