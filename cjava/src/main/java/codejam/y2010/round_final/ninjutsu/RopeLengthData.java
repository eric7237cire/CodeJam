package codejam.y2010.round_final.ninjutsu;

import codejam.utils.geometry.PointInt;

public class RopeLengthData
{

        final int pivotIdx;
        final int cutPointIdx;
        final int bends;
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + bends;
            result = prime * result + cutPointIdx;
            result = prime * result + pivotIdx;
            return result;
        }
        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            RopeLengthData other = (RopeLengthData) obj;
            if (bends != other.bends)
                return false;
            if (cutPointIdx != other.cutPointIdx)
                return false;
            if (pivotIdx != other.pivotIdx)
                return false;
            return true;
        }
        public RopeLengthData(int pivotIdx, int cutPointIdx, int bends) {
            super();
            this.pivotIdx = pivotIdx;
            this.cutPointIdx = cutPointIdx;
            this.bends = bends;
        }


}
