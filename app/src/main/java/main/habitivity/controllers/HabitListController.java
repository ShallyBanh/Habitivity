/*
 * Copyright (c) 2017. Team CMPUT301F17T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package main.habitivity.controllers;


import android.support.annotation.Nullable;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import main.habitivity.habits.Habit;
import main.habitivity.habits.HabitEvent;
import main.habitivity.interactions.HabitInteractionsFactory;
import main.habitivity.users.User;
import main.habitivity.users.UserContainer;

/**
 * Controls getting the habits and habitevents stored in our repo. Also controls removing
 * habitEvents and habits. Will move the remove functions to a remove controller later.
 */
public class HabitListController {
    private HabitInteractionsFactory habitInteractionsFactory;
    private ArrayList<User> allUsers;
    private User currentlyLoggedInUser;


    public HabitListController(HabitInteractionsFactory habitInteractionsFactory) {
        this.habitInteractionsFactory = habitInteractionsFactory;
    }

    /**
     * Gets all the habits of the currently logged in user and all the habits of the users that the currently
     * logged in user is following
     * @return - a list of habits of the currently logged in user and the habits of the users that the currently logged in user is following
     */
    public ArrayList<Habit> getAllHabitsOfUserAndFollowing(){
        ArrayList<Habit> usersAndFollowingHabits = new ArrayList<Habit>();
        ArrayList<User> getFollowing = new ArrayList<>();
        allUsers = UserContainer.getInstance().getAllUsers();
        User currentlyLoggedInUser = UserContainer.getInstance().getUser();
        usersAndFollowingHabits.addAll(currentlyLoggedInUser.getHabits());
        getFollowing = currentlyLoggedInUser.getFollowing();
        for(User user: allUsers) {
            for(User following: getFollowing){
                if(following.getUserName().equals(user.getUserName())){
                    usersAndFollowingHabits.addAll(user.getHabits());
                    continue;
                }
            }
        }
        return usersAndFollowingHabits;
    }
    /**
     * Get a list of habits stored in our repo
     * @return list of habits
     */
    public List<Habit> getHabits() {
        currentlyLoggedInUser = UserContainer.getInstance().getUser();
        return currentlyLoggedInUser.getHabits();
    }

    /**
     * Remove the habit from our habit repo
     * @param habit to remove
     */
    public void removeHabit(Habit habit){
        habitInteractionsFactory.deleteHabit().delete(habit);
    }

    /**
     * Remove the habitEvent from our habit repo
     * @param habitEvent to remove
     */
    public void removeHabitEvent(HabitEvent habitEvent){
        habitInteractionsFactory.removeHabitEvent().remove(habitEvent);
    }

    /**
     * Gets the list of HabitEvents in our repo
     * @return a list of habit events
     */
    public List<HabitEvent> getHabitEvents() {
        currentlyLoggedInUser = UserContainer.getInstance().getUser();
        return currentlyLoggedInUser.getHabitEvents();
    }

    public List<HabitEvent> getSortedEvents(final String filter) {
        currentlyLoggedInUser = UserContainer.getInstance().getUser();
        List<HabitEvent> list = currentlyLoggedInUser.getHabitEvents();
        Collections.sort(list, new Comparator<HabitEvent>() {
            @Override
            public int compare(HabitEvent habitEvent, HabitEvent t1) {
                return t1.getCompletionDate().compareTo(habitEvent.getCompletionDate());
            }
        });
        if (!filter.isEmpty()) {
            list = Lists.newArrayList(Collections2.filter(list, new com.google.common.base.Predicate<HabitEvent>() {
                        @Override
                        public boolean apply(@Nullable HabitEvent habitEvent) {
                            return (habitEvent.getComment().contains(filter) || habitEvent.getId().contains(filter));
                        }
                    }
            ));
        }

        return list;
    }


}