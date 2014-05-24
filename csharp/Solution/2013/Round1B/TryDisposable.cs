using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Osmos
{
    class ReadFile : IDisposable
    {
        private string fileName;


        public string FileName
        {
            get { Console.WriteLine("Returning filename " + fileName); return fileName; }

            set { Console.WriteLine("Setting filename " + value); fileName = value; }
        }

        public string FileName2 { get; set; }

        internal void stuff()
        {
            Console.WriteLine("Stuff");
            fileName = "who";
            Console.WriteLine(FileName);
            FileName = "dat";
        }

        void readFile()
        {
            using (TextReader reader = File.OpenText("test.txt"))
            {
                string text = reader.ReadLine();
                string[] bits = text.Split(' ');
               // int x = int.Parse(bits[0]);
                //double y = double.Parse(bits[1]);
                //string z = bits[2];
            }
        }


        public void Dispose()
        {
            Console.WriteLine("Dispose");
        }
    }
    class TryDisposable
    {
    }
}
