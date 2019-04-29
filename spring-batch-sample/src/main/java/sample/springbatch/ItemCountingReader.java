package sample.springbatch;

public abstract class ItemCountingReader<T> implements ItemReader<T>, ItemStream {
    private Integer currentReadCount = 0;

    private static final String KEY_READ_COUNT = "READ_COUNT";

    @Override
    public T read() {
        currentReadCount ++;
        T item = doRead();

        return item;
    }

    @Override
    public void open(ExecutionContext executionContext) {
        Integer itemCount = (Integer) executionContext.get(KEY_READ_COUNT);
        if (itemCount != null) {
            currentReadCount = itemCount;
        } else {
            currentReadCount = 0;
        }
        jumpToItem(currentReadCount);
    }

    @Override
    public void update(ExecutionContext executionContext) {
        executionContext.put(KEY_READ_COUNT, currentReadCount);
    }

    @Override
    public void close() {

    }

    protected void jumpToItem(int itemIndex) {
        for (int i = 0; i < itemIndex; i++) {
            read();
        }
    }

    protected abstract T doRead();
}
