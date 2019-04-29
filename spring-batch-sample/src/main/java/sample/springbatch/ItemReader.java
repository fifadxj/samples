package sample.springbatch;

public interface ItemReader<T> {
    T read() throws Exception;
}
