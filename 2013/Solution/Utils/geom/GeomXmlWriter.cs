using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using System.Reflection;
using System.Xml;
using System.Globalization;
using System.Text.RegularExpressions;

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

    class Color
    {
        public string colorStr { get; private set; }

        public static Color create(int r, int g, int b)
        {
            string str = "#FF" + r.ToString("X2") + g.ToString("X2") + b.ToString("X2")  ;
            return new Color(str);
        }

        public static Color create(string color)
        {
            Preconditions.checkState(color.Length == 9, color);
            Preconditions.checkState(Regex.IsMatch(color, @"\#[0-9A-F]{8}"));
            return new Color(color);
        }

        private Color(string colorStr)
        {
            this.colorStr = colorStr;
        }

        public override int GetHashCode()
        {
            return colorStr.GetHashCode();
        }

        public override bool Equals(object obj)
        {
            Color rhs = obj as Color;
            if (rhs == null)
                return false;
            return colorStr.Equals(rhs.colorStr);
        }

        public override string ToString()
        {
            return colorStr;
        }
    }

    abstract class Style<T> where T : Style<T>, new()
    {
        private static Dictionary<Color, T> allStyles;
        protected internal Color color { get; protected set; }
        protected internal string styleName { get; protected set; }

        protected abstract string getStyleBaseName();

        static Style()
        {
            allStyles = new Dictionary<Color, T>();
        }
        public static T getStyle(string colorStr, Dictionary<Color, T> styleMap ) 
        {
            Color color = Color.create(colorStr);

            if (styleMap.ContainsKey(color))
            {
                return styleMap[color];
            }

            T style = new T();
            style.styleName = style.getStyleBaseName() + styleMap.Count;
            style.color = color;

            styleMap[color] = style;

            return style;
        }

        public static T getStyle(string colorStr)
        {
            return getStyle(colorStr, allStyles);
        }

        public static IEnumerable<T> getAllStyles()
        {
            foreach (var kv in allStyles)
            {
                yield return kv.Value;
            }
        }
    }

    //<PointStyle Size="7" Fill="#FFF8EABA" IsFilled="True" Color="#FF000000" StrokeWidth="5" Name="ps" />
    class PointStyle : Style<PointStyle> 
    {
        
        protected override string getStyleBaseName()
        {
            return "PointStyle";
        }        

        public void Serialize(XmlWriter writer)
        {
            writer.WriteStartElement("PointStyle");
            writer.WriteAttributeString("Size", "7");
            writer.WriteAttributeString("Fill", "#FFF8EABA");
            writer.WriteAttributeString("IsFilled", "True");
            writer.WriteAttributeString("Color", color.ToString());
            writer.WriteAttributeString("StrokeWidth", "5");
            writer.WriteAttributeString("Name", styleName);
            writer.WriteEndElement();
        }
    }

    //<ShapeStyle Size="10" Fill="#FF00FF00" IsFilled="True" Color="#FF000000" StrokeWidth="1" Name="poly0" />
    class ShapeStyle : Style<ShapeStyle>
    {

        protected override string getStyleBaseName()
        {
            return "ShapeStyle";
        }

        public void Serialize(XmlWriter writer)
        {
            writer.WriteStartElement("ShapeStyle");
            writer.WriteAttributeString("Size", "10");
            writer.WriteAttributeString("Fill",  color.ToString());
            writer.WriteAttributeString("IsFilled", "True");
            writer.WriteAttributeString("Color", "#FF000000");
            writer.WriteAttributeString("StrokeWidth", "1");
            writer.WriteAttributeString("Name", styleName);
            writer.WriteEndElement();
        }
    }

    class LineStyle : Style<LineStyle>
    {
        protected override string getStyleBaseName()
        {
            return "LineStyle";
        }
        //<LineStyle Color="#FF000000" StrokeWidth="1" Name="4" />

        public void Serialize(XmlWriter writer)
        {
            //<LineStyle Color="#FF000000" StrokeWidth="1" Name="4" />
            writer.WriteStartElement("LineStyle");           
            writer.WriteAttributeString("Color", color.ToString());
            writer.WriteAttributeString("StrokeWidth", "2");
            writer.WriteAttributeString("Name", styleName);
            writer.WriteEndElement();
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

        public virtual void Serialize (XmlWriter writer)
        {
            writer.WriteStartElement(Type.ToString());
            writer.WriteAttributeString("Name", Name);
            if (Style != null)
                writer.WriteAttributeString("Style", Style);

            foreach (string dep in deps)
            {
                writer.WriteStartElement("Dependency");
                writer.WriteAttributeString("Name", dep);
                writer.WriteEndElement();
            }
            writer.WriteEndElement();
        }
    }

    class PointFigure : Figure
    {
        internal Point<double> pn;

        public override void Serialize(XmlWriter writer)
        {
            writer.WriteStartElement("FreePoint");
            writer.WriteAttributeString("Name", Name);
            writer.WriteAttributeString("Style", Style);
            writer.WriteAttributeDouble("X", pn.X);
            writer.WriteAttributeDouble("Y", pn.Y);
            writer.WriteEndElement();
        }
    }


    public class Drawing
    {
        public double MinimalVisibleX { get; set; }
        public double MinimalVisibleY { get; set; }
        public double MaximalVisibleX { get; set; }
        public double MaximalVisibleY { get; set; }


        internal List<Figure> figures;

        
        internal Dictionary<Point<double>, string> pointNames;

        const string POINT_NAME_PREFIX = "PointName";
        const string FIGURE_NAME_PREFIX = "FigureName";
        public const long roundingFactor = 1000;

        const string DEFAULT_SHAPE_COLOR = "#FFF5A79E";
        const string DEFAULT_LINE_COLOR = "#FF000000";
        const string DEFAULT_POINT_COLOR = "#FF000000";
        

        public Drawing()
        {
            Polygons = new List<List<Point<double>>>();
            pointNames = new Dictionary<Point<double>, string>();
            figures = new List<Figure>();
            

            MaximalVisibleX = 55;
            MinimalVisibleX = -5;
            MaximalVisibleY = 55;
            MinimalVisibleY = -5;
        }

        public void AddPolygon(IEnumerable<Point<int>> polygon, string color = null)
        {
            color = color ?? DEFAULT_SHAPE_COLOR;

            AddPolygon(polygon, ShapeStyle.getStyle(color).styleName, getName);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="polygon"></param>
        /// <param name="styleName"></param>
        /// <param name="getName">Used to work with any Point of T</param>
        public void AddPolygon<T>(IEnumerable<Point<T>> polygon, string styleName, Func<Point<T>, string, string> getName) where T : IComparable<T>
        {
            Figure f = new Figure();
            f.Name = FIGURE_NAME_PREFIX + figures.Count;
            f.Style = styleName;
            f.Type = FigureType.Polygon;

            foreach (Point<T> p in polygon)
                f.deps.Add(getName(p, null));

            figures.Add(f);
        }

        public void AddAsLine(LineSegment<int> line, string color = null)
        {
            color = color ?? DEFAULT_SHAPE_COLOR;

            Figure f = new Figure();
            f.Name = FIGURE_NAME_PREFIX + figures.Count;
            f.Type = FigureType.LineTwoPoints;
            f.Style = LineStyle.getStyle(color).styleName;

            f.deps.Add(getName(line.p1));
            f.deps.Add(getName(line.p2));

            figures.Add(f);
        }

        public void AddAsLine(LineSegment<double> line, string color = null)
        {
            color = color ?? DEFAULT_SHAPE_COLOR;

            Figure f = new Figure();
            f.Name = FIGURE_NAME_PREFIX + figures.Count;
            f.Type = FigureType.LineTwoPoints;
            f.Style = LineStyle.getStyle(color).styleName;

            f.deps.Add(getName(line.p1));
            f.deps.Add(getName(line.p2));

            figures.Add(f);
        }

        public void AddAsSeg(LineSegment<int> line)
        {
            AddAsSegMain(line.p1, line.p2, getName);
        }
        public void AddAsSeg(LineSegment<double> line)
        {
            AddAsSegMain(line.p1, line.p2, getName);
        }
        public void AddAsSeg(Point<int> p1, Point<int> p2, string color = null)
        {
            AddAsSegMain(p1, p2, getName, color);
        }

        public void AddAsSegMain<T>(Point<T> p1, Point<T> p2, Func<Point<T>, string, string> getName, string color = null) where T : IComparable<T>
        {
            color = color ?? DEFAULT_SHAPE_COLOR;

            
            Figure f = new Figure();
            f.Name = FIGURE_NAME_PREFIX + figures.Count;
            f.Type = FigureType.Segment;
            f.Style = LineStyle.getStyle(color).styleName;

            f.deps.Add(getName(p1, null));
            f.deps.Add(getName(p2, null));

            figures.Add(f);
        }

        public void AddPoint(Point<int> p, string color = null)
        {
            getName(p, color);
        }

        private string getNameBase(Point<double> point, string color = null)
        {
            if (pointNames.ContainsKey(point))
            {
                return pointNames[point];
            }

            string name = "PointName" + pointNames.Count;
            pointNames[point] = name;

            color = color ?? DEFAULT_POINT_COLOR;
                        
            PointFigure f = new PointFigure();
            f.Name = name;
            f.Style = PointStyle.getStyle(color).styleName;
            f.Type = FigureType.FreePoint;
            f.pn = point;

            figures.Add(f);

            return name;
        }


        private string getName(Point<double> point, string color = null)
        {
            return getNameBase(point, color);
        }
        private string getName(Point<int> point, string color = null)
        {
            return getNameBase(new Point<double>(point.X , point.Y ), color);
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

            foreach(var pointStyle in PointStyle.getAllStyles())
            {
                pointStyle.Serialize(writer);
            }

            foreach (var style in ShapeStyle.getAllStyles())
            {
                style.Serialize(writer);
            }
            //<ShapeStyle Fill="" IsFilled="true" Color="" StrokeWidth="1" Name="5" />
            //Color is opagque r g b

            foreach (var style in LineStyle.getAllStyles())
            {
                style.Serialize(writer);
            }

            writer.WriteEndElement();
        }





        public void WriteFigureList(Drawing drawing, XmlWriter writer)
        {
            writer.WriteStartElement("Figures");

            /*
            foreach (var pn in drawing.pointNames)
            {
               
            }*/

            foreach (var fig in drawing.figures)
            {
                fig.Serialize(writer);
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


