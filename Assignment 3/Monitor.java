/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Monitor
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */
	private enum Status{eating, hungry, thinking};
	private boolean isTalking=false;
	private int numOfChopstick;
	private Status[] philosopher_state;


	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		//set appropriate number of chopsticks based on the # of philosophers
		this.numOfChopstick= piNumberOfPhilosophers;
		philosopher_state=new Status[piNumberOfPhilosophers];
		for (int i=0; i < piNumberOfPhilosophers; i++) {
			philosopher_state[i]=Status.thinking;
		}
		isTalking=false;
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */
	public synchronized void test(final int piTID)
	{
		if (philosopher_state[(piTID + 1) % numOfChopstick] != Status.eating &&
				philosopher_state[(piTID-1 + numOfChopstick) % numOfChopstick] != Status.eating &&
						philosopher_state[piTID] == Status.hungry)
		{
			philosopher_state[piTID] = Status.eating;
			notifyAll();
		}
	}
	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID)
	{
		// ...
		int position=piTID-1;
		philosopher_state[position]=Status.hungry;
		test(position);
		
		if(philosopher_state[position]!= Status.eating) {
			try {
				wait();
				pickUp(piTID);
			} catch (InterruptedException e) {
				System.out.println("The Philosopher No." + piTID + " cannot pick up fork!");
			}
		}
		
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		// ...
		int position=piTID-1;
		philosopher_state[position]=Status.thinking;
		notifyAll();
		
	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk()
	{
		// ...
		if(isTalking) {
			try {
				wait();
				requestTalk();
			} catch(InterruptedException e ) {
				System.out.println("The Philosopher cannot talk as someone is already talking!");
			}
		}
		isTalking=true;
		
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk()
	{
		// ...
		isTalking=false;
		notifyAll();
	}
}

// EOF
