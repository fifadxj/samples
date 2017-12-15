package sample.pooldesign;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TaskPoolServer {
    public static void main(String[] args) throws IOException {
        TaskPool pool = new TaskPool();
        pool.start();

        ServerSocket server = new ServerSocket(8888);
        while (true) {
            System.out.println("waiting.....");
            Socket connection = server.accept();
            pool.assignTask(connection);
        }
    }
}

class TaskPool {
    private static final int POOL_SIZE = 5;

    private List<Socket> connections = new ArrayList<Socket>();

    public void start() {
        for (int i = 0; i < POOL_SIZE; i++) {
            LogThread thread = new LogThread(i + 1, this);
            thread.start();
        }
    }

    public Socket fetchTask() {
        synchronized (connections) {
            while (connections.isEmpty()) {
                try {
                    connections.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            Socket connection = connections.remove(0);

            return connection;
        }
    }

    public void assignTask(Socket connection) {
        synchronized (connections) {
            connections.add(connection);
            connections.notifyAll();
        }
    }
}

class LogThread extends Thread {
    private int id;

    private TaskPool pool;

    public LogThread(int id, TaskPool pool) {
        this.id = id;
        this.pool = pool;
    }

    public void run() {
        while (true) {
            Socket connection = pool.fetchTask();
            try {
				doWork(connection);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

	private void doWork(Socket connection) throws Exception {

		InputStream in = connection.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
		StringBuffer sb = new StringBuffer();
        String line = reader.readLine();
        while (line != null && line.trim().length() > 0) {
        	sb.append(line + "\n");
            line = reader.readLine();
        }
		
		//System.out.println("Thread[" + id + "]  " + sb.toString());
		
		OutputStream out = connection.getOutputStream();
		PrintWriter writer = new PrintWriter(out);
        writer.println("HTTP/1.1 200 OK");
        //writer.println("Content-length: 10");
        writer.println("Content-type: text/html");
        writer.println();
        writer.println("Thread[" + id + "] responds");
		writer.close();
		connection.close();
		Thread.sleep(1000);
	}
}
