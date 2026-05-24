package it.unicam.cs.mpgc.rpg126114.model.humor;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HumoralBalanceTest {

    @Test
    void aBalancedBodyHasNoImbalance() {
        HumoralBalance balanced = HumoralBalance.balanced();
        assertEquals(0, balanced.totalImbalance());
        assertTrue(balanced.isWithinTolerance(0));
    }

    @Test
    void shiftIsClampedAndLeavesTheOriginalUntouched() {
        HumoralBalance original = HumoralBalance.balanced();
        HumoralBalance shifted = original.shift(Humor.SANGUE, 9);

        assertEquals(HumoralBalance.MAX_LEVEL, shifted.level(Humor.SANGUE));
        assertEquals(HumoralBalance.IDEAL_LEVEL, original.level(Humor.SANGUE));
    }

    @Test
    void deviationReportsExcessAndDeficiency() {
        HumoralBalance balance = HumoralBalance.of(Map.of(Humor.SANGUE, 8, Humor.FLEMMA, 3));
        assertEquals(3, balance.deviation(Humor.SANGUE));
        assertEquals(-2, balance.deviation(Humor.FLEMMA));
        assertEquals(0, balance.deviation(Humor.BILE_GIALLA));
    }

    @Test
    void mostImbalancedPicksTheLargestDeviation() {
        HumoralBalance balance = HumoralBalance.of(Map.of(Humor.SANGUE, 6, Humor.BILE_NERA, 9));
        assertEquals(Humor.BILE_NERA, balance.mostImbalanced());
    }

    @Test
    void toleranceConsidersEveryHumor() {
        HumoralBalance balance = HumoralBalance.of(Map.of(Humor.BILE_GIALLA, 7));
        assertFalse(balance.isWithinTolerance(1));
        assertTrue(balance.isWithinTolerance(2));
    }

    @Test
    void equalityIsBasedOnLevels() {
        assertEquals(HumoralBalance.of(Map.of(Humor.SANGUE, 7)),
                HumoralBalance.balanced().shift(Humor.SANGUE, 2));
    }
}
