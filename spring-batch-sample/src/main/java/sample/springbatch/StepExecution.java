package sample.springbatch;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StepExecution {
    private String id;
    private String stepName;
    private String status;
    private int readCount = 0;
    private int writeCount = 0;
    private int commitCount = 0;
    private ExecutionContext executionContext;

    public StepExecution(String stepName) {
        this.stepName = stepName;
    }

    public void incrementReadCount() {
        readCount ++;
    }

    public void incrementWriteCount() {
        writeCount ++;
    }

    public void incrementCommitCount() {
        commitCount ++;
    }

}
