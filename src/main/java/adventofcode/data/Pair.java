package adventofcode.data;

public record Pair<I, B>(I first, B second) {

    public I getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pair) {
            // not null safe
            return first.equals(((Pair<?, ?>) obj).getFirst()) && second.equals(((Pair<?, ?>) obj).getSecond());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + first.hashCode();
        result = 31 * result + second.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return first.toString() + ":" + second.toString();
    }
}
