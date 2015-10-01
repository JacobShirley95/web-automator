package org.auriferous.bot.scripts.adclicker.states;

import java.util.List;

import org.auriferous.bot.script.fsm.State;
import org.auriferous.bot.scripts.adclicker.AdClicker;
import org.auriferous.bot.scripts.adclicker.Task;

public class TaskNextState extends AdClickerState{

	public TaskNextState(AdClicker adClicker) {
		super(adClicker);
	}

	@Override
	public State process(List<Integer> events) {
		adClicker.setCurrentTask(adClicker.getTasks().poll());
		
		Task currentTask = adClicker.getCurrentTask();
		
		if (currentTask != null) {
			System.out.println("Starting next task "+currentTask.url);
			if (adClicker.getBotTab() != null) {
				adClicker.closeTab(adClicker.getBotTab());
			}
			adClicker.handleTab();
		} else {
			System.out.println("Finished all tasks");

			return null;
		}
		
		adClicker.resetTimer();
		
		return new ShufflesState(adClicker);
	}
}
