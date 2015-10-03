package hackzurich.pictureshareapp.container;

/**
 * Created by Johannes on 10/3/2015.
 */
public interface IShare {
    public static enum TYPE {
        SHARE(1),
        HEADER(2);

        private final int value;
        private TYPE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public TYPE getType();
}
