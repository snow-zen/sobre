package org.snowzen.review;

import java.time.LocalDateTime;

/**
 * @author snow-zen
 */
public class ReviewTimeUtil {

    public static LocalDateTime nextReviewTime(LocalDateTime preReviewTime, ReviewStrategy reviewStrategy) {
        LocalDateTime next = null;
        if (reviewStrategy == ReviewStrategy.EVERY_DAY) {
            next = preReviewTime.plusDays(1);
        }
        return next;
    }
}
