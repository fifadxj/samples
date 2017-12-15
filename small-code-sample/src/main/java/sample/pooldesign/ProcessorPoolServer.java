package sample.pooldesign;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ProcessorPoolServer {
    public static void main(String[] args) throws IOException {
        final ProcessorPool pool = new ProcessorPool();

        ServerSocket server = new ServerSocket(88);

        while (true) {
            System.out.println("waiting.....");
            final Socket connection = server.accept();
            final Processor processor = pool.getProcessor();
            processor.process(connection);
        }
    }
}

class ProcessorPool {
    private static final int POOL_SIZE = 5;
    private List<Processor> processors;
    
    public ProcessorPool() {
        processors = new ArrayList<Processor>();
        for (int i = 0; i < POOL_SIZE; i++) {
            Processor processor = new Processor(i + 1, this);
            processors.add(processor);
        }
    }
    
    public Processor getProcessor() {
        synchronized (processors) {
            while (processors.size() == 0) {
                try {
                    processors.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            Processor processor = processors.remove(0);
            
            return processor;
        }
    }
    
    public void returnProcessor(Processor processor) {
        synchronized (processors) {
            processors.add(processor);
            processors.notifyAll();
        }
    }
}

class Processor {
    private int id;
    private ProcessorPool pool;
    
    public Processor(int id, ProcessorPool pool) {
        this.id = id;
        this.pool = pool;
    }
    public void process(Socket connection) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() { 
                for (int i = 0; i < 10; i++) {
                    System.out.println("Processor[" + id + "] is working[" + (i + 1) + "]");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                pool.returnProcessor(Processor.this);
            }
        });

        thread.start();
    }
    
    public void close() {
        pool.returnProcessor(this);
    }
}