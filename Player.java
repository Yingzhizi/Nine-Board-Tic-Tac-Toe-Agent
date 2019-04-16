public enum Player {
    x,
    o;

    public static class Pair<T,Y>
    {
        public T first;
        public Y second;
        public Pair(T f, Y s) {
            first = f;
            second = s;
        }
    }
}
