package main.habitivity;

import android.test.ActivityInstrumentationTestCase2;

import main.habitivity.users.User;


public class UserTests extends ActivityInstrumentationTestCase2 {

    public UserTests() {
        super (main.habitivity.HabitivityMain.class);
    }

    public void testUser() {
        User user = new User();
        user.setUserName("test");

        assertEquals(user.getUserName(), "test");
    }
}
