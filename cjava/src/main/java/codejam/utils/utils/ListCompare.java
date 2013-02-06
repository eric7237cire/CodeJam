package codejam.utils.utils;

import java.util.Comparator;
import java.util.List;

import com.google.common.base.Preconditions;

public class ListCompare
{

    public static class ListStringComparator implements Comparator<List<String>>
    {
        @Override
        public int compare(List<String> l1, List<String> l2)
        {

            if (l1.size() != l2.size())
                return Integer.compare(l1.size(), l2.size());

            Preconditions.checkState(l1.size() == l2.size());

            for (int i = 0; i < l1.size(); ++i)
            {
                int cmp = l1.get(i).compareTo(l2.get(i));
                if (cmp == 0)
                    continue;

                return cmp;
            }

            return 0;
        }

    }
}
