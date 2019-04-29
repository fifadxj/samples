package sample.springbatch;

public interface ItemProcessor<I, O> {
    O process(I item) throws Exception;
}
