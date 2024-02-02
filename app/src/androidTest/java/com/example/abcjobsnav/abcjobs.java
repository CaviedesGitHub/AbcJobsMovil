package com.example.abcjobsnav;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.core.content.res.TypedArrayUtils.getText;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.Matchers.allOf;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
//import androidx.test.rule.ActivityTestRule;
import static androidx.test.espresso.assertion.ViewAssertions.matches;




@LargeTest
@RunWith(AndroidJUnit4.class)
public class abcjobs {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityTestRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void Signup() {
        ViewInteraction signBtn = onView(allOf(withId(R.id.textButtonSignup), withText("Do not have an account? sign up"), isDisplayed()));
        signBtn.perform(click());

        ViewInteraction usernameTxt = onView(withId(R.id.userName));
        usernameTxt.perform(scrollTo(), replaceText("Luis_Edo10"), closeSoftKeyboard());

        ViewInteraction pwdTxt = onView(withId(R.id.password));
        pwdTxt.perform(scrollTo(), replaceText("User_10"), closeSoftKeyboard());

        ViewInteraction pwdTxt2 = onView(withId(R.id.confirmPassword));
        pwdTxt2.perform(scrollTo(), replaceText("User_10"), closeSoftKeyboard());

        ViewInteraction confirmSignupBtn = onView(allOf(withId(R.id.btnSignUp), withText("Submit")));
        confirmSignupBtn.perform(scrollTo(), click());
    }

    @Test
    public void login() throws InterruptedException {
        ViewInteraction usernameTxt = onView(withId(R.id.txtUserName));
        usernameTxt.perform(replaceText("Luis_Edo10"), closeSoftKeyboard());

        ViewInteraction pwdTxt = onView(withId(R.id.txtPassword));
        pwdTxt.perform(replaceText("User_10"), closeSoftKeyboard());

        ViewInteraction confirmLoginBtn = onView(allOf(withId(R.id.btnLogin), withText("Submit")));
        confirmLoginBtn.perform(click());

        Thread.sleep(5000);
    }

    @Test
    public void e2e() throws InterruptedException {
        ViewInteraction signBtn = onView(allOf(withId(R.id.textButtonSignup), withText("Do not have an account? sign up"), isDisplayed()));
        signBtn.perform(click());

        long ms = System.currentTimeMillis();
        String nombreUsuario="User"+Long.toString(ms);
        ViewInteraction usernameTxt = onView(withId(R.id.userName));
        usernameTxt.perform(scrollTo(), replaceText(nombreUsuario), closeSoftKeyboard());

        ViewInteraction pwdTxt = onView(withId(R.id.password));
        pwdTxt.perform(scrollTo(), replaceText("User_12345"), closeSoftKeyboard());

        ViewInteraction pwdTxt2 = onView(withId(R.id.confirmPassword));
        pwdTxt2.perform(scrollTo(), replaceText("User_12345"), closeSoftKeyboard());

        ViewInteraction confirmSignupBtn = onView(allOf(withId(R.id.btnSignUp), withText("Submit")));
        confirmSignupBtn.perform(scrollTo(), click());

        Thread.sleep(2000);

        ViewInteraction nameLogin = onView(withId(R.id.txtUserName));
        nameLogin.perform(replaceText(nombreUsuario), closeSoftKeyboard());

        ViewInteraction pwdLogin = onView(withId(R.id.txtPassword));
        pwdLogin.perform(replaceText("User_12345"), closeSoftKeyboard());

        ViewInteraction confirmLoginBtn = onView(allOf(withId(R.id.btnLogin), withText("Submit")));
        confirmLoginBtn.perform(click());

        Thread.sleep(2000);

        ViewInteraction NombreCand = onView(withId(R.id.name));
        NombreCand.perform(scrollTo(), replaceText(nombreUsuario), closeSoftKeyboard());

        ViewInteraction ApellidoCand = onView(withId(R.id.lastname));
        ApellidoCand.perform(scrollTo(), replaceText("Padilla"), closeSoftKeyboard());

        ViewInteraction DocumentoCand = onView(withId(R.id.document));
        long numero = (long) (Math.random() * 1500000000) + 1;
        String docStr = Long.toString(numero);
        DocumentoCand.perform(scrollTo(), replaceText(docStr), closeSoftKeyboard());

        ViewInteraction EmailCand = onView(withId(R.id.email));
        EmailCand.perform(scrollTo(), replaceText(nombreUsuario+"@g.com"), closeSoftKeyboard());

        ViewInteraction FechaCand = onView(withId(R.id.fechanac));
        FechaCand.perform(scrollTo(), replaceText("1980-05-09"), closeSoftKeyboard());

        ViewInteraction TelefonoCand = onView(withId(R.id.phone));
        TelefonoCand.perform(scrollTo(), replaceText("3014561234"), closeSoftKeyboard());

        ViewInteraction CiudadCand = onView(withId(R.id.city));
        CiudadCand.perform(scrollTo(), replaceText("Valledupar"), closeSoftKeyboard());

        ViewInteraction DirCand = onView(withId(R.id.address));
        DirCand.perform(scrollTo(), replaceText("Calle 10 # 17 - 27"), closeSoftKeyboard());

        ViewInteraction CreateCandBtn = onView(allOf(withId(R.id.btnCreate), withText("Submit")));
        CreateCandBtn.perform(click());

        ViewInteraction lastnameCand = onView(withId(R.id.txtApellidosCand));
        lastnameCand.check(matches(withText("Padilla")));

        ViewInteraction nameCand = onView(withId(R.id.txtNombresCand));
        nameCand.check(matches(withText(nombreUsuario)));

        ViewInteraction docCand = onView(withId(R.id.txtDocumento));
        docCand.check(matches(withText(docStr)));

        ViewInteraction fechaCand = onView(withId(R.id.txtFechaNac));
        fechaCand.check(matches(withText("1980-05-09")));

        ViewInteraction mailCand = onView(withId(R.id.txtMail));
        mailCand.check(matches(withText(nombreUsuario+"@g.com")));

        ViewInteraction telCand = onView(withId(R.id.txtTelefono));
        telCand.check(matches(withText("3014561234")));

        ViewInteraction ciudadCand = onView(withId(R.id.txtCiudad));
        ciudadCand.check(matches(withText("Valledupar")));

        ViewInteraction dirCand = onView(withId(R.id.txtDireccion));
        dirCand.check(matches(withText("Calle 10 # 17 - 27")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }
            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup &&
                        parentMatcher.matches(parent)&& view.equals(((ViewGroup)
                        parent).getChildAt(position));
            }
        };
    }
}