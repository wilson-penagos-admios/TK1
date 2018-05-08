package Admios.Logic;

public abstract class Job implements Runnable{

    protected JobStatus status = JobStatus.PREPARED;

    protected String id;

    @Override
    public abstract void run();

    public String getId(){
        return id;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus jobStatus){
        status = jobStatus;
    }

}
