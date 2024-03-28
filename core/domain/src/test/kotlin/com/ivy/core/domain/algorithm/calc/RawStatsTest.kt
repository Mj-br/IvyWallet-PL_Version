package com.ivy.core.domain.algorithm.calc

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.ivy.core.persistence.algorithm.calc.CalcTrn
import com.ivy.data.transaction.TransactionType
import org.junit.jupiter.api.Test
import java.time.Instant

internal class RawStatsTest {


    @Test
    fun `GIVEN transactions WHEN rawStats THEN returns RawStats values`() {

        //Create some different times for transactions
        val firstTime = Instant.MIN
        val secondTimeTenSecondsLater = firstTime.plusSeconds(10)
        val thirdTimeTwentySecondsLater = secondTimeTenSecondsLater.plusSeconds(10)
        val fourthTimeTwentySecondsLater = thirdTimeTwentySecondsLater.plusSeconds(10)

        val trns = listOf(
            CalcTrn(10.0, "USD", TransactionType.Income, firstTime),
            CalcTrn(10.0, "USD", TransactionType.Income, secondTimeTenSecondsLater),
            CalcTrn(10.0, "EUR", TransactionType.Income, thirdTimeTwentySecondsLater),
            CalcTrn(10.0, "EUR", TransactionType.Expense, fourthTimeTwentySecondsLater)
        )

        val rawStats = rawStats(trns)

        assertThat(rawStats.expenses).isEqualTo(mapOf("EUR" to 10.0))
        assertThat(rawStats.expensesCount).isEqualTo(1)
        assertThat(rawStats.newestTrnTime).isEqualTo(fourthTimeTwentySecondsLater)
        assertThat(rawStats.incomes).isEqualTo(mapOf("USD" to 20.0, "EUR" to 10.0))
        assertThat(rawStats.incomesCount).isEqualTo(3)


    }

}