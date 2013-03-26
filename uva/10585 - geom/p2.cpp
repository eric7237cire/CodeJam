int main() {
    int n, t;
    scanf("%d", &t);
    while (t-- > 0) {
        scanf("%d", &n);
        int x, y;
        vector<point> v;
        for (int i = 0; i < n; i++) {
            scanf("%d %d", &x, &y);
            point p(x, y);
            v.push_back(p);
        }
        if (n == 1)
            cout << "yes\n";
        else {
            bool check = true;
            sort(v.begin(), v.end(), xCmp);
            int mid = n / 2;
            double xx = (v[mid].X + v[mid - 1].X) / 2;
            point m;
            if (n % 2 == 1) {
                m = v[mid];
                xx = v[mid].X;
            }
            point p1(xx, -1000);
            point p2(xx, 1000);
            if (n % 2 == 0) {
                line_line_intersection(v[0], v[n - 1], p1, p2, m);
            }
 
            for (int i = 0; i < mid && check; i++) {
                point tmp;
                line_line_intersection(v[i], v[n - 1 - i], p1, p2, tmp);
                double l1 = length(vec(tmp,v[i]));
                double l2 = length(vec(tmp,v[n-i-1]));
 
                if (!(fabs(l1 - l2) < EPS)) {
                    check = false;
                }
            }
            if (check)
                cout << "yes\n";
            else
                cout << "no\n";
 
        }
    }
    return 0;
}