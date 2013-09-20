package codejam.y2009.round_final.min_perimeter;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.PointLong;
import codejam.utils.utils.CombinationIterator;

import com.google.common.base.Preconditions;

public class BruteForce {
    final static Logger log = LoggerFactory.getLogger(BruteForce.class);

    

    static double minPerim(List<PointLong> list) {
        Preconditions.checkArgument(list.size() <= 62);

        CombinationIterator comIt = new CombinationIterator(list.size(), 3);

        double min = Double.MAX_VALUE;

        int count = 0;

        while (comIt.hasNext()) {
            ++count;
            if (count % 1000000 == 0)
                log.debug("Count {}", count);
            Long com = comIt.next();

            PointLong[] arr = new PointLong[3];
            for (int i = 0; i < 3; ++i) {
                long bit = Long.highestOneBit(com);
                int index = Long.numberOfTrailingZeros(bit);
                PointLong p = list.get(index);
                com = com & ~bit;
                arr[i] = p;
            }

            double area = arr[0].distance(arr[1]) + arr[0].distance(arr[2])
                    + arr[1].distance(arr[2]);
            min = Math.min(area, min);
        }

        return min;
    }

}
