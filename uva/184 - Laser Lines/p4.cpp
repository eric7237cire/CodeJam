/* @JUDGE_ID: 65441EO 184    C "Laser Lines" */

/* @BEGIN_OF_SOURCE_CODE */

#include <stdio.h>

#define MAX_POINTS 300

struct coordinates
{
    int x ;
    int y ;
} ;

struct coordinates c[MAX_POINTS] ;

struct lines
{
    int points[MAX_POINTS] ;
    int numPoints ;
} ;

struct lines ln[MAX_POINTS+1] ;

int addLine(int gline, int i, int j, int k)
{
    int m, flag=0 ;

    for (m=1; m<=gline && !flag; m++)
        if (ln[m].points[i]==i && ln[m].points[j]==j && ln[m].points[k]==k)
        {
    /*        printf("Points <%d,%d> <%d,%d> <%d,%d> present on line %d\n", c[i].x, c[i].y, c[j].x, c[j].y, c[k].x, c[k].y, m) ; */
            flag=1 ;
        }
    
    if (flag)
        return 0 ;
    else
    {
        if (ln[gline].numPoints == 0)
        {
            ln[gline].points[i] = i ;
            ln[gline].points[j] = j ;
            ln[gline].points[k] = k ;
            /*
            printf("Adding point #%d <%d,%d> to gline %d\n", i, c[i].x, c[i].y, gline) ;
            printf("Adding point #%d <%d,%d> to gline %d\n", j, c[j].x, c[j].y, gline) ;
            printf("Adding point #%d <%d,%d> to gline %d\n", k, c[k].x, c[k].y, gline) ;
            */
            ln[gline].numPoints+=3 ;
        }
        else
        {
            ln[gline].points[k] = k ;
            /*
            printf("Adding point #%d <%d,%d> to gline %d\n", k, c[k].x, c[k].y, gline) ; 
            */
            ln[gline].numPoints++ ;
        }
    }

    return 1 ;
}

void debug(int gline, int L)
{
    int m, n, numPoints ;
    for (m=1; m<=gline; m++)
    {
        numPoints = 0 ;
        printf("Line #%d|%d: ", m, ln[m].numPoints) ;

        for (n=0; n<MAX_POINTS && (numPoints < ln[m].numPoints); n++)
            if (ln[m].points[n] == n)
            {
                printf("<%d,%d> ", c[n].x, c[n].y) ;
                numPoints++ ;
            }

        printf("\n") ;
    }
}

void sort(int L)
{
    int i, j, l, index ;

    for (i=0; i<=L; i++)
    {
        index = c[i].x;
        l = c[i].y;
        j = i;
        while ((j > 0) && ( (c[j-1].x > index) || ((c[j-1].x == index) && (c[j-1].y > l))) )
        {
            c[j].x = c[j-1].x;
            c[j].y = c[j-1].y;
            j = j - 1;
        }
        c[j].x = index;
        c[j].y = l ;
    }

/*
    for (i=0; i<=L; i++)
        printf("Point #%d: {%d %d}\n", i, c[i].x, c[i].y) ;
            
    printf("\n\n") ;
*/
}

int checkLines(int L)
{
    int i, j, k, count, message=0, gline ;

    for (i=0; i<=MAX_POINTS; i++)
    {
        ln[i].numPoints = 0 ;
        ln[i].points[0] = -1 ;
        for (j=1; j<=L; j++)
            ln[i].points[j] = 0 ;
    }

    gline = 0 ;

    for (i=0; i<=L-2; i++)
    {
/*        printf("starting with {%d, %d}\n", c[i].x, c[i].y) ; */
        for (j=i+1; j<=L-1; j++)
        {
            gline++ ;
            for (count=2, k=j+1; k<=L; k++)
            {
                if (((c[k].x-c[j].x) * (c[j].y-c[i].y)) == ((c[k].y-c[j].y) * (c[j].x-c[i].x)))
                {
                    if (addLine(gline, i, j, k))
                    {
                        count++ ;
                        if (count == 3) {
                            if (!message)
                            {
                                printf("The following lines were found:\n") ;
                                message = 1 ;
                            }
                            printf("(%4d,%4d)(%4d,%4d)", c[i].x, c[i].y, c[j].x, c[j].y) ;
                        }
                        printf("(%4d,%4d)", c[k].x, c[k].y) ;
                    }
                }
            }
            if (count >= 3)
                printf("\n") ;
            else
                gline-- ;
        }
    }

    if (!message)
        printf("No lines were found\n") ;

    return gline ;
}

int main()
{
    int i, j, gline ;

    #ifndef ONLINE_JUDGE
    freopen("184.in", "r", stdin) ;
    #endif

    while (1)
    {
        i = 0, j = 0 ;
        scanf("%d %d", &c[i].x, &c[i].y) ;
        if (c[i].x == 0 && c[i].y == 0)
            break ;
        while (1)
        {
            i++ ;
            scanf("%d%d", &c[i].x, &c[i].y) ;
            if (c[i].x == 0 && c[i].y == 0)
            {
                /*
                for (j=0; j<i; j++)
                    printf("{%d, %d}\n", c[j].x, c[j].y) ;                
                printf("\n") ;
                */
                sort(--i) ;
                gline = checkLines(i) ;
                /*
                debug(gline, i) ;
                */
                break ;
            }
        }
    }    

    return 0;
}

/* @END_OF_SOURCE_CODE */
