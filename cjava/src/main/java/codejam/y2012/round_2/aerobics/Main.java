package codejam.y2012.round_2.aerobics;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.collect.Lists;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
        // return new String[] {"sample.in"};
        return new String[] { "B-small-practice.in", "B-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData in = new InputData(testCase);
        
        
        in.N = scanner.nextInt();
        in.W = scanner.nextInt();
        in.L = scanner.nextInt();
        
        in.r = new int[in.N];
        
        for(int n = 0; n < in.N; ++n) {
            in.r[n] = scanner.nextInt();
        }
        
        return in;
    }

    class Rectangle implements Comparable<Rectangle> {
        //bottom, left corner
        final int x;
        final int y;
        
        final int width;
        final int height;
        
        final int x2;
        final int y2;

        public Rectangle(int x, int y, int width, int height) {
            super();
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.x2 = x + width;
            this.y2 = y + height;
        }

        @Override
        public int compareTo(Rectangle o) {
            return Integer.compare(Math.min(width, height),
                    Math.min(o.width, o.height));
            
        }

        @Override
        public String toString() {
            return String.format("Rect (%d, %d) w=%d h=%d (%d, %d)",
                    x, y, width, height, x+width, y+height);
        }
    }
    
    class Student {
        int x;
        int y;
        int index;
        int radius;
        public Student(int index, int radius) {
            super();
            this.x = -1;
            this.y = -1;
            this.index = index;
            this.radius = radius;
        }
    }
    
    public String handleCase(InputData in) {

        Rectangle mat = new Rectangle(0, 0, in.W, in.L);
    
        List<Student> studentList = Lists.newArrayList();
        
        for(int i = 0; i < in.N; ++i) {
            studentList.add(new Student(i, in.r[i]));
        }
        
        Collections.sort(studentList, new Comparator<Student>() {

            @Override
            public int compare(Student o1, Student o2) {
                return Integer.compare(o2.radius, o1.radius);
            }            
        });
        
        TreeSet<Rectangle> rectangles = new TreeSet<>();
        rectangles.add(mat);
        
        for(int slIdx = 0; slIdx < in.N; ++slIdx) {
            Student student = studentList.get(slIdx);
            
            //Go  through rectangles smallest to largest
            Iterator<Rectangle> rectIt = rectangles.iterator();
            
            Rectangle rect1 = null;
            Rectangle rect2 = null;
            
            while(rectIt.hasNext()) {
                Rectangle rect = rectIt.next();
                
                int minX = rect.x == 0 ? rect.x : rect.x + student.radius;
                int maxX = rect.x2 == mat.width ? rect.x + rect.width :
                    rect.x + rect.width - student.radius;
                
                int minY = rect.y == 0 ? rect.y : rect.y + student.radius;
                int maxY = rect.y2 == mat.height ?
                        rect.y + rect.height :
                        rect.y + rect.height - student.radius;
                
                
                if (maxX < minX || maxY < minY )
                    continue;
                
                student.x = minX;
                student.y = minY;
                
                int newRectY1 = minY + student.radius;
                
                int newRectX1 = minX + student.radius;
                
                
                
                if (rect.height > rect.width) {
                 rect1 = new Rectangle(rect.x, newRectY1, rect.width, 
                        rect.y2 - newRectY1 );
                
                 rect2 = new Rectangle(newRectX1, 
                        rect.y, rect.x2 - newRectX1, 
                        newRectY1 - rect.y );
                } else {
                    rect1 = new Rectangle(newRectX1, rect.y, rect.x2 - newRectX1, 
                            rect.height );
                    
                     rect2 = new Rectangle(rect.x, 
                            newRectY1, newRectX1 - rect.x, 
                            rect.y2 - newRectY1 );    
                }
                
                rectIt.remove();
                break;
            }
            
            rectangles.add(rect1);
            rectangles.add(rect2);
        }
        
        Collections.sort(studentList, new Comparator<Student>() {

            @Override
            public int compare(Student o1, Student o2) {
                return Integer.compare(o1.index, o2.index);
            }            
        });
               
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("Case #%d: ", in.testCase));
        for(Student s : studentList) {
            sb.append(s.x);
            sb.append(" ");
            sb.append(s.y);
            sb.append(" ");
        }
        return sb.toString();
    }

}
