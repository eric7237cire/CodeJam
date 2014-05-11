using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using System.Text;
using System.Xml;
using System.Globalization;
//using System.Windows.Media;

namespace Utils.geom
{
    public static class GeomXmlExt
    {
        public static string ToStringInvariant(this double number)
        {
            return number.ToString(CultureInfo.InvariantCulture);
        }
        public static void WriteAttributeDouble(this System.Xml.XmlWriter writer, string attributeName, double value)
        {
            writer.WriteAttributeString(attributeName, value.ToStringInvariant());
        }
    }

    enum FigureType
    {
        FreePoint,
        Polygon,
        LineTwoPoints,
        Segment
    }

    class Figure
    {
        public string Name;
        public FigureType Type;
        public List<string> deps = new List<string>();
        public string Style;

    }
    public class Drawing
    {
        public double MinimalVisibleX { get; set; }
        public double MinimalVisibleY { get; set; }
        public double MaximalVisibleX { get; set; }
        public double MaximalVisibleY { get; set; }


        internal List<Figure> figures;
        internal Dictionary<Point<int>, string> pointNames;

        const string POINT_NAME_PREFIX = "PointName";
        const string FIGURE_NAME_PREFIX = "FigureName";
        public const int roundingFactor = 1000;


        internal const string POINT_STYLE_NAME = "ps";
        internal const string POLYGON_STYLE_NAME = "poly";
        internal const string LINE_STYLE_NAME = "lineS";

        public Drawing() {
            Polygons = new List<List<Point<double>>>();
            pointNames = new Dictionary<Point<int>, string>();
            figures = new List<Figure>();

            MaximalVisibleX = 55;
            MinimalVisibleX = -5;
            MaximalVisibleY = 55;
            MinimalVisibleY = -5;
        }

        public void AddPolygon(IEnumerable<Point<int>> polygon)
        {
            AddPolygon(polygon, getName);
        }

        public void AddPolygon<T>(IEnumerable<Point<T>> polygon, Func<Point<T>, string> getName)
        {
            Figure f = new Figure();
            f.Name = FIGURE_NAME_PREFIX + figures.Count;
            f.Style = POLYGON_STYLE_NAME;
            f.Type = FigureType.Polygon;

            foreach(Point<T> p in polygon)
                f.deps.Add(getName(p));
            
            figures.Add(f);
        }

        public void AddAsLine(LineSegment<int> line)
        {
            Figure f = new Figure();
            f.Name = FIGURE_NAME_PREFIX + figures.Count;
            f.Type = FigureType.LineTwoPoints;

            f.deps.Add(getName(line.p1));
            f.deps.Add(getName(line.p2));

            figures.Add(f);
        }

        public void AddAsSeg(LineSegment<int> line)
        {
            AddAsSegMain(line, getName);
        }
        public void AddAsSeg(LineSegment<double> line)
        {
            AddAsSegMain(line, getName);
        }

        public void AddAsSegMain<T>(LineSegment<T> line, Func<Point<T>, string> getName)
        {
            Figure f = new Figure();
            f.Name = FIGURE_NAME_PREFIX + figures.Count;
            f.Type = FigureType.Segment;
            f.Style = LINE_STYLE_NAME;

            f.deps.Add(getName(line.p1));
            f.deps.Add(getName(line.p2));

            figures.Add(f);
        }

        public void AddPoint(Point<int> p)
        {
            getName(p);
        }

        private string getNameBase(Point<int> point)
        {
            if (pointNames.ContainsKey(point))
            {
                return pointNames[point];
            }

            pointNames[point] = "PointName" + pointNames.Count;
            return pointNames[point];
        }

        
        private string getName(Point<double> point)
        {
            return getNameBase(new Point<int>( (int) (point.X * roundingFactor), (int) (point.Y * roundingFactor)));
        }
        private string getName(Point<int> point)
        {
            return getNameBase(new Point<int>((int)(point.X * roundingFactor), (int)(point.Y * roundingFactor)));
        }

        public List<List<Point<double>>> Polygons { get; set; }

    }
    public class GeomXmlWriter
    {
    

        public static void Save(Drawing drawing, string fileName)
        {
            using (var w = XmlWriter.Create(fileName, XmlSettings))
            {
                SaveDrawing(drawing, w);
            }

            //string serialized = SaveDrawing(drawing);
            //File.WriteAllText(fileName, serialized);
        }

        public static string SaveDrawing(Drawing drawing)
        {
            return WriteUsingXmlWriter(w => SaveDrawing(drawing, w));
        }

        public static void SaveDrawing(Drawing drawing, XmlWriter writer)
        {
            GeomXmlWriter serializer = new GeomXmlWriter();
            serializer.Write(drawing, writer);
        }

        public static void SaveDrawing(Drawing drawing, Stream stream)
        {
            using (var writer = XmlWriter.Create(stream, XmlSettings))
            {
                SaveDrawing(drawing, writer);
            }
        }

       

        static XmlWriterSettings XmlSettings
        {
            get
            {
                return new XmlWriterSettings()
                {
                    Indent = true,
                    Encoding = Encoding.UTF8,
                    CloseOutput = true
                };
            }
        }

        public static string WriteUsingXmlWriter(Action<XmlWriter> writerConsumer)
        {
            var sb = new StringBuilder();
            using (var w = XmlWriter.Create(sb, XmlSettings))
            {
                writerConsumer(w);
            }

            return sb.ToString();
        }

