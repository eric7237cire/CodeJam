using System;
using Round2_2014.Problem3;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;

namespace CodeJam._2014.Round2
{
    

	public class BuildingIndexes
        {
		            public SortedSet<Building> X0;
			            public SortedSet<Building> X1;
			            public SortedSet<Building> Y0;
			            public SortedSet<Building> Y1;
			            

            public BuildingIndexes()
            {
                
                X0 = new SortedSet<Building>(Comparer<Building>.Create((b1, b2) =>
                {
					
					return b1.X0.CompareTo(b2.X0);
                    
                }));
                    
                X1 = new SortedSet<Building>(Comparer<Building>.Create((b1, b2) =>
                {
					
					return b1.X1.CompareTo(b2.X1);
                    
                }));
                    
                Y0 = new SortedSet<Building>(Comparer<Building>.Create((b1, b2) =>
                {
					
					return b1.Y0.CompareTo(b2.Y0);
                    
                }));
                    
                Y1 = new SortedSet<Building>(Comparer<Building>.Create((b1, b2) =>
                {
					
					return b1.Y1.CompareTo(b2.Y1);
                    
                }));
                
            }

						public SortedSet<Building> GetViewUpToX0(int upperInc)
			{
				Building b = new Building();
				b.X0 = upperInc;

				Building bLower = new Building();
				bLower.X0 = 0;

				return X0.GetViewBetween(bLower, b);
			}

			public SortedSet<Building> GetViewFromX0(int lowerInc)
			{
				Building b = new Building();
				b.X0 = lowerInc;

				Building bUpper = new Building();
				bUpper.X0 = int.MaxValue;

				return X0.GetViewBetween(b, bUpper);
			}

						public SortedSet<Building> GetViewUpToX1(int upperInc)
			{
				Building b = new Building();
				b.X1 = upperInc;

				Building bLower = new Building();
				bLower.X1 = 0;

				return X1.GetViewBetween(bLower, b);
			}

			public SortedSet<Building> GetViewFromX1(int lowerInc)
			{
				Building b = new Building();
				b.X1 = lowerInc;

				Building bUpper = new Building();
				bUpper.X1 = int.MaxValue;

				return X1.GetViewBetween(b, bUpper);
			}

						public SortedSet<Building> GetViewUpToY0(int upperInc)
			{
				Building b = new Building();
				b.Y0 = upperInc;

				Building bLower = new Building();
				bLower.Y0 = 0;

				return Y0.GetViewBetween(bLower, b);
			}

			public SortedSet<Building> GetViewFromY0(int lowerInc)
			{
				Building b = new Building();
				b.Y0 = lowerInc;

				Building bUpper = new Building();
				bUpper.Y0 = int.MaxValue;

				return Y0.GetViewBetween(b, bUpper);
			}

						public SortedSet<Building> GetViewUpToY1(int upperInc)
			{
				Building b = new Building();
				b.Y1 = upperInc;

				Building bLower = new Building();
				bLower.Y1 = 0;

				return Y1.GetViewBetween(bLower, b);
			}

			public SortedSet<Building> GetViewFromY1(int lowerInc)
			{
				Building b = new Building();
				b.Y1 = lowerInc;

				Building bUpper = new Building();
				bUpper.Y1 = int.MaxValue;

				return Y1.GetViewBetween(b, bUpper);
			}

			   

            public void Add(Building b)
            {
			               X0.Add(b);
				               X1.Add(b);
				               Y0.Add(b);
				               Y1.Add(b);
				            }
            public void Remove(Building b)
            {
                
			                Preconditions.checkState(X0.Remove(b));
               
				                Preconditions.checkState(X1.Remove(b));
               
				                Preconditions.checkState(Y0.Remove(b));
               
				                Preconditions.checkState(Y1.Remove(b));
               
				            }
        }
}
