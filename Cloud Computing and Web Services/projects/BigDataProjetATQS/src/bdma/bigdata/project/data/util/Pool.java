package bdma.bigdata.project.data.util;

import java.util.ArrayList;

public class Pool<E> extends ArrayList<E> {

    private static final long serialVersionUID = 1L;

    public E getRandom() {
        return super.get(Random.getInteger(0, this.size() - 1));
    }
}
