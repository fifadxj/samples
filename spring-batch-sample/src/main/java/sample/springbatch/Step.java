package sample.springbatch;

import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Step<T> {
    private ItemReader<T> reader;
    private ItemWriter<T> writer;
    private ItemProcessor<T, T> processor;
    private int chunkSize;
    private TransactionTemplate transactionTemplate;
    private JobRepository jobRepository;
    private String name;

    public Step(String name) {
        this.name = name;
    }

    public static void main(String[] args) throws Exception {
        Step<String> step = new Step<>("syncLoan");
        step.setChunkSize(10);
        step.setReader(new PageReader<String>());
        step.setProcessor(null);
        step.setWriter(null);

        StepExecution stepExecution = new StepExecution(step.getName());
        ExecutionContext executionContext = step.getJobRepository().getLastStepExecutionContext(stepExecution.getId());
        stepExecution.setExecutionContext(executionContext);
        step.execute(stepExecution);

        System.out.println(stepExecution.getStatus());
        System.out.println(stepExecution.getReadCount());
        System.out.println(stepExecution.getWriteCount());
        System.out.println(stepExecution.getCommitCount());
    }

    public void execute(StepExecution stepExecution) throws Exception {
        while(true) {
            RepeatStatus status = transactionTemplate.execute(new TransactionCallback<RepeatStatus>() {
                @Override
                public RepeatStatus doInTransaction(TransactionStatus status) {
                    try {
                        RepeatStatus result = Step.this.executeChunk(stepExecution);

                        if (reader instanceof ItemStream) {
                            ((ItemStream) reader).update(stepExecution.getExecutionContext());
                        }
                        if (writer instanceof ItemStream) {
                            ((ItemStream) writer).update(stepExecution.getExecutionContext());
                        }
                        if (processor instanceof ItemStream) {
                            ((ItemStream) processor).update(stepExecution.getExecutionContext());
                        }

                        stepExecution.incrementCommitCount();
                        jobRepository.update(stepExecution);

                        jobRepository.updateExecutionContext(stepExecution.getExecutionContext());

                        return result;
                    } catch (Exception e) {
                        stepExecution.setStatus("FAIL");
                        throw new RuntimeException(e);
                    }
                }
            });

            if (status == RepeatStatus.FINISHED) {
                break;
            }
        }

        stepExecution.setStatus("SUCCESS");
    }

    public RepeatStatus executeChunk(StepExecution stepExecution) throws Exception {
        List<T> items = readChunk(stepExecution);
        for (T item : items) {
            processor.process(item);
        }
        writer.write(items);
        stepExecution.incrementWriteCount();

        if (items.size() == 0) {
            return RepeatStatus.FINISHED;
        } else {
            return RepeatStatus.CONTINUABLE;
        }
    }

    public List<T> readChunk(StepExecution stepExecution) throws Exception {
        List<T> items = new ArrayList<>();
        for (int i = 0; i < chunkSize; i++) {
            T item = reader.read();
            if (item == null) {
                break;
            }
            items.add(item);
            stepExecution.incrementReadCount();
        }

        return items;
    }
}

