package Admios;

import Admios.Logic.Job;
import Admios.Logic.JobExecutor;
import Admios.Logic.JobStatus;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AppTest 
{

    JobExecutor testExecutor = new JobExecutor();

    class SimpleJob extends Job {

        private Integer wait;

        SimpleJob(String id, Integer wait){
            this.id = id;
            this.wait = wait;
        }

        @Override
        public void run(){
            synchronized (this) {
                try {
                    wait(wait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    status = JobStatus.FINISHED;
                }
            }
        }
    }

    class ExceptionJob extends Job{

        ExceptionJob(String id){
            this.id = id;
        }

        @Override
        public void run(){
            status = JobStatus.EXCEPTION;
        }
    }

    SimpleJob testJob1;
    SimpleJob testJob2;
    SimpleJob testJob3;
    ExceptionJob exceptionJob;

    @Before
    public void before(){
        testJob1 = new SimpleJob("Process Juanita", 10);
        testJob2 = new SimpleJob("PROCES1-2A", 20);
        testJob3 = new SimpleJob("Process Night 01/02?2003", 30);
        exceptionJob = new ExceptionJob("Exception Job");
    }

    @Test
    public void allJobsShouldExist(){
        assertTrue(testJob1 != null);
        assertTrue(testJob2 != null);
        assertTrue(testJob3 != null);
    }

    @Test
    public void shouldExecuteSingleJobSuccessfully(){
        testExecutor.add(testJob1);
        synchronized (this) {
            try {
                wait(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertEquals(JobStatus.FINISHED, testJob1.getStatus());
    }

    @Test
    public void shouldExecuteMultipleJobsSuccessfully(){
        testExecutor.add(testJob1);
        testExecutor.add(testJob2);
        synchronized (this) {
            try {
                wait(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertEquals("Process Juanita:FINISHED", testExecutor.getResults().get(0));
        assertEquals("PROCES1-2A:FINISHED", testExecutor.getResults().get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowExceptionOnUncompletedJob(){
        testExecutor.add(testJob1);
        testExecutor.add(testJob2);
        testExecutor.add(testJob3);
        synchronized (this) {
            try {
                wait(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        testExecutor.getResults().get(2);
    }

    @Test
    public void shouldCompleteExceptionJobSuccessfully(){
        testExecutor.add(exceptionJob);
        synchronized (this) {
            try {
                wait(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertEquals(JobStatus.EXCEPTION, exceptionJob.getStatus());
        assertEquals("Exception Job:EXCEPTION", testExecutor.getResults().get(0));
    }

}