        void Write(Drawing drawing, XmlWriter writer)
        {
           
            writer.WriteStartDocument();
            writer.WriteStartElement("Drawing");
           // writer.WriteAttributeDouble("Version", drawing.Version);
          //  writer.WriteAttributeString("Creator", System.Windows.Application.Current.ToString());
            WriteCoordinateSystem(drawing, writer);
            WriteStyles(drawing, writer);
            WriteFigureList(drawing, writer);
            writer.WriteEndElement();
            writer.WriteEndDocument();
        }

        void WriteCoordinateSystem(Drawing drawing, XmlWriter writer)
        {
            writer.WriteStartElement("Viewport");
            writer.WriteAttributeDouble("Left", drawing.MinimalVisibleX);
            writer.WriteAttributeDouble("Top", drawing.MaximalVisibleY);
            writer.WriteAttributeDouble("Right", drawing.MaximalVisibleX);
            writer.WriteAttributeDouble("Bottom", drawing.MinimalVisibleY);

            /*
            var backgroundBrush = drawing.Canvas.Background as SolidColorBrush;
            if (backgroundBrush != null && backgroundBrush.Color != Colors.White)
            {
                writer.WriteAttributeString("Color", backgroundBrush.Color.ToString());
            }

            if (drawing.CoordinateGrid.Locked)
            {
                writer.WriteAttributeBool("Locked", true);
            }
            
            if (drawing.CoordinateGrid.Visible)
            {
                writer.WriteAttributeBool("Grid", true);
                writer.WriteAttributeBool("Axes", drawing.CoordinateGrid.ShowAxes);
            }*/

            writer.WriteEndElement();
        }


        public virtual void WriteStyles(Drawing drawing, XmlWriter writer)
        {
            //<PointStyle Size="10" Fill="" ="true" Color="#" ="1" Name="1" />
            writer.WriteStartElement("Styles");

            writer.WriteStartElement("PointStyle");
            writer.WriteAttributeString("Size", "7");
            writer.WriteAttributeString("Fill", "#FFF8EABA");
            writer.WriteAttributeString("IsFilled", "True");
            writer.WriteAttributeString("Color", "#FF000000");
            writer.WriteAttributeString("StrokeWidth", "5");
            writer.WriteAttributeString("Name", Drawing.POINT_STYLE_NAME);
            writer.WriteEndElement();


            //<ShapeStyle Fill="" IsFilled="true" Color="" StrokeWidth="1" Name="5" />
            //Color is opagque r g b
            writer.WriteStartElement("ShapeStyle");
            writer.WriteAttributeString("Size", "10");
            writer.WriteAttributeString("Fill", "#FFF5A79E");
            writer.WriteAttributeString("IsFilled", "True");
            writer.WriteAttributeString("Color", "#FF000000");
            writer.WriteAttributeString("StrokeWidth", "1");
            writer.WriteAttributeString("Name", Drawing.POLYGON_STYLE_NAME);
            writer.WriteEndElement();

            //<LineStyle Color="#FF000000" StrokeWidth="1" Name="4" />
            writer.WriteStartElement("LineStyle");
            writer.WriteAttributeString("StrokeWidth", "1");
            writer.WriteAttributeString("Color", "#FFFF0000");            
            writer.WriteAttributeString("Name", Drawing.LINE_STYLE_NAME);
            writer.WriteEndElement();


            writer.WriteEndElement();
        }

        

       

        public void WriteFigureList(Drawing drawing, XmlWriter writer)
        {
            writer.WriteStartElement("Figures");

            foreach(var pn in drawing.pointNames)
            {
                writer.WriteStartElement("FreePoint");
                writer.WriteAttributeString("Name", pn.Value);
                writer.WriteAttributeString("Style", Drawing.POINT_STYLE_NAME);
                writer.WriteAttributeDouble("X", ( (double)pn.Key.X ) / Drawing.roundingFactor);
                writer.WriteAttributeDouble("Y", ((double)pn.Key.Y) / Drawing.roundingFactor);
                writer.WriteEndElement();
            }

            foreach(var fig in drawing.figures)
            {
                writer.WriteStartElement(fig.Type.ToString());
                writer.WriteAttributeString("Name", fig.Name);
                if (fig.Style != null)
                writer.WriteAttributeString("Style", fig.Style);

                foreach (string dep in fig.deps)
                {
                    writer.WriteStartElement("Dependency");
                    writer.WriteAttributeString("Name", dep);
                    writer.WriteEndElement();
                }
                writer.WriteEndElement();
            }

            /*int pointName = 0;
            int polyName = 0;
            foreach( List<Point<double>> polygon in drawing.Polygons)
            {

                int startPointName = pointName;
                foreach( Point<double> point in polygon)
                {
                    //<FreePoint Name="A" Style="1" X="1600" Y="2700" />
                    writer.WriteStartElement("FreePoint");
                    writer.WriteAttributeString("Name", "Point" + pointName++);
                    writer.WriteAttributeString("Style", POINT_STYLE_NAME);
                    writer.WriteAttributeDouble("X", point.X);
                    writer.WriteAttributeDouble("Y", point.Y);
                    writer.WriteEndElement();
                }
                int endPointName = pointName;

                writer.WriteStartElement("Polygon");
                writer.WriteAttributeString("Name", "PolyName" + polyName);
                writer.WriteAttributeString("Style", POLYGON_STYLE_NAME);

                for (int pn = startPointName; pn < endPointName; ++pn )
                {
                    writer.WriteStartElement("Dependency");
                    writer.WriteAttributeString("Name", "Point" + pn);
                    writer.WriteEndElement();
                }
                writer.WriteEndElement();

                    
            }*/
            
            writer.WriteEndElement();
        }


       
    }
}


