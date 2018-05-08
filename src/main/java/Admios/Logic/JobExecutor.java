package Admios.Logic;

import java.util.ArrayList;
import java.util.LinkedList;

public class JobExecutor {

    private LinkedList<Job> jobs;
    private Thread jobMonitor;
    private ArrayList<String> results;

    public JobExecutor (){
        jobs = new LinkedList();
        results = new ArrayList();
        jobMonitor = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                while (!Thread.currentThread().isInterrupted()){
                    if(jobs.size() > 0 ){
                        if(jobs.getFirst().getStatus().equals(JobStatus.PREPARED)) {
                            jobs.getFirst().setStatus(JobStatus.STARTED);
                            jobs.getFirst().run();
                        }
                        else if (jobs.getFirst().getStatus().equals(JobStatus.FINISHED) || jobs.getFirst().getStatus().equals(JobStatus.EXCEPTION)) {
                            Job resJob = jobs.removeFirst();
                            results.add(resJob.getId() + ":" + resJob.getStatus());
                        }
                    }
                }

            }
        });
        jobMonitor.start();
    }

    public void add(Job job){
        jobs.offer(job);
    }

    public ArrayList<String> getResults(){
        return results;
    }

}
