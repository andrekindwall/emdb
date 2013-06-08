package com.emjaay.mdb.communication;

import java.util.ArrayList;

import com.emjaay.mdb.communication.AbstractAsyncTask.CommunicationCallback;
import com.emjaay.mdb.communication.result.ApiResult;
import com.emjaay.mdb.exception.TaskException;

public class TaskQueue implements CommunicationCallback {
	
	private ArrayList<AbstractAsyncTask> mQueue;
	
	private CommunicationCallback mCurrentTaskCallback;
	private TaskQueueCallback mCallback;
	
	private int mQueueSize = 0;
	private int mCurrent = 0;
	
	private boolean mRunning;
	private boolean mFinished;
	
	public interface TaskQueueCallback{
		public void taskQueueProgress(int current, int queueSize);
		public void taskQueueFinished();
	}
	
	public TaskQueue(){
		mQueue = new ArrayList<AbstractAsyncTask>();
	}
	
	public void setTaskQueueCallback(TaskQueueCallback callback) {
		mCallback = callback;
	}
	
	public void addTask(AbstractAsyncTask task) throws TaskException {
		if(mFinished){
			throw new TaskException("Cannot add task, queue already finished all tasks");
		}
		mQueue.add(task);
		mQueueSize++;
	}
	
	public void execute() throws TaskException {
		if(mRunning){
			throw new TaskException("Queue already executed");
		}
		mRunning = true;
		
		next();
	}
	
	private void next() {
		if(mQueue.size() > 0){
			mCurrent++;
			if(mCallback != null){
				mCallback.taskQueueProgress(mCurrent, mQueueSize);
			}
			AbstractAsyncTask task = mQueue.get(0);
			mCurrentTaskCallback = task.getCommunicationCallback();
			task.setCommunicationCallback(this);
			task.execute((Void) null);
			mQueue.remove(0);
		} else {
			mFinished = true;
			if(mCallback != null){
				mCallback.taskQueueFinished();
			}
		}
	}

	@Override
	public void taskComplete(AbstractAsyncTask task, ApiResult result) {
		if(mCurrentTaskCallback != null){
			mCurrentTaskCallback.taskComplete(task, result);
		}
		next();
	}

	@Override
	public void taskCancelled(AbstractAsyncTask task) {
		if(mCurrentTaskCallback != null){
			mCurrentTaskCallback.taskCancelled(task);
		}
		next();
	}

}
